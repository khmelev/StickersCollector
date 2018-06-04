package ru.av3969.stickerscollector.data.pref;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import ru.av3969.stickerscollector.di.PreferenceInfo;

public class AppPreferencesHelper implements PreferencesHelper {

    private final SharedPreferences prefs;

    @Inject
    public AppPreferencesHelper(Context context, @PreferenceInfo String dbName) {
        prefs = context.getSharedPreferences(dbName, context.MODE_PRIVATE);
    }

}
