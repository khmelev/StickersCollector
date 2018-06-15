package ru.av3969.stickerscollector.data;

import java.util.List;

import io.reactivex.Single;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.data.db.entity.DepositoryCollection;
import ru.av3969.stickerscollector.ui.vo.StickerVO;

public interface DataManager {

    Single<List<CatalogCategory>> loadCategoryList(Long parentId);

    Single<CatalogCollection> loadCatalogCollection(Long id);

    Single<List<CatalogCollection>> loadCatalogCollectionList(Long parentCat);

    Single<List<StickerVO>> loadStickerVOList(Long parentCollection, Long collectionId);

    Single<DepositoryCollection> loadDepositoryCollection(Long id);
}
