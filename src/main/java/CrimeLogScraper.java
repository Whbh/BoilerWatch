import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class CrimeLogScraper {

    public static void main(String[] args) {
        String mainUrl = "https://www.purdue.edu/ehps/police/statistics-policies/daily-crime-log.php";

        ArrayList <Elements> dates = new ArrayList <>();
        ArrayList <String> datesString = new ArrayList <>();
        ArrayList <Element> datesLocation = new ArrayList <>();

        LocalDate today = LocalDate.now();
        LocalDate targetedDay = LocalDate.now();
        //LocalDate today = LocalDate.of(2026, 11, 18);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
        String newDate = targetedDay.format(formatter);

        ArrayList<String> savedCrimeLog = new ArrayList<String>();

        boolean change = false;
        boolean isEmpty = false;
        boolean brake = false;

        Element blockByDate = null;
        Elements theDate = null;

        try {
            Document document = Jsoup.connect(mainUrl).get();
            Elements crimeLogs = document.select("section.content__group");

            for (int i = 0; i < crimeLogs.size(); i++) {
                dates.add(crimeLogs.get(i).select("h3"));
                datesString.add(crimeLogs.get(i).select("h3").text());
                datesLocation.add(crimeLogs.get(i));
            }

            try (BufferedReader br = new BufferedReader(new FileReader("savedCrimeLog.txt"))) {
                String line = br.readLine();

                //System.out.println(line);
                for (int i = 0; i < dates.size(); i++) {
                    if (brake) {
                        break;
                    }
                    if (!(dates.get(i).isEmpty())) {
                        try {
                            //System.out.println(datesString.get(i));
                            targetedDay = LocalDate.parse(datesString.get(i), formatter);
                            isEmpty = false;
                        } catch (DateTimeParseException e) {
                            System.out.println("Date cant be parsed.");
                        }
                    } else {
                        isEmpty = true;
                    }

                    if (!(isEmpty)) {
                        try {
                            newDate = targetedDay.format(formatter);
                            if (targetedDay.isAfter(today) || newDate.equals(line)) {
                                continue;
                            }
                        } catch (NullPointerException e) {
                            System.out.println("line was null");
                            try (PrintWriter pw = new PrintWriter(new FileOutputStream("savedCrimeLog.txt"))) {
                                pw.println(newDate);
                            } catch (FileNotFoundException l) {
                                System.out.println("Cannot write to savedCrimeLog.txt");
                            }
                            continue;
                        }

                        for (int k = 0; k < dates.size(); k++) {
                            if (dates.get(k).text().contains(newDate)) {
                                blockByDate = datesLocation.get(k);
                                theDate = dates.get(k);
                                change = true;
                                brake = true;
                                break;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Cannot open savedCrimeLog.txt.");
                return;
            }

            if (change) {
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
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
