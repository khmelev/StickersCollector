package ru.av3969.stickerscollector.ui.vo;

import ru.av3969.stickerscollector.data.db.entity.CatalogStickers;
import ru.av3969.stickerscollector.data.db.entity.DepositoryStickers;
import ru.av3969.stickerscollector.data.db.entity.TransactionRow;

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
    private DepositoryStickers depSticker;
    private TransactionRow transactionRow;

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
        this.depSticker = depSticker;
    }

    public StickerVO(CatalogStickers catSticker, TransactionRow transactionRow) {
        this.id = transactionRow.getId();
        this.ownerId = transactionRow.getOwnerId();
        this.stickerId = transactionRow.getStickerId();
        this.number = catSticker.getNumber();
        this.name = catSticker.getName();
        this.section = catSticker.getSection();
        this.type = catSticker.getType();
        this.quantity = transactionRow.getQuantity();
        this.startQuantity = this.quantity;
        this.transactionRow = transactionRow;
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

    //для того что бы слинковать стикеры прихода или расхода с основным списком
    public StickerVO(StickerVO stickerVO) {
        this.stickerId = stickerVO.getStickerId();
        this.number = stickerVO.getNumber();
        this.name = stickerVO.getName();
        this.section = stickerVO.getSection();
        this.type = stickerVO.getType();
        this.quantity = 0;
        this.startQuantity = this.quantity;
        this.linkedSticker = stickerVO;
    }

    //для не опознанных стикеров прихода или расхода
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

    public void incQuantityVal(Short val) {
        this.quantity = (short)(this.quantity + val);
    }

    public void decQuantity() {
        this.quantity--;
    }

    public void decQuantityVal(Short val) {
        this.quantity = (short)(this.quantity - val);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setQuantity(Short quantity) {
        this.quantity = quantity;
    }

    public Short getStartQuantity() {
        return startQuantity;
    }

    public void setStartQuantity(Short startQuantity) {
        this.startQuantity = startQuantity;
    }

    public void flushStartQuantity() {
        this.startQuantity = this.quantity;
    }

    public StickerVO getLinkedSticker() {
        return linkedSticker;
    }

    public DepositoryStickers getDepSticker() {
        return depSticker;
    }

    public void setDepSticker(DepositoryStickers depSticker) {
        this.depSticker = depSticker;
    }

    public TransactionRow getTransactionRow() {
        return transactionRow;
    }
}
