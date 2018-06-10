package ru.av3969.stickerscollector.data.remote;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;

public class LaststickerHelper {

    private final String baseUrl = "https://www.laststicker.ru/";
    private final String categoryPath = "cards/";

    @Inject
    public LaststickerHelper() {
    }

    private String getCategoryUrl() {
        return baseUrl + categoryPath;
    }

    public List<CatalogCategory> getCategoryList() {

        List<CatalogCategory> categoryList = new ArrayList<>();
        Document doc;
        Pattern patternUrlSplit = Pattern.compile("[/]");
        String strings[];
        String catName;

        try {
            doc = Jsoup.connect(getCategoryUrl()).get();
        } catch (IOException e) {
            return categoryList;
        }

        Element content = doc.selectFirst("div#content table");

        Elements rootCats = content.select("td > p");
        long catCounter = 0L;
        for (Element rootCat : rootCats) {

            long rootCatId = ++catCounter;

            strings = patternUrlSplit.split(rootCat.selectFirst("a").attr("href"));
            catName = strings.length > 0 ? strings[strings.length - 1] : "";

            categoryList.add(new CatalogCategory(rootCatId, catName, rootCat.text(), 0L));

            Elements cats = rootCat.nextElementSibling().select("a");
            for (Element cat : cats) {
                strings = patternUrlSplit.split(cat.attr("href"));
                catName = strings.length > 0 ? strings[strings.length - 1] : "";
                categoryList.add(new CatalogCategory(++catCounter, catName, cat.text(), rootCatId));
            }
        }

        return categoryList;
    }

    public String test(String url) {
        Document doc;
        StringBuilder str = new StringBuilder();

        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            return "";
        }

        Elements uls = doc.select("ul");
        for(Element ul:uls) {
            Elements as = ul.select("a");
            for(Element a:as) {
                str.append(a.text() + "\n");
            }
        }

        return str.toString();
    }

}
