package ru.av3969.stickerscollector.ui.editcoll;

import android.text.TextUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.DataManager;
import ru.av3969.stickerscollector.data.db.entity.Transaction;
import ru.av3969.stickerscollector.ui.base.BasePresenter;
import ru.av3969.stickerscollector.ui.vo.CollectionVO;
import ru.av3969.stickerscollector.ui.vo.StickerVO;
import ru.av3969.stickerscollector.utils.SchedulerProvider;

public class EditCollectionPresenter extends BasePresenter implements EditCollectionContract.Presenter {

    private EditCollectionContract.View view;

    private CollectionVO collectionVO;
    private List<StickerVO> stickersVO;
    private List<StickerVO> incomeStickersVO;
    private List<StickerVO> outlayStickersVO;

    private DataManager dataManager;
    private SchedulerProvider schedulerProvider;
    private CompositeDisposable compositeDisposable;

    @Inject
    EditCollectionPresenter(DataManager dataManager, SchedulerProvider schedulerProvider,
                                   CompositeDisposable compositeDisposable) {
        this.dataManager = dataManager;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void setView(EditCollectionContract.View view) {
        this.view = view;
    }

    @Override
    public void loadCollectionHead(Long parentCollection, Long collectionId) {
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
    public void loadStickersList(Long parentCollection, Long collectionId) {
        if (stickersVO != null) {
            view.updateStickersList(stickersVO);
            return;
        }
        view.showLoading();
        compositeDisposable.add(
                dataManager.loadStickerVOList(parentCollection, collectionId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(stickers -> {
                    this.stickersVO = stickers;
                    view.updateStickersList(stickers);
                    view.hideLoading();
                })
        );
    }

    @Override
    public void saveCollection() {
        if (collectionVO != null && stickersVO != null) {
            view.showLoading();
            compositeDisposable.add(
                    dataManager.saveCollection(collectionVO, stickersVO, new Transaction(view.getStringFromRes(R.string.manual_correction)))
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .subscribe(() -> view.collectionSaved())
            );
        }
    }

    @Override
    public void loadTransactionList(Long collectionId) {
        compositeDisposable.add(
                dataManager.loadTransactionList(collectionId)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(transactions -> {
                            view.updateTransactionList(transactions);
                        })
        );
    }

    @Override
    public void loadTransactionRowList(Transaction transaction) {
        compositeDisposable.add(
                dataManager.loadTransactionRowList(transaction)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(stickers -> view.showTransactionRow(stickers))
        );
    }

    @Override
    public void commitTransactionRow(List<StickerVO> stickers) {
        compositeDisposable.add(
                dataManager.commitTransactionRowList(stickers)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(() -> view.showMsg(view.getStringFromRes(R.string.saved)))
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
                            this.outlayStickersVO = stickers;
                            if(!stickers.isEmpty())
                                view.showOutlayStickers(stickers);
                        })
        );
    }

    @Override
    public void commitIncomeStickers(CharSequence transTitle) {
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
                        .subscribe(() -> view.transactionSaved())
        );
    }

    @Override
    public void commitOutlayStickers(CharSequence transTitle) {
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
                        .subscribe(() -> view.transactionSaved())
        );
    }

    @Override
    public void deactivateTransaction(Transaction transaction) {
        compositeDisposable.add(
                dataManager.deactivateTransaction(collectionVO, stickersVO, transaction)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe((t) -> {
                        view.showMsg(t.getActive()
                                ? view.getStringFromRes(R.string.transaction_activated)
                                : view.getStringFromRes(R.string.transaction_deactivated)
                        );
                    })
        );
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        view = null;
        collectionVO = null;
        stickersVO = null;
        incomeStickersVO = null;
        outlayStickersVO = null;
    }
}
