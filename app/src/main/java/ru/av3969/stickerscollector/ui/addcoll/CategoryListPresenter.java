package ru.av3969.stickerscollector.ui.addcoll;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.av3969.stickerscollector.data.DataManager;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.ui.base.BasePresenter;
import ru.av3969.stickerscollector.utils.SchedulerProvider;

public class CategoryListPresenter extends BasePresenter implements CategoryListContract.Presenter {

    private CategoryListContract.View view;

    private DataManager dataManager;
    private SchedulerProvider schedulerProvider;
    private CompositeDisposable compositeDisposable;

    @Inject
    public CategoryListPresenter(DataManager dataManager, SchedulerProvider schedulerProvider,
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
        List<CatalogCategory> testList = new ArrayList<>();
        testList.add(new CatalogCategory(Long.valueOf(1), "test1", "Футбол", Long.valueOf(0)));
        testList.add(new CatalogCategory(Long.valueOf(2), "test2", "Хокей", Long.valueOf(0)));
        testList.add(new CatalogCategory(Long.valueOf(3), "test3", "Танцы", Long.valueOf(0)));
        testList.add(new CatalogCategory(Long.valueOf(4), "test4", "Шрек", Long.valueOf(0)));

        view.updateCategoryList(testList);
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
