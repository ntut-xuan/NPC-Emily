package emily.dcb.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import emily.dcb.event.StoryEvent;
import emily.dcb.utils.UserDataObject;
import emily.dcb.utils.LogCreator;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.ServerUpdater;
import org.javacord.api.entity.user.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class UserDataBase {

    public static Map<String, String> StudentToUID = new HashMap<>();
    public static Map<String, UserDataObject> UIDDataObject = new HashMap<>();

    public static void load() throws IOException {
        File file = new File("UserData.json");

        /* check file exist */
        if(!file.exists()){

            file.createNewFile();
            LogCreator.info("由於UserData.json不存在，所以創立一個");

            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println("{}");
            printWriter.close();

        }

        /* load file character-by-character */
        String jsonFile = "";
        FileInputStream fileInputStream = new FileInputStream(file);
        Reader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
        Reader buffer = new BufferedReader(reader);
        int r;
        while((r = buffer.read()) != -1){
            jsonFile += (char) r;
        }

        /* Parse text into Json object */
        JsonElement element = JsonParser.parseString(jsonFile);
        JsonObject object = element.getAsJsonObject();
        Set<String> keys = object.keySet();
        for(String key : keys){
            Optional<User> userOptional = EmilySettingDatabase.server.getMemberById(key);
            if(userOptional.isEmpty()){
                continue;
            }
            User user = userOptional.get();
            JsonObject subObject = object.getAsJsonObject(key);
            UserDataObject userDataObject = UserDataObject.parse(user, subObject);
            UIDDataObject.put(key, userDataObject);
            JsonObject userInfoObject = subObject.get("userInfo").getAsJsonObject();
            if(userInfoObject.has("學號")){
                String studentID = userInfoObject.get("學號").getAsJsonObject().get("answer").getAsString();
                StudentToUID.put(studentID, user.getIdAsString());
            }
        }
    }

    public static void save() throws IOException {
        File file = new File("UserData.json");

        JsonObject jsonObject = new JsonObject();
        for(Map.Entry<String, UserDataObject> entry : UIDDataObject.entrySet()){
            String UID = entry.getKey();
            UserDataObject userDataObject = entry.getValue();
            jsonObject.add(UID, userDataObject.getReplyJsonObjectFormat());
        }

        if(!file.exists()){

            file.createNewFile();
            LogCreator.info("由於UserData.json不存在，所以創立一個");

            OutputStream outputStream = new FileOutputStream(file);
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            printWriter.println("{}");
            printWriter.close();

        }

        PrintWriter printWriter = new PrintWriter(file);
        printWriter.println(jsonObject.toString());
        printWriter.close();
    }

    public static void saveReply(String userID){
        UserDataObject object = StoryEvent.answerMap.get(userID);
        UIDDataObject.put(userID, object);
    }

    public static boolean containsReply(String ID){
        return UIDDataObject.containsKey(ID);
    }

    public static void checkUserMemberRole(){
        Role role = EmilySettingDatabase.memberRole;
        Server server = EmilySettingDatabase.server;
        ServerUpdater updater = new ServerUpdater(server);
        for(String keys : UserDataBase.UIDDataObject.keySet()){
            Optional<User> userOptional = server.getMemberById(keys);
            if(userOptional.isEmpty()){
                continue;
            }
            User user = userOptional.get();
            List<Role> roleList = user.getRoles(server);
            if(!roleList.contains(role)){
                updater.addRoleToUser(user, role);
            }
        }
        updater.update();
    }

}
