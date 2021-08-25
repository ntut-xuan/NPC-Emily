package emily.dcb.utils;

import emily.dcb.database.StoryDatabase;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        int size = StoryDatabase.getStoryObjectCount();

        for(Map.Entry<Integer, ReplyPackage> entry : map.entrySet()){
            ReplyPackage replyPackage = entry.getValue();
            embedBuilder.addField(replyPackage.getProblemStatement(), replyPackage.answer);
        }

        return embedBuilder;
    }
}
