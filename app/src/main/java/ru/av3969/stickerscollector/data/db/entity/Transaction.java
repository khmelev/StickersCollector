package ru.av3969.stickerscollector.data.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Transient;

@Entity
public class Transaction {

    @Id(autoincrement = true)
    private Long id;

    private Date date;

    private Long collectionId;

    @ToOne(joinProperty = "collectionId")
    private DepositoryCollection collection;

    private String title;

    private Integer quantity;

    private Boolean active;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 947191939)
    private transient TransactionDao myDao;

    @Generated(hash = 2044882668)
    public Transaction(Long id, Date date, Long collectionId, String title, Integer quantity,
            Boolean active) {
        this.id = id;
        this.date = date;
        this.collectionId = collectionId;
        this.title = title;
        this.quantity = quantity;
        this.active = active;
    }

    @Generated(hash = 750986268)
    public Transaction() {
    }

    public Transaction(String title) {
        this.title = title;
    }

    public Boolean isNew() {
        return id == null || id == 0L;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Generated(hash = 524422847)
    private transient Long collection__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1592041248)
    public DepositoryCollection getCollection() {
        Long __key = this.collectionId;
        if (collection__resolvedKey == null
                || !collection__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DepositoryCollectionDao targetDao = daoSession
                    .getDepositoryCollectionDao();
            DepositoryCollection collectionNew = targetDao.load(__key);
            synchronized (this) {
                collection = collectionNew;
                collection__resolvedKey = __key;
            }
        }
        return collection;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1455885450)
    public void setCollection(DepositoryCollection collection) {
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 511087935)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTransactionDao() : null;
    }
}
