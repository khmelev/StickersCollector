package ru.av3969.stickerscollector.data.remote;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Laststicker {

    private final String baseUrl = "https://www.laststicker.ru/";
    private final String categoryPath = "cards/";

    public String getCategoryUrl() {
        return baseUrl + categoryPath;
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
