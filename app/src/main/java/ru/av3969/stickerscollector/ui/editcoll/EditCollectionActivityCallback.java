package ru.av3969.stickerscollector.ui.editcoll;

import java.util.List;

import ru.av3969.stickerscollector.data.db.entity.Transaction;
import ru.av3969.stickerscollector.ui.vo.StickerVO;

public interface EditCollectionActivityCallback {
    void parseIncomeStickers(CharSequence stickerString);
    void parseOutlayStickers(CharSequence stickerString);
    void commitIncomeStickers(CharSequence transTitle);
    void commitOutlayStickers(CharSequence transTitle);
    void loadStickersList();
    void loadTransactionList();
    void deactivateTransaction(Transaction transaction);
    void loadTransactionRow(Transaction transaction);
    void commitTransactionRow(List<StickerVO> stickersVO);
}
