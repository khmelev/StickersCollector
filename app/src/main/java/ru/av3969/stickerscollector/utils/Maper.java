package ru.av3969.stickerscollector.utils;

import ru.av3969.stickerscollector.data.db.entity.DepositoryCollection;
import ru.av3969.stickerscollector.data.db.entity.DepositoryStickers;
import ru.av3969.stickerscollector.ui.vo.CollectionVO;
import ru.av3969.stickerscollector.ui.vo.StickerVO;

public class Maper {

    public static DepositoryCollection toDepositoryCollection(CollectionVO collectionVO) {
        return new DepositoryCollection(collectionVO.getId(),
                collectionVO.getCollectionId(),
                collectionVO.getTitle(),
                0L,
                collectionVO.getUnique(),
                collectionVO.getQuantity());
    }

    public static DepositoryStickers toDepositoryStickers(StickerVO stickerVO) {
        return new DepositoryStickers(stickerVO.getId(),
                stickerVO.getOwnerId(),
                stickerVO.getStickerId(),
                stickerVO.getQuantity());
    }
}
