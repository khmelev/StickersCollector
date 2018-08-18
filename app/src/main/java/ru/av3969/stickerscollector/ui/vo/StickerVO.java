package ru.av3969.stickerscollector.ui.vo;

import ru.av3969.stickerscollector.data.db.entity.CatalogStickers;
import ru.av3969.stickerscollector.data.db.entity.DepositoryStickers;

public class StickerVO {

    private Long id;
    private Long ownerId;
    private Long stickerId;
    private String number;
    private String name;
    private String section;
    private String type;
    private Short quantity;
    private Short startQuantity;
    private StickerVO linkedSticker;

    public StickerVO(CatalogStickers catSticker, DepositoryStickers depSticker) {
        this.id = depSticker.getId();
        this.ownerId = depSticker.getOwnerId();
        this.stickerId = depSticker.getStickerId();
        this.number = catSticker.getNumber();
        this.name = catSticker.getName();
        this.section = catSticker.getSection();
        this.type = catSticker.getType();
        this.quantity = depSticker.getQuantity();
        this.startQuantity = this.quantity;
    }

    public StickerVO(CatalogStickers catSticker) {
        this.stickerId = catSticker.getId();
        this.number = catSticker.getNumber();
        this.name = catSticker.getName();
        this.section = catSticker.getSection();
        this.type = catSticker.getType();
        this.quantity = 0;
        this.startQuantity = this.quantity;
    }

    public StickerVO(StickerVO stickerVO) {
        this.id = stickerVO.getId();
        this.stickerId = stickerVO.getStickerId();
        this.number = stickerVO.getNumber();
        this.name = stickerVO.getName();
        this.section = stickerVO.getSection();
        this.type = stickerVO.getType();
        this.quantity = 0;
        this.startQuantity = this.quantity;
        this.linkedSticker = stickerVO;
    }

    public StickerVO(String number, String name) {
        this.stickerId = 0L;
        this.number = number;
        this.name = name;
        this.quantity = 0;
        this.startQuantity = this.quantity;
    }

    public void incQuantity() {
        this.quantity++;
    }

    public void decQuantity() {
        this.quantity--;
    }

    public Long getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getStickerId() {
        return stickerId;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getSection() {
        return section;
    }

    public String getType() {
        return type;
    }

    public Short getQuantity() {
        return quantity;
    }

    public Short getStartQuantity() {
        return startQuantity;
    }

    public StickerVO getLinkedSticker() {
        return linkedSticker;
    }
}
