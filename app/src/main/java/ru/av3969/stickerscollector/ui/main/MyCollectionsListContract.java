package ru.av3969.stickerscollector.ui.main;

import java.util.List;

import ru.av3969.stickerscollector.ui.vo.CollectionVO;

public interface MyCollectionsListContract {

    interface View {
        void updateCollectionsList(List<CollectionVO> collections);
    }

    interface Presenter {
        void setView(View view);
        void loadCollectionsList(boolean forceLoad);
        void onDestroy();
    }

}
