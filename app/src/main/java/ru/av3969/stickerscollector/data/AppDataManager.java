package ru.av3969.stickerscollector.data;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.av3969.stickerscollector.data.db.DbHelper;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.data.pref.PreferencesHelper;
import ru.av3969.stickerscollector.data.remote.LaststickerHelper;

public class AppDataManager implements DataManager {

    private PreferencesHelper preferencesHelper;
    private DbHelper dbHelper;
    private LaststickerHelper laststickerHelper;

    @Inject
    AppDataManager(PreferencesHelper preferencesHelper, DbHelper dbHelper,
                          LaststickerHelper laststickerHelper) {
        this.preferencesHelper = preferencesHelper;
        this.dbHelper = dbHelper;
        this.laststickerHelper = laststickerHelper;
    }

    @Override
    public Single<List<CatalogCategory>> loadCategoryList(Long parentId) {
         return Single.fromCallable(() -> {
             List<CatalogCategory> categoryList = dbHelper.selectCatalogCategoryList(parentId);
             if(categoryList.isEmpty()) {
                 List<CatalogCategory> lastStickerCategory = laststickerHelper.getCategoryList();
                 if(parentId.equals(CatalogCategory.defaultId))
                     dbHelper.inflateCategories(lastStickerCategory);
                 else
                    dbHelper.updateCategories(lastStickerCategory);

                 categoryList = dbHelper.selectCatalogCategoryList(parentId);
             }
             return categoryList;
         });
    }

    @Override
    public Single<List<CatalogCollection>> loadCollectionList(Long categoryId) {
        return Single.fromCallable(() -> {
            List<CatalogCollection> collectionList = dbHelper.selectCatalogCollectionList(categoryId);
            if(collectionList.isEmpty()) {
                CatalogCategory category = dbHelper.selectCatalogCategory(categoryId);
                if (category != null) {
                    dbHelper.inflateCollections(laststickerHelper.getCollectionList(category.getName(), categoryId));
                    collectionList = dbHelper.selectCatalogCollectionList(categoryId);
                }
            }
            return collectionList;
        });
    }
}
