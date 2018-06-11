package ru.av3969.stickerscollector;

import org.junit.Test;

import java.util.List;

import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;
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
        List<CatalogCollection> collectionList = laststickerHelper.getCollectionList("fifa_world_cup", 10L);
    }
}