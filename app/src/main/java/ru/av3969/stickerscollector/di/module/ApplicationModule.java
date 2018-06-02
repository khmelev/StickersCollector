package ru.av3969.stickerscollector.di.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.av3969.stickerscollector.data.AppDataManager;
import ru.av3969.stickerscollector.data.DataManager;
import ru.av3969.stickerscollector.data.pref.AppPreferencesHelper;
import ru.av3969.stickerscollector.data.pref.PreferencesHelper;

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    Context provideContext() { return application; }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(Context context) {
        return new AppPreferencesHelper(context, "payments_pref.db");
    }

    @Provides
    @Singleton
    DataManager provideDataManager(PreferencesHelper preferencesHelper) {
        return new AppDataManager(preferencesHelper);
    }
}
