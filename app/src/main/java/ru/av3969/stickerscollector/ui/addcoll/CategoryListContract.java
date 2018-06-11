package ru.av3969.stickerscollector.ui.addcoll;

import java.util.List;

import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;

public interface CategoryListContract {

    interface View {
        void updateCategoryList(List<CatalogCategory> catalogCategoryList);
        void showLoading();
        void hideLoading();
    }

    interface Presenter {
        void setView(View view);
        void loadCategoryList(Long parentId);
        void onDestroy();
    }
}
