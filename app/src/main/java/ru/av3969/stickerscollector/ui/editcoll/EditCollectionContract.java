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
        void showLoading(int page);
        void hideLoading();
        void collectionSaved();
        void transactionSaved();
        void showIncomeStickers(List<StickerVO> stickers);
        void showOutlayStickers(List<StickerVO> stickers, String comment);
        void showMsg(CharSequence msg);
        void showTransactionRow(List<StickerVO> stickers, TransactionVO transaction);
        void showAvailableStickersAsText(CharSequence text);
        void showAlertDialog(String title, String message);
        void showError(@StringRes int resId);
        void showError(String errorMsg);
        String getStringFromRes(@StringRes int resId);
    }

    interface Presenter {
        Long getCollectionId();
        Long getParentCollection();
        void setView(View view);
        void loadCollectionHead(Long parentCollection, Long collectionId);
        void loadStickersList(Boolean forceLoad);
        void loadTransactionList(Boolean forceLoad);
        void loadTransactionRowList(TransactionVO transaction);
        void saveTransactionRows(CharSequence transTitle);
        void saveCollection();
        void parseIncomeStickers(CharSequence stickerString);
        void parseOutlayStickers(CharSequence stickerString);
        void commitIncomeStickers(CharSequence transTitle);
        void commitOutlayStickers(CharSequence transTitle);
        void deactivateTransaction(TransactionVO transaction);
        void assembleStickersVOAsText();
        void onDestroy();
    }
}
