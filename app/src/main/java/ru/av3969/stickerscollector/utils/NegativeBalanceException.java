package ru.av3969.stickerscollector.utils;

import java.util.List;

import ru.av3969.stickerscollector.ui.vo.StickerVO;

public class NegativeBalanceException extends Exception {

    List<StickerVO> negativeBalanceList;

    public NegativeBalanceException(List<StickerVO> negativeBalanceList) {
        super("Some stickers have negative balance");
        this.negativeBalanceList = negativeBalanceList;
    }

    public String getStickersAsString() {
        StringBuilder builder = new StringBuilder();
        if(negativeBalanceList == null) return builder.toString();
        for (StickerVO stickerVO : negativeBalanceList) {
            if(builder.length() > 0) builder.append(", ");
            builder.append(stickerVO.getNumber());
        }
        return builder.toString();
    }
}
