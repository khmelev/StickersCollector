package ru.av3969.stickerscollector.di.module;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import ru.av3969.stickerscollector.di.PerFragment;
import ru.av3969.stickerscollector.ui.addcoll.CategoryListContract;
import ru.av3969.stickerscollector.ui.addcoll.CategoryListPresenter;
import ru.av3969.stickerscollector.ui.addcoll.CollectionListContract;
import ru.av3969.stickerscollector.ui.addcoll.CollectionListPresenter;
import ru.av3969.stickerscollector.ui.main.MyCollectionsListContract;
import ru.av3969.stickerscollector.ui.main.MyCollectionsListPresenter;
import ru.av3969.stickerscollector.utils.AppSchedulerProvider;
import ru.av3969.stickerscollector.utils.SchedulerProvider;

@Module
public class FragmentModule {

    @Provides
    @PerFragment
    MyCollectionsListContract.Presenter provideCollectionsListContractPresenter(
            MyCollectionsListPresenter myCollectionsListPresenter) {
        return myCollectionsListPresenter;
    }

    @Provides
    @PerFragment
    CategoryListContract.Presenter provideCategoryListContractPresenter(
            CategoryListPresenter categoryListPresenter) {
        return categoryListPresenter;
    }

    @Provides
    @PerFragment
    CollectionListContract.Presenter provideCollectionListContractPresenter(
            CollectionListPresenter collectionListPresenter) {
        return collectionListPresenter;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }
}
