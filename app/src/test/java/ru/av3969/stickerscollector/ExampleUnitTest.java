package ru.av3969.stickerscollector;

import org.junit.Test;

import ru.av3969.stickerscollector.data.remote.Laststicker;

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
        Laststicker laststicker = new Laststicker();
        System.out.println("Trying get category list:");
        System.out.println(laststicker.test(laststicker.getCategoryUrl()));
    }
}