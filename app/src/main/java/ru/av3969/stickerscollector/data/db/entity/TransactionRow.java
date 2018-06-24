package ru.av3969.stickerscollector.data.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class TransactionRow {

    @Id(autoincrement = true)
    private Long id;

    private Long ownerId;

    @ToOne(joinProperty = "ownerId")
    private Transaction owner;

    private Long stickerId;

    @ToOne(joinProperty = "stickerId")
    private DepositoryStickers sticker;

    private Short quantity;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 912734114)
    private transient TransactionRowDao myDao;

    @Generated(hash = 1091940337)
    public TransactionRow(Long id, Long ownerId, Long stickerId, Short quantity) {
        this.id = id;
        this.ownerId = ownerId;
        this.stickerId = stickerId;
        this.quantity = quantity;
    }

    @Generated(hash = 676068591)
    public TransactionRow() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getStickerId() {
        return this.stickerId;
    }

    public void setStickerId(Long stickerId) {
        this.stickerId = stickerId;
    }

    public Short getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Short quantity) {
        this.quantity = quantity;
    }

    @Generated(hash = 1847295403)
    private transient Long owner__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 203199029)
    public Transaction getOwner() {
        Long __key = this.ownerId;
        if (owner__resolvedKey == null || !owner__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TransactionDao targetDao = daoSession.getTransactionDao();
            Transaction ownerNew = targetDao.load(__key);
            synchronized (this) {
                owner = ownerNew;
                owner__resolvedKey = __key;
            }
        }
        return owner;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 4924165)
    public void setOwner(Transaction owner) {
        synchronized (this) {
            this.owner = owner;
            ownerId = owner == null ? null : owner.getId();
            owner__resolvedKey = ownerId;
        }
    }

    @Generated(hash = 1899151975)
    private transient Long sticker__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 592341739)
    public DepositoryStickers getSticker() {
        Long __key = this.stickerId;
        if (sticker__resolvedKey == null || !sticker__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DepositoryStickersDao targetDao = daoSession.getDepositoryStickersDao();
            DepositoryStickers stickerNew = targetDao.load(__key);
            synchronized (this) {
                sticker = stickerNew;
                sticker__resolvedKey = __key;
            }
        }
        return sticker;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 452199643)
    public void setSticker(DepositoryStickers sticker) {
        synchronized (this) {
            this.sticker = sticker;
            stickerId = sticker == null ? null : sticker.getId();
            sticker__resolvedKey = stickerId;
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
    @Generated(hash = 123230210)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTransactionRowDao() : null;
    }
}
