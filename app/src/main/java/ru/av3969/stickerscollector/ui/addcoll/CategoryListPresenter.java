package ru.av3969.stickerscollector.ui.addcoll;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.av3969.stickerscollector.R;
import ru.av3969.stickerscollector.data.DataManager;
import ru.av3969.stickerscollector.ui.base.BasePresenter;
import ru.av3969.stickerscollector.utils.NoInternetException;
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
    public void loadCategoryList(Long parentId) {
        view.showLoading();
        compositeDisposable.add(
                dataManager.loadCategoryList(parentId)
                        .subscribeOn(schedulerProvider.io())
                        .observeOn(schedulerProvider.ui())
                        .subscribe(catalogCategories -> {
                                view.updateCategoryList(catalogCategories);
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
    public void onDestroy() {
        compositeDisposable.clear();
        view = null;
    }
}
