package ru.av3969.stickerscollector.data.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferencesHelper implements PreferencesHelper {

    private final SharedPreferences prefs;

    public AppPreferencesHelper(Context context, String dbName) {
        prefs = context.getSharedPreferences(dbName, context.MODE_PRIVATE);
    }

}
