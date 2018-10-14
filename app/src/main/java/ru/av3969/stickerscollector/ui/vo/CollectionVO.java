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
    private Long order;
    private Long startOrder;
    private String collectionCoverUrl;
    private String collectionSmallCoverUrl;

    public CollectionVO(Long id, Long collectionId) {
        this.id = id;
        this.collectionId = collectionId;
        this.title = "";
        this.year = 0;
        this.stype = 0;
        this.size = 0;
        this.desc = "";
        this.unique = 0;
        this.quantity = 0;
    }

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
        this.order = depCollection.getOrder();
        this.startOrder = this.order;
    }

    public CollectionVO(CatalogCollection catCollection) {
        this.id = 0L;
        this.collectionId = catCollection.getId();
        this.title = catCollection.getTitle();
        this.year = catCollection.getYear();
        this.stype = catCollection.getStype();
        this.size = catCollection.getSize();
        this.desc = catCollection.getDesc();
        this.unique = 0;
        this.quantity = 0;
        this.order = this.id;
        this.startOrder = this.order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setOrder(Long order) {
        this.order = order;
    }

    public void setStartOrder(Long startOrder) {
        this.startOrder = startOrder;
    }

    public Long getOrder() {
        return order;
    }

    public Long getStartOrder() {
        return startOrder;
    }

    public Boolean isNew() { return id == null || id == 0L; }

    public void setCollectionCoverUrl(String collectionCoverUrl) {
        this.collectionCoverUrl = collectionCoverUrl;
    }

    public void setCollectionSmallCoverUrl(String collectionSmallCoverUrl) {
        this.collectionSmallCoverUrl = collectionSmallCoverUrl;
    }

    public String getCollectionCoverUrl() {
        return collectionCoverUrl;
    }

    public String getCollectionSmallCoverUrl() {
        return collectionSmallCoverUrl;
    }
}
