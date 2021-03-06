package ru.av3969.stickerscollector.data.db;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategoryDao;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollectionDao;
import ru.av3969.stickerscollector.data.db.entity.CatalogStickers;
import ru.av3969.stickerscollector.data.db.entity.CatalogStickersDao;
import ru.av3969.stickerscollector.data.db.entity.DaoMaster;
import ru.av3969.stickerscollector.data.db.entity.DaoSession;
import ru.av3969.stickerscollector.data.db.entity.DepositoryCollection;
import ru.av3969.stickerscollector.data.db.entity.DepositoryCollectionDao;
import ru.av3969.stickerscollector.data.db.entity.DepositoryStickers;
import ru.av3969.stickerscollector.data.db.entity.DepositoryStickersDao;
import ru.av3969.stickerscollector.data.db.entity.Transaction;
import ru.av3969.stickerscollector.data.db.entity.TransactionDao;
import ru.av3969.stickerscollector.data.db.entity.TransactionRow;
import ru.av3969.stickerscollector.data.db.entity.TransactionRowDao;

public class AppDbHelper implements DbHelper {

    private final DaoSession mDaoSession;

    @Inject
    AppDbHelper(DbOpenHelper dbOpenHelper) {
        mDaoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }

    // Category

    @Override
    public CatalogCategory selectCategory(Long id) {
        return mDaoSession.getCatalogCategoryDao().load(id);
    }

    @Override
    public List<CatalogCategory> selectCategoryList(Long parentId) {
        return mDaoSession.getCatalogCategoryDao().queryBuilder()
                .where(CatalogCategoryDao.Properties.ParentId.eq(parentId))
                .list();
    }

    @Override
    public void inflateCategories(List<CatalogCategory> categoryList) {
        mDaoSession.getCatalogCategoryDao().deleteAll();
        for (CatalogCategory category : categoryList) {
            mDaoSession.getCatalogCategoryDao().insert(category);
        }
    }

    @Override
    public void updateCategories(List<CatalogCategory> categoryList) {
        for (CatalogCategory category : categoryList) {
            mDaoSession.getCatalogCategoryDao().insertOrReplace(category);
        }
    }

    // CatalogCollection

    @Override
    public CatalogCollection selectCatalogCollection(Long id) {
        return mDaoSession.getCatalogCollectionDao().load(id);
    }

    @Override
    public List<CatalogCollection> selectCatalogCollectionList(Long categoryId) {
        return mDaoSession.getCatalogCollectionDao().queryBuilder()
                .where(CatalogCollectionDao.Properties.CategoryId.eq(categoryId))
                .orderDesc(CatalogCollectionDao.Properties.Year, CatalogCollectionDao.Properties.Id)
                .list();
    }

    @Override
    public void inflateCollections(List<CatalogCollection> collectionList) {
        for (CatalogCollection collection : collectionList) {
            mDaoSession.getCatalogCollectionDao().insertOrReplace(collection);
        }
    }

    // CatalogStickers

    @Override
    public List<CatalogStickers> selectCatalogStickersList(Long ownerId) {
        return mDaoSession.getCatalogStickersDao().queryBuilder()
                .where(CatalogStickersDao.Properties.OwnerId.eq(ownerId))
                .list();
    }

    @Override
    public void inflateStickers(List<CatalogStickers> stickersList) {
        for (CatalogStickers sticker : stickersList) {
            mDaoSession.getCatalogStickersDao().insert(sticker);
        }
    }

    // DepositoryCollection

    @Override
    public DepositoryCollection selectDepositoryCollection(Long id) {
        return mDaoSession.getDepositoryCollectionDao().load(id);
    }

    @Override
    public List<DepositoryCollection> selectDepositoryCollectionList() {
        return mDaoSession.getDepositoryCollectionDao().queryBuilder()
                .orderDesc(DepositoryCollectionDao.Properties.Order)
                .list();
    }

    @Override
    public long insertDepositoryCollection(DepositoryCollection collection) {
        return mDaoSession.getDepositoryCollectionDao().insertOrReplace(collection);
    }

    @Override
    public void dropDepositoryCollection(Long id) {
        mDaoSession.getDepositoryCollectionDao().deleteByKey(id);
    }

    // DepositoryStickers

    @Override
    public List<DepositoryStickers> selectDepositoryStickersList(Long ownerId) {
        return mDaoSession.getDepositoryStickersDao().queryBuilder()
                .where(DepositoryStickersDao.Properties.OwnerId.eq(ownerId))
                .list();
    }

    @Override
    public void dropDepositoryStickersList(List<Long> idList) {
        mDaoSession.getDepositoryStickersDao().deleteByKeyInTx(idList);
    }

    @Override
    public void insertDepositoryStickersList(List<DepositoryStickers> stickersList) {
        for(DepositoryStickers sticker : stickersList) {
            mDaoSession.getDepositoryStickersDao().insertOrReplace(sticker);
        }
    }

    // Transaction

    @Override
    public Transaction selectTransaction(Long id) {
        return mDaoSession.getTransactionDao().load(id);
    }

    @Override
    public List<Transaction> selectTransactionList(Long collectionId) {
        return collectionId==null ? new ArrayList<>() : mDaoSession.getTransactionDao().queryBuilder()
                .where(TransactionDao.Properties.CollectionId.eq(collectionId))
                .orderDesc(TransactionDao.Properties.Id)
                .list();
    }

    @Override
    public void insertTransaction(Transaction transaction) {
        mDaoSession.getTransactionDao().insert(transaction);
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        mDaoSession.getTransactionDao().update(transaction);
    }

    @Override
    public void dropTransaction(Long transId) {
        mDaoSession.getTransactionDao().deleteByKey(transId);
    }

    // Transaction Row

    @Override
    public List<TransactionRow> selectTransactionRowList(Long ownerId) {
        List<TransactionRow> transactionRowList = mDaoSession.getTransactionRowDao().queryBuilder()
                .where(TransactionRowDao.Properties.OwnerId.eq(ownerId))
                .list();
        for (TransactionRow transactionRow : transactionRowList) {
            transactionRow.getSticker().getSticker(); //Что бы загрузились связанные объекты
        }
        return transactionRowList;
    }

    @Override
    public void updateTransactionRowList(List<TransactionRow> transactionRowList) {
        for(TransactionRow transactionRow : transactionRowList) {
            mDaoSession.getTransactionRowDao().insertOrReplace(transactionRow);
        }
    }

    @Override
    public void dropTransactionRowList(List<Long> transactionIdList) {
        mDaoSession.getTransactionRowDao().deleteByKeyInTx(transactionIdList);
    }
}
