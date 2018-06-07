package ru.av3969.stickerscollector.ui.addcoll;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.av3969.stickerscollector.data.DataManager;
import ru.av3969.stickerscollector.ui.base.BasePresenter;
import ru.av3969.stickerscollector.utils.SchedulerProvider;

public class CategoryListPresenter extends BasePresenter implements CategoryListContract.Presenter {

    private CategoryListContract.View view;

    private DataManager dataManager;
    private SchedulerProvider schedulerProvider;
    private CompositeDisposable compositeDisposable;

    @Inject
    CategoryListPresenter(DataManager dataManager, SchedulerProvider schedulerProvider,
                                 CompositeDisposable compositeDisposable) {
        this.dataManager = dataManager;
        this.schedulerProvider = schedulerProvider;
        this.compositeDisposable = compositeDisposable;
    }

    @Override
    public void setView(CategoryListContract.View view) {
        this.view = view;
    }

    @Override
    public void loadCategoryList() {

        compositeDisposable.add(
                dataManager.loadCategoryList()
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(catalogCategories ->
                            view.updateCategoryList(catalogCategories)
                        ));

    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        view = null;
    }
}
