package ru.av3969.stickerscollector.ui.editcoll;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import ru.av3969.stickerscollector.data.DataManager;
import ru.av3969.stickerscollector.data.db.entity.CatalogStickers;
import ru.av3969.stickerscollector.ui.base.BasePresenter;
import ru.av3969.stickerscollector.ui.vo.CollectionVO;
import ru.av3969.stickerscollector.ui.vo.StickerVO;
import ru.av3969.stickerscollector.utils.SchedulerProvider;

public class EditCollectionPresenter extends BasePresenter implements EditCollectionContract.Presenter {

    EditCollectionContract.View view;

    private DataManager dataManager;
    private SchedulerProvider schedulerProvider;
    private CompositeDisposable compositeDisposable;

    @Inject
    public EditCollectionPresenter(DataManager dataManager, SchedulerProvider schedulerProvider,
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
                Single.zip(
                    dataManager.loadCatalogCollection(parentCollection),
                    dataManager.loadDepositoryCollection(collectionId),
                        CollectionVO::new)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(collectionVO -> {
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
                    view.updateStickersList(stickers);
                    view.hideLoading();
                })
        );
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        view = null;
    }
}
