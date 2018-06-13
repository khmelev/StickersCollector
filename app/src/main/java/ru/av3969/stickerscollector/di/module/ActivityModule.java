package ru.av3969.stickerscollector.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import ru.av3969.stickerscollector.di.ActivityContext;
import ru.av3969.stickerscollector.di.PerActivity;
import ru.av3969.stickerscollector.ui.editcoll.EditCollectionContract;
import ru.av3969.stickerscollector.ui.editcoll.EditCollectionPresenter;
import ru.av3969.stickerscollector.utils.AppSchedulerProvider;
import ru.av3969.stickerscollector.utils.SchedulerProvider;

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @Provides
    @PerActivity
    EditCollectionContract.Presenter provideEditCollectionContractPresenter(
            EditCollectionPresenter editCollectionPresenter) {
        return editCollectionPresenter;
    }
}
