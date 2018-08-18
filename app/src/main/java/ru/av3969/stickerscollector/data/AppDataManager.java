package ru.av3969.stickerscollector.data;

import android.annotation.SuppressLint;

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
import ru.av3969.stickerscollector.utils.Maper;

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
    public Single<List<StickerVO>> loadStickerVOList(Long parentCollection, Long collectionId) {
        return Single.fromCallable(() -> {
            List<StickerVO> stickers = new ArrayList<>();
            @SuppressLint("UseSparseArrays")
            Map<Long,DepositoryStickers> depositoryStickersMap = new HashMap<>();

            List<CatalogStickers> catalogStickers = dbHelper.selectCatalogStickersList(parentCollection);
            if(catalogStickers.isEmpty()) {
                CatalogCollection catalogCollection = dbHelper.selectCatalogCollection(parentCollection);
                dbHelper.inflateStickers(laststickerHelper.getStickersList(catalogCollection.getName(), parentCollection));
                catalogStickers = dbHelper.selectCatalogStickersList(parentCollection);
            }
            List<DepositoryStickers> depositoryStickers = dbHelper.selectDepositoryStickersList(collectionId);
            if(!depositoryStickers.isEmpty()) {
                for (DepositoryStickers depSticker : depositoryStickers) {
                    depositoryStickersMap.put(depSticker.getStickerId(), depSticker);
                }
            }

            for (CatalogStickers catSticker : catalogStickers) {
                if (!depositoryStickersMap.isEmpty()) {
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
    public Completable saveCollection(CollectionVO collectionVO, List<StickerVO> stickersVO) {
        return Completable.fromCallable(() -> {
            //Записываем коллекцию
            Long depCollectionId = dbHelper.insertDepositoryCollection(Maper.toDepositoryCollection(collectionVO));
            DepositoryCollection depCollection = dbHelper.selectDepositoryCollection(depCollectionId);
            //Записываем наклейки в коллекции
            Integer totalQuantity = 0;
            Short uniqueQuantity = 0;
            List<DepositoryStickers> depStickers = new ArrayList<>();
            for(StickerVO stickerVO : stickersVO) {
                if(stickerVO.getId() != null || stickerVO.getQuantity() > 0) {
                    DepositoryStickers depSticker = Maper.toDepositoryStickers(stickerVO);
                    depSticker.setOwnerId(depCollectionId);
                    depStickers.add(depSticker);
                    totalQuantity += depSticker.getQuantity();
                    if(stickerVO.getQuantity() > 0) {
                        uniqueQuantity++;
                    }
                }
            }
            dbHelper.insertDepositoryStickersList(depStickers);
            //Обновляем количество в коллекции
            depCollection.setQuantity(totalQuantity);
            depCollection.setUnique(uniqueQuantity);
            dbHelper.insertDepositoryCollection(depCollection);

            //Записываем транзакцию
            List<TransactionRow> transactionRowList = new ArrayList<>();
            int transactionQuantity = 0;
            for(StickerVO stickerVO : stickersVO) {
                short deltaQuantity = (short)(stickerVO.getQuantity() - stickerVO.getStartQuantity());
                if(deltaQuantity != 0) {
                    transactionRowList.add(new TransactionRow(null, null, null, deltaQuantity));
                    transactionQuantity += deltaQuantity;
                }
            }
            if(transactionQuantity != 0) {
                Transaction transaction = new Transaction(null,
                        new Date(),
                        Transaction.addType,
                        depCollection.getId(),
                        "Ручное изменение",
                        transactionQuantity,
                        true);
                dbHelper.insertTransaction(transaction);
                for(TransactionRow transactionRow : transactionRowList) {
                    transactionRow.setOwnerId(transaction.getId());
                }
                dbHelper.insertTransactionRowList(transactionRowList);
            }
            return true;
        });
    }

    @Override
    public Single<List<Transaction>> loadTransactionList(Long collectionId) {
        return Single.fromCallable(() -> {
            return dbHelper.selectTransactionList(collectionId);
        });
    }

    @Override
    public Completable saveTransaction(Transaction transaction) {
        return Completable.fromCallable(() -> {
            dbHelper.insertTransaction(transaction);
            return true;
        });
    }

    @Override
    public Single<List<StickerVO>> parseStickers(CharSequence stickerString, List<StickerVO> availableList) {
        return Single.fromCallable(() -> {
            List<StickerVO> parsedStickersList = new ArrayList<>();
            Map<String, StickerVO> parsedStickersMap = new HashMap<>();

            Pattern splitPattern = Pattern.compile("[,./;:\\s_\n]+");
            String[] stickerAr = splitPattern.split(stickerString);

            if(stickerAr.length == 0) return parsedStickersList;

            Map<String, StickerVO> availableMap = new HashMap<>();
            for(StickerVO stickerVO : availableList) {
                availableMap.put(stickerVO.getNumber(), stickerVO);
            }

            for(String sticker : stickerAr) {
                StickerVO stickerVO = availableMap.get(sticker);
                if(stickerVO == null) {
                    //Не найден стикер в каталоге, наверно введен кривой номер
                    StickerVO parsedSticker = new StickerVO(sticker, "???");
                    parsedStickersMap.put(sticker, parsedSticker);
                } else {
                    //Найден стикер в каталоге
                    StickerVO parsedSticker = parsedStickersMap.get(stickerVO.getNumber());
                    if(parsedSticker == null) {
                        //Еще не добавляли
                        parsedSticker = new StickerVO(stickerVO);
                        parsedSticker.incQuantity();
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
}
