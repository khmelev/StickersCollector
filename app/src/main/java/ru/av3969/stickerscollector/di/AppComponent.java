package ru.av3969.stickerscollector.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.av3969.stickerscollector.di.module.ApplicationModule;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface AppComponent {


}
