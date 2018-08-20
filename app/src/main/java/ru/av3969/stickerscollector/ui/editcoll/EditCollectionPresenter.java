package ru.av3969.stickerscollector.ui.editcoll;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
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
                    dataManager.saveCollection(collectionVO, stickersVO)
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
                            view.showOutlayStickers(stickers);
                        })
        );
    }

    @Override
    public void commitIncomeStickers() {
        if(collectionVO != null && collectionVO.isSaved()) {
            List<StickerVO> stickersToSave = new ArrayList<>();
            for (StickerVO incomeSticker : incomeStickersVO) {
                StickerVO stickerVO = incomeSticker.getLinkedSticker();
                if (stickerVO != null) {
                    stickerVO.incQuantityVal(incomeSticker.getQuantity());
                    stickersToSave.add(stickerVO);
                }
            }
            if(!stickersToSave.isEmpty())
                compositeDisposable.add(
                        dataManager.commitDepositoryTransaction(collectionVO, stickersToSave, "Приход")
                                .subscribeOn(schedulerProvider.io())
                                .observeOn(schedulerProvider.ui())
                                .subscribe(() -> view.transactionSaved())
                );
        } else {
            view.showError("Коллекция должна быть сохранена!");
        }
    }

    @Override
    public void commitOutlayStickers() {
        if(collectionVO != null && collectionVO.isSaved()) {
            List<StickerVO> stickersToSave = new ArrayList<>();
            for (StickerVO outlaySticker : outlayStickersVO) {
                StickerVO stickerVO = outlaySticker.getLinkedSticker();
                if (stickerVO != null) {
                    stickerVO.decQuantityVal(outlaySticker.getQuantity());
                    stickersToSave.add(stickerVO);
                }
            }
            if(!stickersToSave.isEmpty())
                compositeDisposable.add(
                        dataManager.commitDepositoryTransaction(collectionVO, stickersToSave, "Расход")
                                .subscribeOn(schedulerProvider.io())
                                .observeOn(schedulerProvider.ui())
                                .subscribe(() -> view.transactionSaved())
                );
        } else {
            view.showError("Коллекция должна быть сохранена!");
        }
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        view = null;
        collectionVO = null;
        stickersVO = null;
    }
}
