package ru.av3969.stickerscollector.di.component;

import javax.inject.Singleton;

import dagger.Component;
import ru.av3969.stickerscollector.data.DataManager;
import ru.av3969.stickerscollector.di.module.ApplicationModule;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface AppComponent {

    DataManager getDataManager();

}
