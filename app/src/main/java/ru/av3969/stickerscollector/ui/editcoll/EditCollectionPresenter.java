package ru.av3969.stickerscollector.ui.editcoll;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.DataManager;
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
                    dataManager.saveCollection(collectionVO, stickersVO, view.getStringFromRes(R.string.manual_correction))
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
    public void commitIncomeStickers() {
        for (StickerVO incomeSticker : incomeStickersVO) {
            StickerVO stickerVO = incomeSticker.getLinkedSticker();
            if (stickerVO != null) {
                stickerVO.incQuantityVal(incomeSticker.getQuantity());
            }
        }
        compositeDisposable.add(
                dataManager.saveCollection(collectionVO, stickersVO, view.getStringFromRes(R.string.income))
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(() -> view.transactionSaved())
        );
    }

    @Override
    public void commitOutlayStickers() {
        for (StickerVO outlaySticker : outlayStickersVO) {
            StickerVO stickerVO = outlaySticker.getLinkedSticker();
            if (stickerVO != null) {
                stickerVO.decQuantityVal(outlaySticker.getQuantity());
            }
        }
        compositeDisposable.add(
                dataManager.saveCollection(collectionVO, stickersVO, view.getStringFromRes(R.string.outlay))
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(() -> view.transactionSaved())
        );
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        view = null;
        collectionVO = null;
        stickersVO = null;
    }
}
