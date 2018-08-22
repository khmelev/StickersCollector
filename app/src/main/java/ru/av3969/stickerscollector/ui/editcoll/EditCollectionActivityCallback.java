package ru.av3969.stickerscollector.ui.editcoll;

public interface EditCollectionActivityCallback {
    void parseIncomeStickers(CharSequence stickerString);
    void parseOutlayStickers(CharSequence stickerString);
    void commitIncomeStickers();
    void commitOutlayStickers();
    void loadStickersList();
    void loadTransactionList();
}
