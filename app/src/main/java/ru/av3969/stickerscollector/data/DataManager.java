package ru.av3969.stickerscollector.data;

import java.util.List;

import io.reactivex.Single;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;

public interface DataManager {

    Single<List<CatalogCategory>> loadCategoryList(Long parentId);

}
