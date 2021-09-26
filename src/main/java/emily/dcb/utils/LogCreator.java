package emily.dcb.utils;

import org.javacord.api.entity.user.User;
import org.joda.time.DateTime;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class LogCreator {

    public static void info(String str){
        DateTime dateTime = DateTime.now();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date = simpleDateFormat.format(dateTime.toDate());
        String format = String.format("%s [Info]: %s", date, str);
        System.out.println(format);
        save(format);
    }

    public static void chat(User user, String str){
        DateTime dateTime = DateTime.now();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date = simpleDateFormat.format(dateTime.toDate());
        String format = String.format("%s [Chat]: %s -> %s", date, user.getDiscriminatedName(), str);
        System.out.println(format);
        save(format);
    }

    public static void error(String str){
        DateTime dateTime = DateTime.now();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date = simpleDateFormat.format(dateTime.toDate());
        String format = String.format("%s [Error]: %s", date, str);
        System.out.println(format);
        save(format);
    }

    public static void save(String message){
        try {
            DateTime dateTime = DateTime.now();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = simpleDateFormat.format(dateTime.toDate());
            File dir = new File(System.getProperty("user.dir") + File.separator + "log");
            if(!dir.exists()){
                dir.mkdir();
            }
            File log = new File(System.getProperty("user.dir") + File.separator + "log" + File.separator + date + ".txt");
            if(!log.exists()){
                log.createNewFile();
            }
            String text = "";
            InputStream inputStream = new FileInputStream(log);
            BufferedReader reader = new BufferedReader (new InputStreamReader(inputStream, StandardCharsets.UTF_8), 8192);
            String temp = "";
            while ((temp = reader.readLine()) != null) {
                text += temp + "\n";
            }
            reader.close();
            text += message + "\n";
            OutputStream outputStream = new FileOutputStream(log);
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            printWriter.print(text);
            printWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
