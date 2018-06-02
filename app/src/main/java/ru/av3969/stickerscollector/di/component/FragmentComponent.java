package ru.av3969.stickerscollector.di.component;

import dagger.Component;
import ru.av3969.stickerscollector.di.PerFragment;
import ru.av3969.stickerscollector.di.module.FragmentModule;
import ru.av3969.stickerscollector.ui.main.CollectionsListFragment;

@PerFragment
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(CollectionsListFragment fragment);

}
