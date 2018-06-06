package ru.av3969.stickerscollector.di.module;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import ru.av3969.stickerscollector.di.PerFragment;
import ru.av3969.stickerscollector.ui.addcoll.CategoryListContract;
import ru.av3969.stickerscollector.ui.addcoll.CategoryListPresenter;
import ru.av3969.stickerscollector.ui.main.CollectionsListContract;
import ru.av3969.stickerscollector.ui.main.CollectionsListPresenter;
import ru.av3969.stickerscollector.utils.AppSchedulerProvider;
import ru.av3969.stickerscollector.utils.SchedulerProvider;

@Module
public class FragmentModule {

    @Provides
    @PerFragment
    CollectionsListContract.Presenter provideCollectionsListContractPresenter(
            CollectionsListPresenter collectionsListPresenter) {
        return collectionsListPresenter;
    }

    @Provides
    @PerFragment
    CategoryListContract.Presenter provideCategoryListContractPresenter(
            CategoryListPresenter categoryListPresenter) {
        return categoryListPresenter;
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
