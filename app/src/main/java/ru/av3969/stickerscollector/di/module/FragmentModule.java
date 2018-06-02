package ru.av3969.stickerscollector.di.module;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import ru.av3969.stickerscollector.di.PerFragment;
import ru.av3969.stickerscollector.ui.main.CollectionsListContract;
import ru.av3969.stickerscollector.ui.main.CollectionsListPresenter;

@Module
public class FragmentModule {

    @Provides
    @PerFragment
    CollectionsListContract.Presenter provideCollectionsListContractPresenter(CollectionsListPresenter collectionsListPresenter) {
        return collectionsListPresenter;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }
}
