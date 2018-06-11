package ru.av3969.stickerscollector.di.component;

import dagger.Component;
import ru.av3969.stickerscollector.di.PerFragment;
import ru.av3969.stickerscollector.di.module.FragmentModule;
import ru.av3969.stickerscollector.ui.addcoll.AddMyCollectionFragment;
import ru.av3969.stickerscollector.ui.addcoll.CategoryListFragment;
import ru.av3969.stickerscollector.ui.addcoll.CollectionListFragment;
import ru.av3969.stickerscollector.ui.main.MyCollectionsListFragment;

@PerFragment
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(MyCollectionsListFragment fragment);

    void inject(CategoryListFragment fragment);

    void inject(AddMyCollectionFragment fragment);

    void inject(CollectionListFragment collectionListFragment);
}
