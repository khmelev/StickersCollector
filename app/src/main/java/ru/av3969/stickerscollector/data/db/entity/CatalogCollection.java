package ru.av3969.stickerscollector.data.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class CatalogCollection {

    @Id
    private Long id;

    @Unique
    private String name;

    private String title;

    private Long categoryId;

    @ToOne(joinProperty = "categoryId")
    private CatalogCategory category;

    private Short year;

    private Byte stype;

    @Transient
    public static Byte stickerType = 1;

    private Short size;

    private String desc;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1640355786)
    private transient CatalogCollectionDao myDao;

    @Generated(hash = 1527351255)
    public CatalogCollection(Long id, String name, String title, Long categoryId,
            Short year, Byte stype, Short size, String desc) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.categoryId = categoryId;
        this.year = year;
        this.stype = stype;
        this.size = size;
        this.desc = desc;
    }

    @Generated(hash = 507910761)
    public CatalogCollection() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Short getYear() {
        return this.year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public Byte getStype() {
        return this.stype;
    }

    public void setStype(Byte stype) {
        this.stype = stype;
    }

    public Short getSize() {
        return this.size;
    }

    public void setSize(Short size) {
        this.size = size;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Generated(hash = 1372501278)
    private transient Long category__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2130816813)
    public CatalogCategory getCategory() {
        Long __key = this.categoryId;
        if (category__resolvedKey == null || !category__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CatalogCategoryDao targetDao = daoSession.getCatalogCategoryDao();
            CatalogCategory categoryNew = targetDao.load(__key);
            synchronized (this) {
                category = categoryNew;
                category__resolvedKey = __key;
            }
        }
        return category;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1865812700)
    public void setCategory(CatalogCategory category) {
        synchronized (this) {
            this.category = category;
            categoryId = category == null ? null : category.getId();
            category__resolvedKey = categoryId;
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
    @Generated(hash = 389015406)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCatalogCollectionDao() : null;
    }
}
