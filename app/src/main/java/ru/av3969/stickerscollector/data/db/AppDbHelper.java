package ru.av3969.stickerscollector.data.db;

import java.util.List;

import javax.inject.Inject;

import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.data.db.entity.DaoMaster;
import ru.av3969.stickerscollector.data.db.entity.DaoSession;

public class AppDbHelper implements DbHelper {

    private final DaoSession mDaoSession;

    @Inject
    AppDbHelper(DbOpenHelper dbOpenHelper) {
        mDaoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }

    @Override
    public Long insertCatalogCategory(CatalogCategory catalogCategory) {
        return mDaoSession.getCatalogCategoryDao().insertOrReplace(catalogCategory);
    }

    @Override
    public List<CatalogCategory> selectCatalogCategoryList() {
        return mDaoSession.getCatalogCategoryDao().loadAll();
    }
}
