package emily.dcb.database;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import emily.dcb.event.StoryEvent;
import emily.dcb.main.Main;
import emily.dcb.utils.AnswerObject;
import emily.dcb.utils.LogCreator;
import emily.dcb.utils.ReplyPackage;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.permission.RoleUpdater;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.server.ServerUpdater;
import org.javacord.api.entity.user.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class UserDataBase {

    static Map<String, String> StudentToUID = new HashMap<>();
    static Map<String, JsonObject> UIDReply = new HashMap<>();

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
            UIDReply.put(key, object.get(key).getAsJsonObject());
        }
    }

    public static void save() throws IOException {
        File file = new File("UserData.json");

        JsonObject jsonObject = new JsonObject();
        for(Map.Entry<String, JsonObject> entry : UIDReply.entrySet()){
            jsonObject.add(entry.getKey(), entry.getValue());
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
        AnswerObject object = StoryEvent.answerMap.get(userID);
        JsonObject jsonObject = new JsonObject();
        for(ReplyPackage replyPackage : object.getReplyPackageList()){
            jsonObject.addProperty(replyPackage.getProblemStatement(), replyPackage.getAnswer());
        }
        UIDReply.put(userID, jsonObject);
    }

    public static boolean containsReply(String ID){
        return UIDReply.containsKey(ID);
    }

    public static void checkUserMemberRole(){
        Role role = EmilySettingDatabase.memberRole;
        Server server = EmilySettingDatabase.server;
        ServerUpdater updater = new ServerUpdater(server);
        for(String keys : UserDataBase.UIDReply.keySet()){
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
