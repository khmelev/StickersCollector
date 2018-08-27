package ru.av3969.stickerscollector.ui.editcoll;

import ru.av3969.stickerscollector.data.db.entity.Transaction;

public interface EditCollectionActivityCallback {
    void parseIncomeStickers(CharSequence stickerString);
    void parseOutlayStickers(CharSequence stickerString);
    void commitIncomeStickers();
    void commitOutlayStickers();
    void loadStickersList();
    void loadTransactionList();
    void deactivateTransaction(Transaction transaction);
}
