package emily.dcb.utils;

import org.javacord.api.entity.user.User;
import org.joda.time.DateTime;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
            File dir = new File(System.getProperty("user.dir") + "\\log");
            if(!dir.exists()){
                dir.mkdir();
            }
            File log = new File(System.getProperty("user.dir") + "\\log\\" + date + ".txt");
            if(!log.exists()){
                log.createNewFile();
            }
            String text = "";
            Scanner scanner = new Scanner(log);
            while(scanner.hasNextLine()){
                text += scanner.nextLine() + "\n";
            }
            scanner.close();
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
