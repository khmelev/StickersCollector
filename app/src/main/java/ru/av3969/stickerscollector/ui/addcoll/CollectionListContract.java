package ru.av3969.stickerscollector.ui.addcoll;

public interface CollectionListContract {
    interface View {
        void updateCollectionList();
        void showLoading();
        void hideLoading();
    }
    interface Presenter {
        void setView(View view);
        void loadCollectionList(Long parentCat);
        void onDestroy();
    }
}
