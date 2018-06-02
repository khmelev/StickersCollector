package ru.av3969.stickerscollector.ui.main;

public interface CollectionsListContract {

    interface View {
        void updateCollectionsList();
    }

    interface Presenter {
        void setView(View view);
        void loadCollectionsList();
        void onDestroy();
    }

}
