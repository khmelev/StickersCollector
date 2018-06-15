package ru.av3969.stickerscollector.data.db;

import java.util.List;

import javax.inject.Inject;

import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategoryDao;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollectionDao;
import ru.av3969.stickerscollector.data.db.entity.CatalogStickers;
import ru.av3969.stickerscollector.data.db.entity.CatalogStickersDao;
import ru.av3969.stickerscollector.data.db.entity.DaoMaster;
import ru.av3969.stickerscollector.data.db.entity.DaoSession;
import ru.av3969.stickerscollector.data.db.entity.DepositoryCollection;
import ru.av3969.stickerscollector.data.db.entity.DepositoryStickers;
import ru.av3969.stickerscollector.data.db.entity.DepositoryStickersDao;

public class AppDbHelper implements DbHelper {

    private final DaoSession mDaoSession;

    @Inject
    AppDbHelper(DbOpenHelper dbOpenHelper) {
        mDaoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }

    // Category

    @Override
    public CatalogCategory selectCategory(Long id) {
        return mDaoSession.getCatalogCategoryDao().load(id);
    }

    @Override
    public List<CatalogCategory> selectCategoryList(Long parentId) {
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

    // CatalogCollection

    @Override
    public CatalogCollection selectCatalogCollection(Long id) {
        return mDaoSession.getCatalogCollectionDao().load(id);
    }

    @Override
    public List<CatalogCollection> selectCatalogCollectionList(Long categoryId) {
        return mDaoSession.getCatalogCollectionDao().queryBuilder()
                .where(CatalogCollectionDao.Properties.CategoryId.eq(categoryId))
                .orderDesc(CatalogCollectionDao.Properties.Year, CatalogCollectionDao.Properties.Id)
                .list();
    }

    @Override
    public void inflateCollections(List<CatalogCollection> collectionList) {
        for (CatalogCollection collection : collectionList) {
            mDaoSession.getCatalogCollectionDao().insertOrReplace(collection);
        }
    }

    // CatalogStickers

    @Override
    public List<CatalogStickers> selectCatalogStickersList(Long ownerId) {
        return mDaoSession.getCatalogStickersDao().queryBuilder()
                .where(CatalogStickersDao.Properties.OwnerId.eq(ownerId))
                .list();
    }

    @Override
    public void inflateStickers(List<CatalogStickers> stickersList) {
        for (CatalogStickers sticker : stickersList) {
            mDaoSession.getCatalogStickersDao().insert(sticker);
        }
    }

    // DepositoryCollection

    @Override
    public DepositoryCollection selectDepositoryCollection(Long id) {
        return mDaoSession.getDepositoryCollectionDao().load(id);
    }

    // DepositoryStickers

    @Override
    public List<DepositoryStickers> selectDepositoryStickersList(Long ownerId) {
        return mDaoSession.getDepositoryStickersDao().queryBuilder()
                .where(DepositoryStickersDao.Properties.OwnerId.eq(ownerId))
                .list();
    }
}
