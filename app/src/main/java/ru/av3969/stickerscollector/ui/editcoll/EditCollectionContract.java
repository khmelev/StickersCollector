package ru.av3969.stickerscollector.ui.editcoll;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;

import java.util.List;

import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.data.db.entity.Transaction;
import ru.av3969.stickerscollector.data.db.entity.TransactionRow;
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
        void showMsg(CharSequence msg);
        void showTransactionRow(List<StickerVO> stickers);
        String getStringFromRes(@StringRes int resId);
    }

    interface Presenter {
        void setView(View view);
        void loadCollectionHead(Long parentCollection, Long collectionId);
        void loadStickersList(Long parentCollection, Long collectionId);
        void loadTransactionList(Long collectionId);
        void loadTransactionRowList(Transaction transaction);
        void commitTransactionRow(List<StickerVO> stickersVO);
        void saveCollection();
        void parseIncomeStickers(CharSequence stickerString);
        void parseOutlayStickers(CharSequence stickerString);
        void commitIncomeStickers(CharSequence transTitle);
        void commitOutlayStickers(CharSequence transTitle);
        void deactivateTransaction(Transaction transaction);
        void onDestroy();
    }
}
