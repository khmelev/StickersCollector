package ru.av3969.stickerscollector.di.component;

import dagger.Component;
import ru.av3969.stickerscollector.di.PerActivity;
import ru.av3969.stickerscollector.di.module.ActivityModule;
import ru.av3969.stickerscollector.ui.editcoll.EditCollectionActivity;
import ru.av3969.stickerscollector.ui.main.MainActivity;

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(EditCollectionActivity activity);
}
