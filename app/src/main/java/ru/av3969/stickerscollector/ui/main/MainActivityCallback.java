package ru.av3969.stickerscollector.ui.main;

import android.support.annotation.StringRes;

public interface MainActivityCallback {

    void startEditCollectionActivity(Long parentCollectionId, Long collectionId);
    String getStringFromRes(@StringRes int resId);
    void updateAbTitle(CharSequence title);

}
