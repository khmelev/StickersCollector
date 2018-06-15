package ru.av3969.stickerscollector.ui.addcoll;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.av3969.stickerscollector.data.DataManager;
import ru.av3969.stickerscollector.ui.base.BasePresenter;
import ru.av3969.stickerscollector.utils.SchedulerProvider;

public class CollectionListPresenter extends BasePresenter implements CollectionListContract.Presenter {

    private CollectionListContract.View view;

    private DataManager dataManager;
    private SchedulerProvider schedulerProvider;
    private CompositeDisposable compositeDisposable;

    @Inject
    CollectionListPresenter(DataManager dataManager, SchedulerProvider schedulerProvider,
                                   CompositeDisposable compositeDisposable) {
        this.dataManager = dataManager;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void setView(CollectionListContract.View view) {
        this.view = view;
    }

    @Override
    public void loadCollectionList(Long categoryId) {
        view.showLoading();
        compositeDisposable.add(
                dataManager.loadCatalogCollectionList(categoryId)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(collectionList -> {
                        view.updateCollectionList(collectionList);
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
