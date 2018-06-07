package ru.av3969.stickerscollector.data;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.av3969.stickerscollector.data.db.AppDbHelper;
import ru.av3969.stickerscollector.data.db.DbHelper;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.data.pref.PreferencesHelper;

public class AppDataManager implements DataManager {

    private PreferencesHelper preferencesHelper;
    private DbHelper dbHelper;

    @Inject
    public AppDataManager(PreferencesHelper preferencesHelper, DbHelper dbHelper) {
        this.preferencesHelper = preferencesHelper;
        this.dbHelper = dbHelper;
    }

    @Override
    public Single<List<CatalogCategory>> loadCategoryList() {
         return Single.fromCallable(() -> {
             List<CatalogCategory> testList = new ArrayList<>();
             testList.add(new CatalogCategory(Long.valueOf(1), "test1", "Футбол", Long.valueOf(0)));
             testList.add(new CatalogCategory(Long.valueOf(2), "test2", "Хокей", Long.valueOf(0)));
             testList.add(new CatalogCategory(Long.valueOf(3), "test3", "Танцы", Long.valueOf(0)));
             testList.add(new CatalogCategory(Long.valueOf(4), "test4", "Шрек", Long.valueOf(0)));

             List<CatalogCategory> listFromDB = dbHelper.selectCatalogCategoryList();

             return listFromDB.isEmpty() ? testList : listFromDB;
         });
    }
}
