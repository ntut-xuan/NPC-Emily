package emily.dcb.utils;

import emily.dcb.exception.StudentIDNotFoundException;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class StudentInfoCrawler {
    public Pair<String, String> getStudentNameAndClass(String studentID) throws IOException, StudentIDNotFoundException {

        File file = new File("NTUT-StudentInfo.txt");
        Scanner cin = new Scanner(file);

        String ID = cin.nextLine();
        String password = cin.nextLine();

        String urlString = "https://app.ntut.edu.tw/login.do";
        Map<String, String> datas = new HashMap<>();
        datas.put("muid", ID);
        datas.put("mpassword", password);
        datas.put("forceMobile", "app");
        datas.put("md5Code", "1111");
        datas.put("ssoId", "");
        Map<String, String> headers = new HashMap<>();
        headers.put("user-agent", "Direk Android App");
        Connection.Response response = Jsoup.connect(urlString).headers(headers).data(datas).maxBodySize(0).method(Connection.Method.POST).execute();


        String ssoIndexURL = "https://app.ntut.edu.tw/ssoIndex.do?apUrl=https://aps.ntut.edu.tw/course/tw/Select.jsp&apOu=aa_0010-&sso=true&datetime1=" + System.currentTimeMillis();
        Connection ssoIndexConnection = Jsoup.connect(ssoIndexURL);
        ssoIndexConnection = ssoIndexConnection.cookies(response.cookies());
        ssoIndexConnection = ssoIndexConnection.headers(response.headers());
        Document ssoIndexDocument = ssoIndexConnection.get();
        String sessionId = ssoIndexDocument.select("body > form > input[type=hidden]:nth-child(1)").attr("value");
        String reqFrom = ssoIndexDocument.select("body > form > input[type=hidden]:nth-child(2)").attr("value");
        String userID = ssoIndexDocument.select("body > form > input[type=hidden]:nth-child(3)").attr("value");
        String userType = ssoIndexDocument.select("body > form > input[type=hidden]:nth-child(4)").attr("value");

        Connection poster = Jsoup.connect("https://aps.ntut.edu.tw/course/tw/courseSID.jsp");
        poster = poster.followRedirects(true);
        poster = poster.data("sessionId", sessionId);
        poster = poster.data("reqFrom", reqFrom);
        poster = poster.data("userid", userID);
        poster = poster.data("userType", userType);
        poster = poster.cookies(response.cookies());
        poster = poster.headers(response.headers());
        Connection.Response posterResponse = poster.method(Connection.Method.POST).execute();

        poster = Jsoup.connect("https://aps.ntut.edu.tw/course/tw/courseSID.jsp");
        poster = poster.followRedirects(true);
        poster = poster.data("sessionId", sessionId);
        poster = poster.data("reqFrom", reqFrom);
        poster = poster.data("userid", userID);
        poster = poster.data("userType", userType);
        poster = poster.cookies(response.cookies());
        poster = poster.headers(response.headers());
        posterResponse = poster.method(Connection.Method.GET).execute();

        poster = Jsoup.connect("https://aps.ntut.edu.tw/course/tw/Select.jsp");
        poster = poster.followRedirects(true);
        poster = poster.data("code", studentID);
        poster = poster.data("format", "-2");
        poster = poster.data("year", "110");
        poster = poster.data("sem", "1");
        poster = poster.cookies(response.cookies());
        poster = poster.headers(response.headers());
        posterResponse = poster.method(Connection.Method.GET).execute();

        Document document = posterResponse.parse();

        Elements element = document.select("body > table:nth-child(2) > tbody > tr:nth-child(1) > td");

        String data = element.text();

        if (data.equals("")) {
            throw new StudentIDNotFoundException();
        }

        List<String> list = new ArrayList<>();

        String temp = "";

        for (int i = 0, j = 0; i < data.length(); i++) {
            if (data.charAt(i) == '：') {
                j = 1;
                continue;
            }
            if (j == 1 && data.charAt(i) == '　') {
                j = 0;
                list.add(temp);
                temp = "";
                continue;
            }
            if (j == 1) {
                temp += data.charAt(i);
            }
        }

        String studentName = list.get(1);
        String studentClass = list.get(2);
        return Pair.of(studentName, studentClass);
    }
}
