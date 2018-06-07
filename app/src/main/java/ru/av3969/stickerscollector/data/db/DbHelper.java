package ru.av3969.stickerscollector.data.db;

import java.util.List;

import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;

public interface DbHelper {

    Long insertCatalogCategory(CatalogCategory catalogCategory);

    List<CatalogCategory> selectCatalogCategoryList();

}
