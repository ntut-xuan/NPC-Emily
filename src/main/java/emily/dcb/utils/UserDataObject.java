package emily.dcb.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import emily.dcb.database.ClubClassDatabase;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.awt.*;
import java.util.*;
import java.util.List;

public class UserDataObject {

    User user;

    public UserDataObject(User user){
        this.user = user;
    }

    Map<Integer, ReplyPackage> userAnswerMap = new HashMap<>();
    Set<ClubClass> registeredClubClass = new HashSet<>();

    public void regiseredClubClass(ClubClass clubClass){
        registeredClubClass.add(clubClass);
    }

    public void unregisterClubClass(ClubClass clubClass){
        registeredClubClass.remove(clubClass);
    }

    public ReplyPackage getReplyByIndex(int index){
        return userAnswerMap.getOrDefault(index, null);
    }

    public void setReplyByIndex(int index, ReplyPackage replyPackage){
        userAnswerMap.put(index, replyPackage);
    }

    public Set<Map.Entry<Integer, ReplyPackage>> getMapEntry(){
        return userAnswerMap.entrySet();
    }

    public List<ReplyPackage> getReplyPackageList(){
        List<ReplyPackage> list = new ArrayList<>();
        for(Map.Entry<Integer, ReplyPackage> replyPackageEntry : userAnswerMap.entrySet()){
            list.add(replyPackageEntry.getValue());
        }
        return list;
    }

    public EmbedBuilder getEmbed() {

        EmbedBuilder embedBuilder = new EmbedBuilder();

        String title = user.getDiscriminatedName();
        if(getReplyByIndex(1) == null){
            embedBuilder.setTitle(title + " - 還沒註冊過");
        }else if(getReplyByIndex(1).answer.equals("Yes")){
            embedBuilder.setTitle(title + " - 是北科大的學生");
        }else if(getReplyByIndex(1).answer.equals("No")){
            embedBuilder.setTitle(title + " - 不是北科大的學生");
        }

        embedBuilder.setThumbnail(user.getAvatar());
        embedBuilder.setColor(Color.CYAN);

        for(Map.Entry<Integer, ReplyPackage> entry : userAnswerMap.entrySet()){
            ReplyPackage replyPackage = entry.getValue();
            embedBuilder.addInlineField(replyPackage.getProblemStatement(), replyPackage.answer);
        }

        embedBuilder.addField("---------社課---------", "-----------------------");

        for(ClubClass clubClass : registeredClubClass){
            embedBuilder.addField(clubClass.getClassName(), "已參加");
        }

        return embedBuilder;
    }

    public JsonObject getReplyJsonObjectFormat(){
        JsonObject jsonObject = new JsonObject();
        JsonObject userInfo = new JsonObject();
        for(Map.Entry<Integer, ReplyPackage> entry : getMapEntry()){
            JsonObject subObject = new JsonObject();
            subObject.addProperty("index", entry.getKey());
            subObject.addProperty("answer", entry.getValue().getAnswer());
            userInfo.add(entry.getValue().getProblemStatement(), subObject);
        }
        jsonObject.add("userInfo", userInfo);
        JsonArray registeredClubClassArray = new JsonArray();
        for(ClubClass clubClass : registeredClubClass){
            String className = clubClass.getClassName();
            registeredClubClassArray.add(className);
        }
        jsonObject.add("clubClass", registeredClubClassArray);
        return jsonObject;
    }

    public static UserDataObject parse(User user, JsonObject jsonObject){
        UserDataObject userDataObject = new UserDataObject(user);
        JsonObject userInfoJsonObject = jsonObject.getAsJsonObject("userInfo");
        JsonArray clubClassJsonObject = jsonObject.getAsJsonArray("clubClass");
        for(String problemStatement : userInfoJsonObject.keySet()){
            JsonObject replyPackage = userInfoJsonObject.get(problemStatement).getAsJsonObject();
            int index = replyPackage.get("index").getAsInt();
            String answer = replyPackage.get("answer").getAsString();
            userDataObject.setReplyByIndex(index, new ReplyPackage(problemStatement, answer));
        }
        for(int index = 0; index < clubClassJsonObject.size(); index++){
            String clubClassName = clubClassJsonObject.get(index).getAsString();
            for(ClubClass clubClass : ClubClassDatabase.clubClassList){
                if(clubClass.getClassName().equals(clubClassName)){
                    userDataObject.regiseredClubClass(clubClass);
                }
            }
        }
        return userDataObject;
    }
}
