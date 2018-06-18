package ru.av3969.stickerscollector.ui.editcoll;

import java.util.List;

import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.ui.vo.CollectionVO;
import ru.av3969.stickerscollector.ui.vo.StickerVO;

public interface EditCollectionContract {

    interface View {
        void updateCollectionHead(CollectionVO collectionVO);
        void updateStickersList(List<StickerVO> stickers);
        void showLoading();
        void hideLoading();
        void collectionSaved();
    }

    interface Presenter {
        void setView(View view);
        void loadCollectionHead(Long parentCollection, Long collectionId);
        void loadStickersList(Long parentCollection, Long collectionId);
        void saveCollection();
        void onDestroy();
    }
}
