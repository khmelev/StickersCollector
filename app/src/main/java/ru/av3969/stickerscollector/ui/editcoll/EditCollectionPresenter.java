package ru.av3969.stickerscollector.ui.editcoll;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.DataManager;
import ru.av3969.stickerscollector.data.db.entity.Transaction;
import ru.av3969.stickerscollector.ui.base.BasePresenter;
import ru.av3969.stickerscollector.ui.vo.CollectionVO;
import ru.av3969.stickerscollector.ui.vo.StickerVO;
import ru.av3969.stickerscollector.ui.vo.TransactionVO;
import ru.av3969.stickerscollector.utils.NegativeBalanceException;
import ru.av3969.stickerscollector.utils.NoInternetException;
import ru.av3969.stickerscollector.utils.SchedulerProvider;

public class EditCollectionPresenter extends BasePresenter implements EditCollectionContract.Presenter {

    private EditCollectionContract.View view;

    private CollectionVO collectionVO;
    private List<StickerVO> stickersVO;
    private List<StickerVO> incomeStickersVO;
    private List<StickerVO> outlayStickersVO;
    private List<TransactionVO> transactionsVO;
    private TransactionVO currTransactionVO;
    private List<StickerVO> currTransactionStickersVO;

    private DataManager dataManager;
    private SchedulerProvider schedulerProvider;
    private CompositeDisposable compositeDisposable;

    private ObservableEmitter<TransactionVO> transDeactivateEmitter;

    @Inject
    EditCollectionPresenter(DataManager dataManager, SchedulerProvider schedulerProvider,
                                   CompositeDisposable compositeDisposable) {
        this.dataManager = dataManager;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public Long getCollectionId() {
        return collectionVO.getId();
    }

    @Override
    public Long getParentCollection() {
        return collectionVO.getCollectionId();
    }

    @Override
    public void setView(EditCollectionContract.View view) {
        this.view = view;

        initTransactionProcessor();
    }

    @Override
    public String getCollectionCoverUrl() {
        return collectionVO.getCollectionCoverUrl();
    }

    public void initTransactionProcessor() {
        compositeDisposable.add(
                Observable.create(new ObservableOnSubscribe<TransactionVO>() {
                    @Override
                    public void subscribe(ObservableEmitter<TransactionVO> emitter) throws Exception {
                        transDeactivateEmitter = emitter;
                    }
                }).subscribe(transaction -> {
                    compositeDisposable.add(
                            dataManager.deactivateTransaction(collectionVO, stickersVO, transaction)
                                    .subscribeOn(schedulerProvider.io())
                                    .observeOn(schedulerProvider.ui())
                                    .subscribe((t, e) -> {
                                        if(t != null) {
                                            view.showMsg(t.getActive()
                                                    ? view.getStringFromRes(R.string.transaction_activated)
                                                    : view.getStringFromRes(R.string.transaction_deactivated)
                                            );
                                        } else if(e != null && e instanceof NegativeBalanceException) {
                                            view.showAlertDialog(view.getStringFromRes(R.string.negative_balance), ((NegativeBalanceException) e).getStickersAsString());
                                            loadStickersList(true);
                                            loadTransactionList(true);
                                        }
                                    })
                    );
                })
        );
    }

    @Override
    public void loadCollectionHead(Long parentCollection, Long collectionId) {
        //Создадим простой объект, что бы параллельно загружались стикеры, позже он обновится
        collectionVO = new CollectionVO(collectionId, parentCollection);

        compositeDisposable.add(
                dataManager.loadCollectionVO(parentCollection, collectionId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(collectionVO -> {
                    this.collectionVO = collectionVO;
                    view.updateCollectionHead(collectionVO);
                }));
    }

    @Override
    public void loadStickersList(Boolean forceLoad) {
        if (stickersVO != null && !stickersVO.isEmpty() && !forceLoad) {
            view.updateStickersList(stickersVO);
            return;
        }
        view.showLoading(0);
        compositeDisposable.add(
                dataManager.loadStickerVOList(getParentCollection(), getCollectionId())
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(stickers -> {
                        this.stickersVO = stickers;
                        view.updateStickersList(stickers);
                        view.hideLoading();
                    }, e -> {
                        view.hideLoading();
                        if(e instanceof NoInternetException)
                            view.showError(R.string.error_no_internet_connection);
                        else
                            view.showError(e.getCause().toString());
                    }
                )
        );
    }

    @Override
    public void saveCollection() {
        if (collectionVO != null && stickersVO != null) {
            view.showLoading(0);
            compositeDisposable.add(
                    dataManager.saveCollection(collectionVO, stickersVO, new Transaction(view.getStringFromRes(R.string.manual_correction)))
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .subscribe(() -> view.collectionSaved())
            );
        }
    }

    @Override
    public void loadTransactionList(Boolean forceLoad) {
        if (!forceLoad && transactionsVO != null && !transactionsVO.isEmpty()) {
            view.updateTransactionList(transactionsVO);
            return;
        }
        view.showLoading(3);
        compositeDisposable.add(
                dataManager.loadTransactionList(getCollectionId())
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(transactions -> {
                            this.transactionsVO = transactions;
                            view.updateTransactionList(transactions);
                            view.hideLoading();
                        })
        );
    }

    @Override
    public void loadTransactionRowList(TransactionVO transaction) {
        this.currTransactionVO = transaction;
        compositeDisposable.add(
                dataManager.loadTransactionRowList(transaction, stickersVO)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(stickers -> {
                            this.currTransactionStickersVO = stickers;
                            view.showTransactionRow(stickers, transaction);
                        })
        );
    }

    @Override
    public void saveTransactionRows(CharSequence transTitle) {
        currTransactionVO.setTitle(transTitle.toString());
        compositeDisposable.add(
                dataManager.saveTransactionRowList(collectionVO, stickersVO, currTransactionVO, currTransactionStickersVO)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(() -> {
                            view.showMsg(view.getStringFromRes(R.string.saved));
                            loadTransactionList(true);
                        })
        );
    }

    @Override
    public void parseIncomeStickers(CharSequence stickerString) {
        compositeDisposable.add(
                dataManager.parseStickers(stickerString, stickersVO)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(stickers -> {
                        this.incomeStickersVO = stickers;
                        if(!stickers.isEmpty())
                            view.showIncomeStickers(stickers);
                    })
        );
    }

    @Override
    public void parseOutlayStickers(CharSequence stickerString) {
        compositeDisposable.add(
                dataManager.parseStickers(stickerString, stickersVO)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(stickers -> {
                            if(stickers.isEmpty()) return;

                            this.outlayStickersVO = stickers;
                            Map<String, Integer> quantityByTypeMap = new HashMap<>();

                            //Перебираем разобранный список стикеров, находим тех что не хватает
                            List<StickerVO> notEnoughStickers = new ArrayList<>();
                            for (StickerVO sticker : stickers) {
                                StickerVO linkedSticker = sticker.getLinkedSticker();
                                if(linkedSticker == null) {
                                    //Не распознанные стикеры добавляем сразу
                                    StickerVO notEnoughSticker = new StickerVO(sticker);
                                    notEnoughSticker.incQuantity();
                                    notEnoughStickers.add(notEnoughSticker);
                                } else {
                                    if (linkedSticker.getQuantity() < sticker.getQuantity()) {
                                        //Если у нас в наличии меньше чем в расходе
                                        short delta = (short) (sticker.getQuantity() - linkedSticker.getQuantity());
                                        StickerVO notEnoughSticker = new StickerVO(sticker);
                                        notEnoughSticker.setQuantity(delta);
                                        notEnoughStickers.add(notEnoughSticker);

                                        //Установим максимально доступное количество
                                        sticker.setQuantity(linkedSticker.getQuantity());
                                        sticker.setStartQuantity(linkedSticker.getQuantity());
                                    }
                                    if(sticker.getQuantity() > 0) {
                                        //Подсчет доступного количеста расхода по типам наклеек
                                        Integer quantityByType = quantityByTypeMap.get(linkedSticker.getType());
                                        if(quantityByType == null) {
                                            quantityByTypeMap.put(linkedSticker.getType(), sticker.getQuantity().intValue());
                                        } else {
                                            quantityByTypeMap.put(linkedSticker.getType(), quantityByType + sticker.getQuantity().intValue());
                                        }
                                    }
                                }
                            }
                            //Вывод тех что есть
                            StringBuilder comment = new StringBuilder();
                            comment.append(view.getStringFromRes(R.string.there_is)).append(": ");
                            Observable.fromIterable(stickers).map(StickerVO::getQuantity).map(Short::intValue).reduce((a, b) -> a + b).subscribe(comment::append);
                            comment.append("\n").append(assembleStickersAsText(stickers)).append("\n").append("\n");

                            //Расклад по типам
                            if(quantityByTypeMap.size() > 0) {
                                compositeDisposable.add(Observable.fromIterable(quantityByTypeMap.entrySet()).sorted((a,b) -> a.getKey().length() - b.getKey().length()).subscribe(set -> {comment.append(set.getKey()).append(": ").append(set.getValue()).append("\n");}));
                                comment.append("\n");
                            }

                            //Вывод тех что нет
                            if(notEnoughStickers.size() > 0) {
                                comment.append(view.getStringFromRes(R.string.there_is_not)).append(": ");
                                compositeDisposable.add(Observable.fromIterable(notEnoughStickers).map(StickerVO::getQuantity).map(Short::intValue).reduce((a, b) -> a + b).subscribe(comment::append));
                                comment.append("\n").append(assembleStickersAsText(notEnoughStickers));
                            }
                            view.showOutlayStickers(stickers, comment.toString());
                        })
        );
    }

    @Override
    public void commitIncomeStickers(CharSequence transTitle) {
        view.showLoading(1);

        for (StickerVO incomeSticker : incomeStickersVO) {
            StickerVO stickerVO = incomeSticker.getLinkedSticker();
            if (stickerVO != null) {
                stickerVO.incQuantityVal(incomeSticker.getQuantity());
            }
        }
        compositeDisposable.add(
                dataManager.saveCollection(collectionVO, stickersVO, new Transaction(TextUtils.isEmpty(transTitle) ? view.getStringFromRes(R.string.income) : transTitle.toString()))
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(() -> {
                            view.transactionSaved();
                            view.hideLoading();
                        })
        );
    }

    @Override
    public void commitOutlayStickers(CharSequence transTitle) {
        view.showLoading(2);

        for (StickerVO outlaySticker : outlayStickersVO) {
            StickerVO stickerVO = outlaySticker.getLinkedSticker();
            if (stickerVO != null) {
                stickerVO.decQuantityVal(outlaySticker.getQuantity());
            }
        }
        compositeDisposable.add(
                dataManager.saveCollection(collectionVO, stickersVO, new Transaction(TextUtils.isEmpty(transTitle) ? view.getStringFromRes(R.string.outlay) : transTitle.toString()))
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(() -> {
                            view.transactionSaved();
                            view.hideLoading();
                        })
        );
    }

    @Override
    public void deactivateTransaction(TransactionVO transaction) {
        /*compositeDisposable.add(
                dataManager.deactivateTransaction(collectionVO, stickersVO, transaction)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe((t, e) -> {
                        if(t != null) {
                            view.showMsg(t.getActive()
                                    ? view.getStringFromRes(R.string.transaction_activated)
                                    : view.getStringFromRes(R.string.transaction_deactivated)
                            );
                        } else if(e != null && e instanceof NegativeBalanceException) {
                            view.showAlertDialog(view.getStringFromRes(R.string.negative_balance), ((NegativeBalanceException) e).getStickersAsString());
                            loadStickersList(true);
                            loadTransactionList(true);
                        }
                    })
        );*/
        if (transDeactivateEmitter != null) {
            transDeactivateEmitter.onNext(transaction);
        }
    }

    @Override
    public void assembleStickersVOAsText() {

        view.showAvailableStickersAsText(assembleStickersAsText(stickersVO));
    }

    private String assembleStickersAsText(List<StickerVO> stickerList) {
        StringBuilder stickersAsText = new StringBuilder();
        for (StickerVO stickerVO : stickerList) {
            if(stickerVO.getQuantity() > 0) {
                if(stickersAsText.length() > 0)
                    stickersAsText.append(", ");
                stickersAsText.append(stickerVO.getNumber());
                if(stickerVO.getQuantity() > 1)
                    stickersAsText.append("(").append(stickerVO.getQuantity()).append(")");
            }
        }
        return stickersAsText.toString();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        view = null;
    }
}
