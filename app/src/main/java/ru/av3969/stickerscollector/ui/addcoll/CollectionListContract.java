package ru.av3969.stickerscollector.ui.addcoll;

import java.util.List;

import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;

public interface CollectionListContract {
    interface View {
        void updateCollectionList(List<CatalogCollection> collectionList);
        void showLoading();
        void hideLoading();
    }
    interface Presenter {
        void setView(View view);
        void loadCollectionList(Long categoryId);
        void onDestroy();
    }
}
