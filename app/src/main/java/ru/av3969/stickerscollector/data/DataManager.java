package ru.av3969.stickerscollector.data;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.data.db.entity.DepositoryCollection;
import ru.av3969.stickerscollector.data.db.entity.Transaction;
import ru.av3969.stickerscollector.ui.vo.CollectionVO;
import ru.av3969.stickerscollector.ui.vo.StickerVO;
import ru.av3969.stickerscollector.ui.vo.TransactionVO;

public interface DataManager {

    Single<List<CatalogCategory>> loadCategoryList(Long parentId);

    Single<CatalogCollection> loadCatalogCollection(Long id);

    Single<List<CatalogCollection>> loadCatalogCollectionList(Long parentCat);

    Single<CollectionVO> loadCollectionVO(Long parentCollection, Long collectionId);

    Single<List<CollectionVO>> loadCollectionsVO();

    Single<List<StickerVO>> loadStickerVOList(Long parentCollection, Long collectionId);

    Single<DepositoryCollection> loadDepositoryCollection(Long id);

    Completable saveCollection(CollectionVO collectionVO, List<StickerVO> stickersVO, Transaction transaction);

    Single<List<TransactionVO>> loadTransactionList(Long collectionId);

    Single<List<StickerVO>> loadTransactionRowList(TransactionVO transaction, List<StickerVO> stickersVO);

    Completable saveTransactionRowList(CollectionVO collectionVO, List<StickerVO> stickersVO, TransactionVO transaction, List<StickerVO> tranStickersVO);

    Single<List<StickerVO>> parseStickers(CharSequence stickerString, List<StickerVO> availableList);

    Single<TransactionVO> deactivateTransaction(CollectionVO collectionVO, List<StickerVO> stickersVO, TransactionVO transaction);

    Completable commitCollectionsOrder(List<CollectionVO> collectionsVO);

    Completable destroyCollections(List<CollectionVO> collectionsForDestroy);

}
