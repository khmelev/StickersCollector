package ru.av3969.stickerscollector.ui.editcoll;

import java.util.List;

import ru.av3969.stickerscollector.data.db.entity.Transaction;
import ru.av3969.stickerscollector.ui.vo.StickerVO;
import ru.av3969.stickerscollector.ui.vo.TransactionVO;

public interface EditCollectionActivityCallback {
    void parseIncomeStickers(CharSequence stickerString);
    void parseOutlayStickers(CharSequence stickerString);
    void commitIncomeStickers(CharSequence transTitle);
    void commitOutlayStickers(CharSequence transTitle);
    void loadStickersList();
    void loadTransactionList();
    void deactivateTransaction(TransactionVO transaction);
    void loadTransactionRow(TransactionVO transaction);
    void saveTransactionRows(CharSequence transTitle);
    void assembleStickersAsText();
    void copyTextToClipboard(CharSequence text);
    void updateFabVisibility();
}
