package ru.av3969.stickerscollector.data;

import javax.inject.Inject;

import ru.av3969.stickerscollector.data.db.AppDbHelper;
import ru.av3969.stickerscollector.data.db.DbHelper;
import ru.av3969.stickerscollector.data.pref.PreferencesHelper;

public class AppDataManager implements DataManager {

    private PreferencesHelper preferencesHelper;
    private DbHelper dbHelper;

    @Inject
    public AppDataManager(PreferencesHelper preferencesHelper, DbHelper dbHelper) {
        this.preferencesHelper = preferencesHelper;
        this.dbHelper = dbHelper;
    }
}
