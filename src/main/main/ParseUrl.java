package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

/**
 *  Class ParseUrl contain functions to parse Birthday congratulations
 *      and Birthday pictures.
 * @author Kovalenko Tim
 * @version 1.0
 * @since   2021-10-18
 *
 */

public class ParseUrl {
    /**
     * Function to parse Birthday congratulations
     * @param list Empty list for congratulations
     */
    public static void BirthdayTxt(ArrayList<String> list) {
        String Url = "https://www.homemade-gifts-made-easy.com/birthday-wishes.html";
        try {
            Document page = Jsoup.connect(Url).get();
            Elements congratulations = page.select("p[class = poem-left]");
            for (Element s : congratulations) {
                if (Functions.isFamily(s.text())) list.add(s.text());
            }
        } catch (IndexOutOfBoundsException | IOException ignored) {}
    }

    /**
     * Function to parse Birthday pictures
     * @param Url Url for thematic picture
     * @param list Empty list for pictures
     * @return List with Birthday pictures
     */
    public static ArrayList<String> PicUrl(String Url, ArrayList<String> list ) throws IOException {
        String site = "https://www.greetingsisland.com";
        try {
            Document page = Jsoup.connect(Url).get();
            Elements congratulations = page.select("IMG");
            for (Element s : congratulations) {
                if (s.attr("data-src").length() > 1) list.add(s.attr("data-src"));
            }

            String link = site + Jsoup.connect(Url).get().select("div.pagination a").get(0).attr("href");
            int i = 0;
            while (Jsoup.connect(link).get().select("div.pagination a").size() > 1) {
                link = site + Jsoup.connect(Url).get().select("div.pagination a").get(i++).attr("href");
                page = Jsoup.connect(link).get();
                congratulations = page.select("IMG");
                for (Element s : congratulations) {
                    if (s.attr("data-src").length() > 1) list.add(s.attr("data-src"));
                }
            }
        } catch (IndexOutOfBoundsException | IOException ignored) {}

        return list;
    }
}

