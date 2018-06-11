package ru.av3969.stickerscollector.data.db;

import java.util.List;

import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;

public interface DbHelper {

    CatalogCategory selectCatalogCategory(Long id);

    List<CatalogCategory> selectCatalogCategoryList(Long parentId);

    void inflateCategories(List<CatalogCategory> categoryList);

    void updateCategories(List<CatalogCategory> categoryList);

    List<CatalogCollection> selectCatalogCollectionList(Long categoryId);

    void inflateCollections(List<CatalogCollection> collectionList);

}
