package ru.av3969.stickerscollector.data.db.entity;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import ru.av3969.stickerscollector.ui.vo.CollectionVO;

@Entity
public class DepositoryCollection {

    @Id(autoincrement = true)
    private Long id;

    private Long collectionId;

    @ToOne(joinProperty = "collectionId")
    private CatalogCollection collection;

    private String title;

    private Long holder;

    private Short unique;

    private Integer quantity;

    private Long order;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 459890095)
    private transient DepositoryCollectionDao myDao;

    @Generated(hash = 1460699417)
    public DepositoryCollection(Long id, Long collectionId, String title, Long holder,
            Short unique, Integer quantity, Long order) {
        this.id = id;
        this.collectionId = collectionId;
        this.title = title;
        this.holder = holder;
        this.unique = unique;
        this.quantity = quantity;
        this.order = order;
    }

    @Generated(hash = 1179574680)
    public DepositoryCollection() {
    }

    public DepositoryCollection(CollectionVO collectionVO) {
        this(collectionVO.getId() == 0L ? null : collectionVO.getId(),
                collectionVO.getCollectionId(),
                collectionVO.getTitle(),
                0L,
                collectionVO.getUnique(),
                collectionVO.getQuantity(),
                collectionVO.getOrder());
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCollectionId() {
        return this.collectionId;
    }

    public void setCollectionId(Long collectionId) {
        this.collectionId = collectionId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getHolder() {
        return this.holder;
    }

    public void setHolder(Long holder) {
        this.holder = holder;
    }

    public Short getUnique() {
        return this.unique;
    }

    public void setUnique(Short unique) {
        this.unique = unique;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Generated(hash = 524422847)
    private transient Long collection__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1489870964)
    public CatalogCollection getCollection() {
        Long __key = this.collectionId;
        if (collection__resolvedKey == null
                || !collection__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CatalogCollectionDao targetDao = daoSession.getCatalogCollectionDao();
            CatalogCollection collectionNew = targetDao.load(__key);
            synchronized (this) {
                collection = collectionNew;
                collection__resolvedKey = __key;
            }
        }
        return collection;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1135994741)
    public void setCollection(CatalogCollection collection) {
        synchronized (this) {
            this.collection = collection;
            collectionId = collection == null ? null : collection.getId();
            collection__resolvedKey = collectionId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public Long getOrder() {
        return this.order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 678262890)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDepositoryCollectionDao() : null;
    }
}
