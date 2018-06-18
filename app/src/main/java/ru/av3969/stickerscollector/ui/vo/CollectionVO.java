package ru.av3969.stickerscollector.ui.vo;

import android.text.TextUtils;

import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.data.db.entity.DepositoryCollection;

public class CollectionVO {

    private Long id;
    private Long collectionId;
    private String title;
    private Short year;
    private Byte stype;
    private Short size;
    private String desc;
    private Short unique;
    private Integer quantity;

    public CollectionVO(CatalogCollection catCollection, DepositoryCollection depCollection) {
        this.id = depCollection.getId();
        this.collectionId = catCollection.getId();
        this.title = TextUtils.isEmpty(depCollection.getTitle()) ? catCollection.getTitle() : depCollection.getTitle();
        this.year = catCollection.getYear();
        this.stype = catCollection.getStype();
        this.size = catCollection.getSize();
        this.desc = catCollection.getDesc();
        this.unique = depCollection.getUnique();
        this.quantity = depCollection.getQuantity();
    }

    public CollectionVO(CatalogCollection catCollection) {
        this.collectionId = catCollection.getId();
        this.title = catCollection.getTitle();
        this.year = catCollection.getYear();
        this.stype = catCollection.getStype();
        this.size = catCollection.getSize();
        this.desc = catCollection.getDesc();
        this.unique = 0;
        this.quantity = 0;
    }

    public Long getId() {
        return id;
    }

    public Long getCollectionId() {
        return collectionId;
    }

    public String getTitle() {
        return title;
    }

    public Short getYear() {
        return year;
    }

    public Byte getStype() {
        return stype;
    }

    public Short getSize() {
        return size;
    }

    public String getDesc() {
        return desc;
    }

    public Short getUnique() {
        return unique;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
