package ru.av3969.stickerscollector.data;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Single;
import ru.av3969.stickerscollector.data.db.DbHelper;
import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.data.db.entity.CatalogStickers;
import ru.av3969.stickerscollector.data.db.entity.DepositoryCollection;
import ru.av3969.stickerscollector.data.db.entity.DepositoryStickers;
import ru.av3969.stickerscollector.data.pref.PreferencesHelper;
import ru.av3969.stickerscollector.data.remote.LaststickerHelper;
import ru.av3969.stickerscollector.ui.vo.StickerVO;

public class AppDataManager implements DataManager {

    private PreferencesHelper preferencesHelper;
    private DbHelper dbHelper;
    private LaststickerHelper laststickerHelper;

    @Inject
    AppDataManager(PreferencesHelper preferencesHelper, DbHelper dbHelper,
                          LaststickerHelper laststickerHelper) {
        this.preferencesHelper = preferencesHelper;
        this.dbHelper = dbHelper;
        this.laststickerHelper = laststickerHelper;
    }

    @Override
    public Single<List<CatalogCategory>> loadCategoryList(Long parentId) {
         return Single.fromCallable(() -> {
             List<CatalogCategory> categoryList = dbHelper.selectCategoryList(parentId);
             if(categoryList.isEmpty()) {
                 List<CatalogCategory> lastStickerCategory = laststickerHelper.getCategoryList();
                 if(parentId.equals(CatalogCategory.defaultId))
                     dbHelper.inflateCategories(lastStickerCategory);
                 else
                    dbHelper.updateCategories(lastStickerCategory);

                 categoryList = dbHelper.selectCategoryList(parentId);
             }
             return categoryList;
         });
    }

    @Override
    public Single<List<CatalogCollection>> loadCatalogCollectionList(Long categoryId) {
        return Single.fromCallable(() -> {
            List<CatalogCollection> collectionList = dbHelper.selectCatalogCollectionList(categoryId);
            if(collectionList.isEmpty()) {
                CatalogCategory category = dbHelper.selectCategory(categoryId);
                if (category != null) {
                    dbHelper.inflateCollections(laststickerHelper.getCollectionList(category.getName(), categoryId));
                    collectionList = dbHelper.selectCatalogCollectionList(categoryId);
                }
            }
            return collectionList;
        });
    }

    @Override
    public Single<CatalogCollection> loadCatalogCollection(Long id) {
        return Single.fromCallable(() -> {
            return dbHelper.selectCatalogCollection(id);
        });
    }

    @Override
    public Single<List<StickerVO>> loadStickerVOList(Long parentCollection, Long collectionId) {
        return Single.fromCallable(() -> {
            List<StickerVO> stickers = new ArrayList<>();
            @SuppressLint("UseSparseArrays")
            Map<Long,DepositoryStickers> depositoryStickersMap = new HashMap<>();

            List<CatalogStickers> catalogStickers = dbHelper.selectCatalogStickersList(parentCollection);
            if(catalogStickers.isEmpty()) {
                CatalogCollection catalogCollection = dbHelper.selectCatalogCollection(parentCollection);
                dbHelper.inflateStickers(laststickerHelper.getStickersList(catalogCollection.getName(), parentCollection));
                catalogStickers = dbHelper.selectCatalogStickersList(parentCollection);
            }
            List<DepositoryStickers> depositoryStickers = dbHelper.selectDepositoryStickersList(collectionId);
            if(!depositoryStickers.isEmpty()) {
                for (DepositoryStickers depSticker : depositoryStickers) {
                    depositoryStickersMap.put(depSticker.getStickerId(), depSticker);
                }
            }

            for (CatalogStickers catSticker : catalogStickers) {
                if (!depositoryStickersMap.isEmpty()) {
                    DepositoryStickers depSticker = depositoryStickersMap.get(catSticker.getId());
                    if (depSticker != null)
                        stickers.add(new StickerVO(catSticker, depSticker));
                    else
                        stickers.add(new StickerVO(catSticker));
                } else {
                    stickers.add(new StickerVO(catSticker));
                }
            }

            return stickers;
        });
    }

    @Override
    public Single<DepositoryCollection> loadDepositoryCollection(Long id) {
        return Single.fromCallable(() -> {
            DepositoryCollection collection = dbHelper.selectDepositoryCollection(id);
            return collection != null ? collection : new DepositoryCollection(null,null,"",0L,(short)0,0);
        });
    }
}
