package ru.av3969.stickerscollector.ui.addcoll;

import android.support.annotation.StringRes;

import java.util.List;

import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;

public interface CollectionListContract {
    interface View {
        void updateCollectionList(List<CatalogCollection> collectionList);
        void showLoading();
        void hideLoading();
        void showError(@StringRes int resId);
        void showError(String errorMsg);
    }
    interface Presenter {
        void setView(View view);
        void loadCollectionList(Long categoryId);
        void onDestroy();
    }
}
