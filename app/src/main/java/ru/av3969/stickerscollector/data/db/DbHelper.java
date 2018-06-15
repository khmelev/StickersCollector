package ru.av3969.stickerscollector.data.db;

import java.util.List;

import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.data.db.entity.CatalogStickers;
import ru.av3969.stickerscollector.data.db.entity.DepositoryCollection;
import ru.av3969.stickerscollector.data.db.entity.DepositoryStickers;

public interface DbHelper {

    // Category

    CatalogCategory selectCategory(Long id);

    List<CatalogCategory> selectCategoryList(Long parentId);

    void inflateCategories(List<CatalogCategory> categoryList);

    void updateCategories(List<CatalogCategory> categoryList);

    // CatalogCollection

    CatalogCollection selectCatalogCollection(Long id);

    List<CatalogCollection> selectCatalogCollectionList(Long categoryId);

    void inflateCollections(List<CatalogCollection> collectionList);

    // CatalogStickers

    List<CatalogStickers> selectCatalogStickersList(Long ownerId);

    void inflateStickers(List<CatalogStickers> stickersList);

    // DepositoryCollection

    DepositoryCollection selectDepositoryCollection(Long id);

    // DepositoryStickers

    List<DepositoryStickers> selectDepositoryStickersList(Long ownerId);
}
