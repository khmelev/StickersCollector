package ru.av3969.stickerscollector.data;

import android.support.v4.util.LongSparseArray;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.av3969.stickerscollector.data.db.DbHelper;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.data.db.entity.CatalogStickers;
import ru.av3969.stickerscollector.data.db.entity.DepositoryCollection;
import ru.av3969.stickerscollector.data.db.entity.DepositoryStickers;
import ru.av3969.stickerscollector.data.db.entity.Transaction;
import ru.av3969.stickerscollector.data.db.entity.TransactionRow;
import ru.av3969.stickerscollector.data.pref.PreferencesHelper;
import ru.av3969.stickerscollector.data.remote.LaststickerHelper;
import ru.av3969.stickerscollector.ui.vo.CollectionVO;
import ru.av3969.stickerscollector.ui.vo.StickerVO;
import ru.av3969.stickerscollector.ui.vo.TransactionVO;

public class AppDataManager implements DataManager {

    private PreferencesHelper preferencesHelper;
    private DbHelper dbHelper;
    private LaststickerHelper laststickerHelper;

    @Inject
    AppDataManager(PreferencesHelper preferencesHelper, DbHelper dbHelper,
                          LaststickerHelper laststickerHelper) {
        this.preferencesHelper = preferencesHelper;
        this.dbHelper = dbHelper;
        this.laststickerHelper = laststickerHelper;
    }

    @Override
    public Single<List<CatalogCategory>> loadCategoryList(Long parentId) {
         return Single.fromCallable(() -> {
             List<CatalogCategory> categoryList = dbHelper.selectCategoryList(parentId);
             if(categoryList.isEmpty()) {
                 List<CatalogCategory> lastStickerCategory = laststickerHelper.getCategoryList();
                 if(parentId.equals(CatalogCategory.defaultId))
                     dbHelper.inflateCategories(lastStickerCategory);
                 else
                    dbHelper.updateCategories(lastStickerCategory);

                 categoryList = dbHelper.selectCategoryList(parentId);
             }
             return categoryList;
         });
    }

    @Override
    public Single<List<CatalogCollection>> loadCatalogCollectionList(Long categoryId) {
        return Single.fromCallable(() -> {
            List<CatalogCollection> collectionList = dbHelper.selectCatalogCollectionList(categoryId);
            if(collectionList.isEmpty()) {
                CatalogCategory category = dbHelper.selectCategory(categoryId);
                if (category != null) {
                    dbHelper.inflateCollections(laststickerHelper.getCollectionList(category.getName(), categoryId));
                    collectionList = dbHelper.selectCatalogCollectionList(categoryId);
                }
            }
            return collectionList;
        });
    }

    @Override
    public Single<CatalogCollection> loadCatalogCollection(Long id) {
        return Single.fromCallable(() -> dbHelper.selectCatalogCollection(id));
    }

    @Override
    public Single<CollectionVO> loadCollectionVO(Long parentCollection, Long collectionId) {
        return Single.fromCallable(() -> {
            DepositoryCollection depCollection = dbHelper.selectDepositoryCollection(collectionId);
            if (depCollection == null) {
                return new CollectionVO(dbHelper.selectCatalogCollection(parentCollection));
            } else {
                return new CollectionVO(dbHelper.selectCatalogCollection(parentCollection), depCollection);
            }
        });
    }

    @Override
    public Single<List<CollectionVO>> loadCollectionsVO() {
        return Single.fromCallable(() -> {
            List<CollectionVO> collectionsVO = new ArrayList<>();
            List<DepositoryCollection> depCollections = dbHelper.selectDepositoryCollectionList();
            for(DepositoryCollection depCollection : depCollections) {
                collectionsVO.add(new CollectionVO(depCollection.getCollection(), depCollection));
            }
            return collectionsVO;
        });
    }

    @Override
    public Single<List<StickerVO>> loadStickerVOList(Long parentCollection, Long depCollectionId) {
        return Single.fromCallable(() -> {
            List<StickerVO> stickers = new ArrayList<>();
            LongSparseArray<DepositoryStickers> depositoryStickersMap = new LongSparseArray<>();

            //Загрузка списка стикеров из каталога
            List<CatalogStickers> catalogStickers = dbHelper.selectCatalogStickersList(parentCollection);
            //Если в базе пусто то загрузим из интернета
            if(catalogStickers.isEmpty()) {
                CatalogCollection catalogCollection = dbHelper.selectCatalogCollection(parentCollection);
                dbHelper.inflateStickers(laststickerHelper.getStickersList(catalogCollection.getName(), parentCollection));
                catalogStickers = dbHelper.selectCatalogStickersList(parentCollection);
            }

            //Загрузка списка стикеров из депозитория и помещение в LongSparseArray для быстрого поиска по ключу
            if (depCollectionId != null) {
                List<DepositoryStickers> depositoryStickers = dbHelper.selectDepositoryStickersList(depCollectionId);
                if(!depositoryStickers.isEmpty()) {
                    for (DepositoryStickers depSticker : depositoryStickers) {
                        depositoryStickersMap.put(depSticker.getStickerId(), depSticker);
                    }
                }
            }

            for (CatalogStickers catSticker : catalogStickers) {
                if (depositoryStickersMap.size() > 0) {
                    DepositoryStickers depSticker = depositoryStickersMap.get(catSticker.getId());
                    if (depSticker != null)
                        stickers.add(new StickerVO(catSticker, depSticker));
                    else
                        stickers.add(new StickerVO(catSticker));
                } else {
                    stickers.add(new StickerVO(catSticker));
                }
            }

            return stickers;
        });
    }

    @Override
    public Single<DepositoryCollection> loadDepositoryCollection(Long id) {
        return Single.fromCallable(() -> dbHelper.selectDepositoryCollection(id));
    }

    @Override
    public Completable saveCollection(CollectionVO collectionVO, List<StickerVO> stickersVO, Transaction transaction) {
        return Completable.fromCallable(() -> {
            saveCollectionNow(collectionVO, stickersVO, transaction);
            return true;
        });
    }

    private void saveCollectionNow(CollectionVO collectionVO, List<StickerVO> stickersVO, Transaction transaction) {
        if(collectionVO.isNew()) {
            //Записываем коллекцию
            collectionVO.setId(dbHelper.insertDepositoryCollection(new DepositoryCollection(collectionVO)));
        }

        //Находим измененные стикеры, маркером того что стикер нужно записать является
        //расхождение между quantity и startQuantity
        Integer totalQuantity = 0;
        Short uniqueQuantity = 0;
        List<StickerVO> hasChangedStickersVO = new ArrayList<>();
        List<DepositoryStickers> depStickers = new ArrayList<>();
        List<StickerVO> newStickersVO = new ArrayList<>();
        for(StickerVO stickerVO : stickersVO) {
            short deltaQuantity = (short)(stickerVO.getQuantity() - stickerVO.getStartQuantity());
            if(deltaQuantity != 0) {
                hasChangedStickersVO.add(stickerVO);

                DepositoryStickers depSticker = stickerVO.getDepSticker();
                if(depSticker == null) {
                    depSticker = new DepositoryStickers(stickerVO);
                    depSticker.setOwnerId(collectionVO.getId());
                    stickerVO.setDepSticker(depSticker);
                    newStickersVO.add(stickerVO);
                } else {
                    depSticker.setQuantity(stickerVO.getQuantity());
                }
                depStickers.add(depSticker);
            }
            totalQuantity += stickerVO.getQuantity();
            if(stickerVO.getQuantity() > 0) {
                uniqueQuantity++;
            }
        }
        if(hasChangedStickersVO.isEmpty()) return; //Если нет измененных стикеров то дальше код можно не выполнять

        dbHelper.insertDepositoryStickersList(depStickers); //Записываем стикеры в депозиторий

        //Обновим id у VO стикеров
        for (StickerVO stickerVO : newStickersVO) {
            stickerVO.setId(stickerVO.getDepSticker().getId());
        }

        //Обновляем количество в депозитории
        DepositoryCollection depCollection = dbHelper.selectDepositoryCollection(collectionVO.getId());
        depCollection.setQuantity(totalQuantity);
        depCollection.setUnique(uniqueQuantity);
        dbHelper.insertDepositoryCollection(depCollection);

        //Записываем транзакцию
        saveTransaction(depCollection.getId(), hasChangedStickersVO, transaction);

        //Обнуляем расхождение между quantity и startQuantity
        syncQuantity(hasChangedStickersVO);
    }

    private void saveTransaction(Long depCollectionId, List<StickerVO> hasChangedStickersVO, Transaction transaction) {

        if(transaction.isNew()) {
            List<TransactionRow> transactionRowList = new ArrayList<>();
            int transactionQuantity = 0;
            for (StickerVO stickerVO : hasChangedStickersVO) {
                short deltaQuantity = (short) (stickerVO.getQuantity() - stickerVO.getStartQuantity());
                transactionRowList.add(new TransactionRow(null, null, stickerVO.getId(), deltaQuantity));
                transactionQuantity += deltaQuantity;
            }
            transaction.setDate(new Date());
            transaction.setType(Transaction.addType);
            transaction.setCollectionId(depCollectionId);
            transaction.setQuantity(transactionQuantity);
            transaction.setActive(true);
            dbHelper.insertTransaction(transaction);

            for (TransactionRow transactionRow : transactionRowList) {
                transactionRow.setOwnerId(transaction.getId());
            }
            dbHelper.updateTransactionRowList(transactionRowList);
        }
    }

    private void syncQuantity(List<StickerVO> stickersVO) {
        for (StickerVO sticker : stickersVO) {
            sticker.flushStartQuantity();
        }
    }

    @Override
    public Single<List<TransactionVO>> loadTransactionList(Long collectionId) {
        return Single.fromCallable(() -> {
            List<TransactionVO> transactionsVO = new ArrayList<>();
            for(Transaction trans : dbHelper.selectTransactionList(collectionId)) {
                List<TransactionRow> transactionRows = dbHelper.selectTransactionRowList(trans.getId());
                StringBuilder transStickersText = new StringBuilder();
                for (TransactionRow transactionRow : transactionRows) {
                    if(transactionRow.getQuantity() != 0) {
                        if(transStickersText.length() > 0) transStickersText.append(", ");
                        transStickersText.append(transactionRow.getSticker().getSticker().getNumber());
                        if(transactionRow.getQuantity() > 1) transStickersText.append("("+transactionRow.getQuantity()+")");
                        if(transactionRow.getQuantity() < -1) transStickersText.append("("+-transactionRow.getQuantity()+")");
                    }
                }
                TransactionVO transactionVO = new TransactionVO(trans.getId(), trans.getDate(), trans.getTitle(), trans.getActive(), trans);
                transactionVO.setTransStickersText(transStickersText.toString());
                transactionsVO.add(transactionVO);
            }
            return transactionsVO;
        });
    }

    @Override
    public Single<List<StickerVO>> loadTransactionRowList(TransactionVO transaction) {
        return Single.fromCallable(() -> {
            List<StickerVO> stickersVO = new ArrayList<>();
            List<TransactionRow> transRowList = dbHelper.selectTransactionRowList(transaction.getTransaction().getId());
            for (TransactionRow transactionRow : transRowList) {
                stickersVO.add(new StickerVO(transactionRow.getSticker().getSticker(), transactionRow));
            }
            return stickersVO;
        });
    }

    @Override
    public Completable saveTransactionRowList(CollectionVO collectionVO, List<StickerVO> stickersVO,
                                              TransactionVO transactionVO, List<StickerVO> tranStickersVO) {
        return Completable.fromCallable(() -> {

            List<TransactionRow> transactionRowList = new ArrayList<>();

            for (StickerVO tranStickerVO : tranStickersVO) {
                short deltaQuantity = (short)(tranStickerVO.getQuantity() - tranStickerVO.getStartQuantity());
                if(deltaQuantity != 0) {
                    TransactionRow transactionRow = tranStickerVO.getTransactionRow();
                    transactionRow.setQuantity(tranStickerVO.getQuantity());
                    transactionRowList.add(transactionRow);
                }
            }

            if(transactionVO.getActive()) {
                //Для активной транзакции нужно еще обновить стикеры в коллекции
                LongSparseArray<StickerVO> savedStickers = new LongSparseArray<>();
                for (StickerVO stickerVO : stickersVO) {
                    if (stickerVO.getDepSticker() != null)
                        savedStickers.put(stickerVO.getId(), stickerVO);
                }
                for (StickerVO tranStickerVO : tranStickersVO) {
                    short deltaQuantity = (short)(tranStickerVO.getQuantity() - tranStickerVO.getStartQuantity());
                    if(deltaQuantity != 0) {
                        StickerVO savedSticker = savedStickers.get(tranStickerVO.getTransactionRow().getStickerId());
                        if (savedSticker != null) {
                            savedSticker.incQuantityVal(deltaQuantity);
                        }
                    }
                }
                saveCollectionNow(collectionVO, stickersVO, transactionVO.getTransaction());
            }

            dbHelper.updateTransactionRowList(transactionRowList);
            syncQuantity(tranStickersVO);

            return true;
        });
    }

    @Override
    public Single<List<StickerVO>> parseStickers(CharSequence stickerString, List<StickerVO> availableList) {
        return Single.fromCallable(() -> {
            List<StickerVO> parsedStickersList = new ArrayList<>();
            if(TextUtils.isEmpty(stickerString)) return parsedStickersList;

            Map<String, StickerVO> parsedStickersMap = new HashMap<>();

            Pattern splitStickersPattern = Pattern.compile("[,./;:\\s_\n]+");
            Pattern splitQuantityPattern = Pattern.compile("[()]");

            String[] stickersWithQuantity = splitStickersPattern.split(stickerString);

            if(stickersWithQuantity.length == 0) return parsedStickersList;

            Map<String, StickerVO> availableMap = new HashMap<>();
            for(StickerVO stickerVO : availableList) {
                availableMap.put(stickerVO.getNumber(), stickerVO);
            }

            for(String stickerWithQuantity : stickersWithQuantity) {
                String stickerNumber, quantity;

                String[] splitStickerQuantity = splitQuantityPattern.split(stickerWithQuantity);
                if(splitStickerQuantity.length == 1 || TextUtils.isEmpty(splitStickerQuantity[0])) {
                    //Количества в скобках нет
                    stickerNumber = stickerWithQuantity;
                    quantity = "1";
                } else {
                    //Есть количество в скобках
                    stickerNumber = splitStickerQuantity[0];
                    quantity = splitStickerQuantity[1];
                }

                StickerVO stickerVO = availableMap.get(stickerNumber);
                if(stickerVO == null) {
                    //Не найден стикер в каталоге, наверно введен кривой номер
                    StickerVO parsedSticker = new StickerVO(stickerNumber, "???");
                    parsedStickersMap.put(stickerNumber, parsedSticker);
                } else {
                    //Найден стикер в каталоге
                    StickerVO parsedSticker = parsedStickersMap.get(stickerVO.getNumber());
                    if(parsedSticker == null) {
                        //Еще не добавляли
                        parsedSticker = new StickerVO(stickerVO);
                        parsedSticker.incQuantityVal(Short.valueOf(quantity));
                        parsedStickersMap.put(parsedSticker.getNumber(), parsedSticker);
                    } else {
                        //Уже добавляли, просто увеличим количество
                        parsedSticker.incQuantity();
                    }
                }
            }

            for(Map.Entry<String, StickerVO> entry : parsedStickersMap.entrySet()) {
                parsedStickersList.add(entry.getValue());
            }

            Collections.sort(parsedStickersList, (a,b) -> (int)(a.getStickerId() - b.getStickerId()));

            return parsedStickersList;
        });
    }

    @Override
    public Single<TransactionVO> deactivateTransaction(CollectionVO collectionVO, List<StickerVO> stickersVO, TransactionVO transactionVO) {
        return Single.fromCallable(() -> {

            Transaction transaction = transactionVO.getTransaction();
            Boolean deactivating = transaction.getActive();

            LongSparseArray<StickerVO> savedStickers = new LongSparseArray<>();
            for (StickerVO stickerVO : stickersVO) {
                if(stickerVO.getDepSticker() != null)
                    savedStickers.put(stickerVO.getId(), stickerVO);
            }

            List<TransactionRow> transactionRowList = dbHelper.selectTransactionRowList(transaction.getId());
            for (TransactionRow transactionRow : transactionRowList) {
                StickerVO stickerVO = savedStickers.get(transactionRow.getStickerId());
                if (stickerVO != null) {
                    if (deactivating)
                        stickerVO.decQuantityVal(transactionRow.getQuantity());
                    else
                        stickerVO.incQuantityVal(transactionRow.getQuantity());
                }
            }

            saveCollectionNow(collectionVO, stickersVO, transaction);

            transaction.setActive(!deactivating);
            dbHelper.updateTransaction(transaction);

            transactionVO.setActive(transaction.getActive()); //Синхронизация VO с DB
            return transactionVO;
        });
    }
}
