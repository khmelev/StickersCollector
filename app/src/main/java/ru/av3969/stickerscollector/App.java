package ru.av3969.stickerscollector;

import android.app.Application;

import es.dmoral.toasty.Toasty;
import ru.av3969.stickerscollector.di.component.AppComponent;
import ru.av3969.stickerscollector.di.component.DaggerAppComponent;
import ru.av3969.stickerscollector.di.module.ApplicationModule;

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Toasty.Config.getInstance().setSuccessColor(getResources().getColor(R.color.colorPrimaryDark)).apply();

        appComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
