import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class CrimeLogScraper {

    public static void main(String[] args) {
        String mainUrl = "https://www.purdue.edu/ehps/police/statistics-policies/daily-crime-log.php";

        try {
            Document document = Jsoup.connect(mainUrl).get();
            Elements crimeLogs = document.select(
                    "section.content__group");

            for (int i = 0; i < crimeLogs.size(); i++) {
                System.out.println(crimeLogs.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
