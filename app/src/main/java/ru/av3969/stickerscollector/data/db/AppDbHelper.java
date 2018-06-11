package ru.av3969.stickerscollector.data.db;

import java.util.List;

import javax.inject.Inject;

import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategoryDao;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollectionDao;
import ru.av3969.stickerscollector.data.db.entity.DaoMaster;
import ru.av3969.stickerscollector.data.db.entity.DaoSession;

public class AppDbHelper implements DbHelper {

    private final DaoSession mDaoSession;

    @Inject
    AppDbHelper(DbOpenHelper dbOpenHelper) {
        mDaoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }

    @Override
    public CatalogCategory selectCatalogCategory(Long id) {
        return mDaoSession.getCatalogCategoryDao().load(id);
    }

    @Override
    public List<CatalogCategory> selectCatalogCategoryList(Long parentId) {
        return mDaoSession.getCatalogCategoryDao().queryBuilder()
                .where(CatalogCategoryDao.Properties.ParentId.eq(parentId))
                .list();
    }

    @Override
    public void inflateCategories(List<CatalogCategory> categoryList) {
        mDaoSession.getCatalogCategoryDao().deleteAll();
        for (CatalogCategory category : categoryList) {
            mDaoSession.getCatalogCategoryDao().insert(category);
        }
    }

    @Override
    public void updateCategories(List<CatalogCategory> categoryList) {
        for (CatalogCategory category : categoryList) {
            mDaoSession.getCatalogCategoryDao().insertOrReplace(category);
        }
    }

    @Override
    public List<CatalogCollection> selectCatalogCollectionList(Long categoryId) {
        return mDaoSession.getCatalogCollectionDao().queryBuilder()
                .where(CatalogCollectionDao.Properties.CategoryId.eq(categoryId))
                .orderDesc(CatalogCollectionDao.Properties.Id)
                .list();
    }

    @Override
    public void inflateCollections(List<CatalogCollection> collectionList) {
        for (CatalogCollection collection : collectionList) {
            mDaoSession.getCatalogCollectionDao().insertOrReplace(collection);
        }
    }
}
