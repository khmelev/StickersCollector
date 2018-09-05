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
import ru.av3969.stickerscollector.ui.vo.TransactionVO;

public interface EditCollectionContract {

    interface View {
        void updateCollectionHead(CollectionVO collectionVO);
        void updateStickersList(List<StickerVO> stickers);
        void updateTransactionList(List<TransactionVO> transactionList);
        void showLoading();
        void hideLoading();
        void collectionSaved();
        void transactionSaved();
        void showIncomeStickers(List<StickerVO> stickers);
        void showOutlayStickers(List<StickerVO> stickers, String comment);
        void showMsg(CharSequence msg);
        void showTransactionRow(List<StickerVO> stickers);
        void showAvailableStickersAsText(CharSequence text);
        String getStringFromRes(@StringRes int resId);
    }

    interface Presenter {
        void setView(View view);
        void loadCollectionHead(Long parentCollection, Long collectionId);
        void loadStickersList(Long parentCollection, Long collectionId);
        void loadTransactionList(Long collectionId);
        void loadTransactionRowList(TransactionVO transaction);
        void saveTransactionRows();
        void saveCollection();
        void parseIncomeStickers(CharSequence stickerString);
        void parseOutlayStickers(CharSequence stickerString);
        void commitIncomeStickers(CharSequence transTitle);
        void commitOutlayStickers(CharSequence transTitle);
        void deactivateTransaction(TransactionVO transaction);
        void assembleStickersAsText();
        void onDestroy();
    }
}
