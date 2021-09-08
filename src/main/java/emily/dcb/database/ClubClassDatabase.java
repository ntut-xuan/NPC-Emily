package emily.dcb.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import emily.dcb.utils.ClubClass;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ClubClassDatabase {

    public static List<ClubClass> clubClassList = new ArrayList<>();

    public static void load() throws IOException {
        File file = new File("ClubClass.json");
        if(!file.exists()){
            file.createNewFile();
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println("{}");
            printWriter.close();
            return;
        }
        InputStreamReader fileInputStream = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
        String jsonString = "";
        int c;
        while((c = fileInputStream.read()) > 0){
            jsonString += (char) c;
        }
        JsonObject object = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonArray jsonArray = object.get("Class").getAsJsonArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JsonObject subObject = jsonArray.get(i).getAsJsonObject();
            String index = String.valueOf(i);
            String className = subObject.get("className").getAsString();
            String classDescription = subObject.get("description").getAsString();
            String classScheduleString = subObject.get("schedule").getAsString();
            int maxRegisterSize = subObject.get("maxRegisterSize").getAsInt();

            /* Convert scheduleString into DateTime */
            DateTime dateTime = DateTime.parse(classScheduleString, DateTimeFormat.forPattern("yyyy/MM/dd HH:mm"));

            ClubClass clubClass = new ClubClass(className, classDescription, dateTime, maxRegisterSize);
            clubClassList.add(clubClass);
        }
    }
}
