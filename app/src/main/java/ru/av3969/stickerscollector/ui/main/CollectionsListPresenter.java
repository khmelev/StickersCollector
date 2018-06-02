package ru.av3969.stickerscollector.ui.main;

import javax.inject.Inject;

import ru.av3969.stickerscollector.data.DataManager;
import ru.av3969.stickerscollector.ui.base.BasePresenter;

public class CollectionsListPresenter extends BasePresenter
        implements CollectionsListContract.Presenter {

    private CollectionsListContract.View view;

    @Inject
    public CollectionsListPresenter(DataManager dataManager) {
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
