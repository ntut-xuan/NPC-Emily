package emily.dcb.utils;

import com.google.gson.JsonObject;
import emily.dcb.database.StoryDatabase;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.awt.*;
import java.util.*;
import java.util.List;

public class AnswerObject {

    User user;

    public AnswerObject(User user){
        this.user = user;
    }

    Map<Integer, ReplyPackage> map = new HashMap<>();

    public ReplyPackage getReplyByIndex(int index){
        return map.getOrDefault(index, null);
    }

    public void setReplyByIndex(int index, ReplyPackage replyPackage){
        map.put(index, replyPackage);
    }

    public Set<Map.Entry<Integer, ReplyPackage>> getMapEntry(){
        return map.entrySet();
    }

    public List<ReplyPackage> getReplyPackageList(){
        List<ReplyPackage> list = new ArrayList<>();
        for(Map.Entry<Integer, ReplyPackage> replyPackageEntry : map.entrySet()){
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

        for(Map.Entry<Integer, ReplyPackage> entry : map.entrySet()){
            ReplyPackage replyPackage = entry.getValue();
            embedBuilder.addInlineField(replyPackage.getProblemStatement(), replyPackage.answer);
        }

        return embedBuilder;
    }

    public JsonObject getReplyJsonObjectFormat(){
        JsonObject jsonObject = new JsonObject();
        for(Map.Entry<Integer, ReplyPackage> entry : getMapEntry()){
            JsonObject subObject = new JsonObject();
            subObject.addProperty("index", entry.getKey());
            subObject.addProperty("answer", entry.getValue().getAnswer());
            jsonObject.add(entry.getValue().getProblemStatement(), subObject);
        }
        return jsonObject;
    }

    public static AnswerObject parse(User user, JsonObject jsonObject){
        AnswerObject answerObject = new AnswerObject(user);
        for(String problemStatement : jsonObject.keySet()){
            JsonObject replyPackage = jsonObject.get(problemStatement).getAsJsonObject();
            int index = replyPackage.get("index").getAsInt();
            String answer = replyPackage.get("answer").getAsString();
            answerObject.setReplyByIndex(index, new ReplyPackage(problemStatement, answer));
        }
        return answerObject;
    }
}
