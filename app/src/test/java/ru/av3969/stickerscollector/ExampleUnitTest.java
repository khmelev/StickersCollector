package ru.av3969.stickerscollector;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
import ru.av3969.stickerscollector.data.db.entity.CatalogStickers;
import ru.av3969.stickerscollector.data.remote.LaststickerHelper;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void workingCorrent() {
        LaststickerHelper laststickerHelper = new LaststickerHelper();
//        List<CatalogCategory> categoryList = laststickerHelper.getCategoryList();
//        List<CatalogCollection> collectionList = laststickerHelper.getCollectionList("fifa_world_cup", 10L);
        try {
            List<CatalogStickers> stickers = laststickerHelper.getStickersList("panini_fifa_world_cup_2018", 3852L);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}