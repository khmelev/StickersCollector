package ru.av3969.stickerscollector.ui.addcoll;

import android.support.annotation.StringRes;

public interface AddCollectionActivityCallback {

    void setActionBarTitle(@StringRes int resId);

    void showCategoryList(Long filterCat);

    void showCollectionList(Long parentCat);

    void showMyCollectionAdd(Long parentColl);

    void showLoading();

    void hideLoading();
}
