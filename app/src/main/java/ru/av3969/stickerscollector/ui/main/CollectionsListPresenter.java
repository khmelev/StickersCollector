package ru.av3969.stickerscollector.ui.main;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.av3969.stickerscollector.data.DataManager;
import ru.av3969.stickerscollector.ui.base.BasePresenter;
import ru.av3969.stickerscollector.utils.SchedulerProvider;

public class CollectionsListPresenter extends BasePresenter
        implements CollectionsListContract.Presenter {

    private CollectionsListContract.View view;

    private DataManager dataManager;
    private SchedulerProvider schedulerProvider;
    private CompositeDisposable compositeDisposable;

    @Inject
    CollectionsListPresenter(DataManager dataManager, SchedulerProvider schedulerProvider,
                                    CompositeDisposable compositeDisposable) {
        this.dataManager = dataManager;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void setView(CollectionsListContract.View view) {
        this.view = view;
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }

    @Override
    public void loadCollectionsList() {

    }
}
