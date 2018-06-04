package ru.av3969.stickerscollector.data.db;

import android.content.Context;

import javax.inject.Inject;

import ru.av3969.stickerscollector.data.db.entity.DaoMaster;
import ru.av3969.stickerscollector.di.DatabaseInfo;

public class DbOpenHelper extends DaoMaster.OpenHelper {

    @Inject
    public DbOpenHelper(Context context, @DatabaseInfo String name) {
        super(context, name);
    }
}
