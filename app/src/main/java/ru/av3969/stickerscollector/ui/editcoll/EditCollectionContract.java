package ru.av3969.stickerscollector.ui.editcoll;

import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;

public interface EditCollectionContract {

    interface View {
        void updateCollectionHead(CatalogCollection catalogCollection);
        void updateStickersList();
        void showLoading();
        void hideLoading();
    }

    interface Presenter {
        void setView(View view);
        void loadCollectionHead(Long parentCollection, Long collectionId);
        void loadStickersList(Long parentCollection, Long collectionId);
        void onDestroy();
    }
}
