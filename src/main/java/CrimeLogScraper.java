import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;

public class CrimeLogScraper {

    public static void main(String[] args) {
        String mainUrl = "https://www.purdue.edu/ehps/police/statistics-policies/daily-crime-log-archives/111626-daily-crime-log.php";
        ArrayList <Elements> dates = new ArrayList <>();
        ArrayList <Element> datesLocation = new ArrayList <>();

        //LocalDate today = LocalDate.now();
        LocalDate today = LocalDate.of(2026, 11, 18);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        String newDate = today.format(formatter);

        //System.out.println("Current date: " + newDate);

        ArrayList<String> savedCrimeLog = new ArrayList<String>();

        try {
            Document document = Jsoup.connect(mainUrl).get();
            Elements crimeLogs = document.select("section.content__group");

            for (int i = 0; i < crimeLogs.size(); i++) {
                dates.add(crimeLogs.get(i).select("h3"));
                datesLocation.add(crimeLogs.get(i));
            }

            Element blockByDate = null;
            Elements theDate = null;
            for (int i = 0; i < dates.size(); i++) {
                if (dates.get(i).text().contains(newDate)) {
                    blockByDate = datesLocation.get(i);
                    theDate = dates.get(i);
                    break;
                }
            }

            if (blockByDate != null) {
                Elements table = blockByDate.select("tbody tr");
                String date = theDate.toString();
                System.out.println(date.substring(4, date.length() - 5));
                try (PrintWriter pw = new PrintWriter(new FileOutputStream("savedCrimeLog.txt"))) {
                    pw.println(date.substring(4, date.length() - 5));

                    for (int i = 0; i < table.size(); i++) {
                        Elements cells = table.get(i).select("td");
                        if (cells.isEmpty()) {
                            continue;
                        }

                        String row = cells.get(0).text() + " | " + cells.get(1).text() +
                                " | " + cells.get(2).text() + " | " + cells.get(3).text() +
                                " | " + cells.get(4).text();

                        System.out.println(row);
                        pw.println(row);
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Cannot write to savedCrimeLog.txt");
                }
            }

            //for (int i = 0; i < dates.size(); i++) {
               // System.out.println(dates.get(i).text());
            //}

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
