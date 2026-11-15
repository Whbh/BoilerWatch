import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class ArchivedCrimeLogScraper {

    public static void main(String[] args) {
        String mainUrl = "https://www.purdue.edu/ehps/police/statistics-policies/daily-crime-log-archives/index.php";

        try {
            Document document = Jsoup.connect(mainUrl).get();
            Elements ArchivedCrimeLogUrls = document.select(
                    "a[href$='-daily-crime-log.php'][title$='Daily Crime Log'][title$='Daily Crime Log']:contains(Week of)");

            for (Element aclu: ArchivedCrimeLogUrls) {
                System.out.println(aclu);
                String temp = aclu.toString();
                System.out.println("https://www.purdue.edu/ehps/police/statistics-policies/daily-crime-log-archives/" +  temp.substring(9, 35));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
