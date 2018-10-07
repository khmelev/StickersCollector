package ru.av3969.stickerscollector.ui.addcoll;

import android.support.annotation.StringRes;

import java.util.List;

import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;

public interface CategoryListContract {

    interface View {
        void updateCategoryList(List<CatalogCategory> catalogCategoryList);
        void showLoading();
        void hideLoading();
        void showError(@StringRes int resId);
        void showError(String errorMsg);
    }

    interface Presenter {
        void setView(View view);
        void loadCategoryList(Long parentId);
        void onDestroy();
    }
}
