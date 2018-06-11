package ru.av3969.stickerscollector.data.remote;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import ru.av3969.stickerscollector.data.db.entity.CatalogCategory;
import ru.av3969.stickerscollector.data.db.entity.CatalogCollection;

public class LaststickerHelper {

    private final String baseUrl = "https://www.laststicker.ru/";
    private final String categoryPath = "cards/";
    private final String collectionsPath = "s/";

    private final Pattern patternUrlSplit = Pattern.compile("[/]");

    @Inject
    public LaststickerHelper() {
    }

    private String getCategoryUrl() {
        return baseUrl + categoryPath;
    }

    private String getCollectionsUrl(String categoryName) {
        return baseUrl + categoryPath + collectionsPath + categoryName + "/";
    }

    public List<CatalogCategory> getCategoryList() {

        List<CatalogCategory> categoryList = new ArrayList<>();

        Document doc;
        String strings[];
        String catName;

        try {
            doc = Jsoup.connect(getCategoryUrl()).get();
        } catch (IOException e) {
            return categoryList;
        }

        Element content = doc.selectFirst("div#content table");

        long catCounter = 0L;
        for (Element rootCat : content.select("td > p")) {

            long rootCatId = ++catCounter;

            strings = patternUrlSplit.split(rootCat.selectFirst("a").attr("href"));
            catName = strings.length > 0 ? strings[strings.length - 1] : "";

            categoryList.add(new CatalogCategory(rootCatId, catName, rootCat.text(), 0L));

            for (Element cat : rootCat.nextElementSibling().select("a")) {
                strings = patternUrlSplit.split(cat.attr("href"));
                catName = strings.length > 0 ? strings[strings.length - 1] : "";
                categoryList.add(new CatalogCategory(++catCounter, catName, cat.text(), rootCatId));
            }
        }

        return categoryList;
    }

    public List<CatalogCollection> getCollectionList(String categoryName, Long categoryId) {
        List<CatalogCollection> collectionList = new ArrayList<>();

        Document doc;
        Pattern patNumberId = Pattern.compile("\\d+");
        Pattern patStickersType = Pattern.compile("Наклеек");
        Matcher matcher;

        try {
            doc = Jsoup.connect(getCollectionsUrl(categoryName)).get();
        } catch (IOException e) {
            return collectionList;
        }

        Element content = doc.selectFirst("div#content table");

        for (Element tr : content.select("tr")) {
            for (Element albumItem : tr.select("td div.album_item")) {

                //Ищем id коллекции
                String albumIcoUrl = albumItem.selectFirst("img.ico_album").attr("src");
                matcher = patNumberId.matcher(albumIcoUrl);
                if (!matcher.find()) continue;
                Long collId = Long.valueOf(matcher.group());

                //Ищем name и title
                Element albumTitle = albumItem.selectFirst("h3 > a");
                String strAr[] = patternUrlSplit.split(albumTitle.attr("href"));
                String collName = strAr.length > 0 ? strAr[strAr.length - 1] : "";
                String collTitle = albumTitle.text();

                //Ищем год
                Element yearElement = albumItem.selectFirst("span");
                matcher = patNumberId.matcher(yearElement.text());
                if (!matcher.find()) continue;
                Short collYear = Short.valueOf(matcher.group());

                //Ищем тип наклеек и их количество
                Element sizeElement = yearElement.nextElementSibling().nextElementSibling();
                matcher = patStickersType.matcher(sizeElement.text());
                Byte collStype = matcher.find() ? (byte)1 : (byte)2;
                matcher = patNumberId.matcher(sizeElement.text());
                if (!matcher.find()) continue;
                Short collSize = Short.valueOf(matcher.group());

                //Ищем desc
                String collDesc = albumItem.selectFirst("p").text();

                collectionList.add(new CatalogCollection(collId, collName, collTitle, categoryId,
                        collYear, collStype, collSize, collDesc));
            }
        }

        return collectionList;
    }

}
