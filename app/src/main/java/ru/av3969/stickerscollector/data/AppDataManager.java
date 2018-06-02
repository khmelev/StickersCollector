package ru.av3969.stickerscollector.data;

import ru.av3969.stickerscollector.data.pref.PreferencesHelper;

public class AppDataManager implements DataManager {

    private PreferencesHelper preferencesHelper;

    public AppDataManager(PreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
    }
}
