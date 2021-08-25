package emily.dcb.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SchoolAbbrTableCrawler {

    public static Map<String, String> map = new HashMap<>();

    public static void load() throws IOException {
        Document document = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_universities_in_Taiwan").get();
        for(int i = 3; i <= 60; i++) {
            String abbr = document.select(String.format("#mw-content-text > div.mw-parser-output > table:nth-child(12) > tbody > tr:nth-child(%d) > td:nth-child(2)", i)).text();
            String chName= document.select(String.format("#mw-content-text > div.mw-parser-output > table:nth-child(12) > tbody > tr:nth-child(%d) > td:nth-child(3)", i)).text();
            if(!abbr.equals("")) map.put(abbr.split(" ")[0], chName);
        }
        for(int i = 3; i <= 109; i++) {
            String abbr = document.select(String.format("#mw-content-text > div.mw-parser-output > table:nth-child(27) > tbody > tr:nth-child(%d) > td:nth-child(2)", i)).text();
            String chName= document.select(String.format("#mw-content-text > div.mw-parser-output > table:nth-child(27) > tbody > tr:nth-child(%d) > td:nth-child(3)", i)).text();
            if(!abbr.equals("")) map.put(abbr.split(" ")[0], chName);
        }
        for(int i = 3; i <= 10; i++) {
            String abbr = document.select(String.format("#mw-content-text > div.mw-parser-output > table:nth-child(36) > tbody > tr:nth-child(%d) > td:nth-child(2)", i)).text();
            String chName= document.select(String.format("#mw-content-text > div.mw-parser-output > table:nth-child(36) > tbody > tr:nth-child(%d) > td:nth-child(3)", i)).text();
            if(!abbr.equals("")) map.put(abbr.split(" ")[0], chName);
        }
    }
}
