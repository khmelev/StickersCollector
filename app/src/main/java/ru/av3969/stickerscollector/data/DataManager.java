package ru.av3969.stickerscollector.data;

import java.util.List;

import io.reactivex.Single;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;

public interface DataManager {

    Single<List<CatalogCategory>> loadCategoryList(Long parentId);

    Single<List<CatalogCollection>> loadCollectionList(Long parentCat);

    Single<CatalogCollection> loadCatalogCollection(Long id);
}
