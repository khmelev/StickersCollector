package ru.av3969.stickerscollector.ui.editcoll;

import java.util.List;

import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.data.db.entity.Transaction;
import ru.av3969.stickerscollector.ui.vo.CollectionVO;
import ru.av3969.stickerscollector.ui.vo.StickerVO;

public interface EditCollectionContract {

    interface View {
        void updateCollectionHead(CollectionVO collectionVO);
        void updateStickersList(List<StickerVO> stickers);
        void updateTransactionList(List<Transaction> transactionList);
        void showLoading();
        void hideLoading();
        void collectionSaved();
        void transactionSaved();
        void showIncomeStickers(List<StickerVO> stickers);
        void showOutlayStickers(List<StickerVO> stickers);
        void showError(CharSequence msg);
    }

    interface Presenter {
        void setView(View view);
        void loadCollectionHead(Long parentCollection, Long collectionId);
        void loadStickersList(Long parentCollection, Long collectionId);
        void loadTransactionList(Long collectionId);
        void saveCollection();
        void parseIncomeStickers(CharSequence stickerString);
        void parseOutlayStickers(CharSequence stickerString);
        void commitIncomeStickers();
        void commitOutlayStickers();
        void onDestroy();
    }
}
