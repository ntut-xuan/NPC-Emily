package emily.dcb.event;

import emily.dcb.database.StoryDatabase;
import emily.dcb.utils.*;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.*;

public class TextAnswerEvent implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        //If message is non-private message, return.
        if (!messageCreateEvent.isPrivateMessage()) {
            return;
        }

        //Get message.
        Message message = messageCreateEvent.getMessage();

        //Get message content.
        String content = message.getContent();

        //If it's necessary, you can split message by space, and access this array.
        String[] contentSplit = content.split(" ");

        //Get message author.
        MessageAuthor author = message.getAuthor();

        //Do not catch any message from Emily.
        if (author.isYourself()) {
            return;
        }

        Optional<User> userOptional = author.asUser();
        if (userOptional.isEmpty()) {
            return;
        }
        User user = userOptional.get();

        //Get text channel object
        TextChannel channel = messageCreateEvent.getChannel();

        if (contentSplit[0].equals("ans")) {
            //使用者還沒有開始故事線，跳過
            if (!StoryEvent.userStoryLoadMap.containsKey(author.getIdAsString())) {
                return;
            }

            //取得故事編號
            int storyID = StoryEvent.userStoryLoadMap.get(author.getIdAsString());

            //取得故事類別
            String storyType = StoryDatabase.getStoryObjectByIndex(storyID).getClass().getSimpleName();

            //只有<ans後面沒有答案，跳過
            if (contentSplit.length == 1) {
                return;
            }

            //<ans <答案>可能會有空格，用這個把空格合併在一起
            String answer = String.join(" ", Arrays.copyOfRange(contentSplit, 1, contentSplit.length));

            //確認型態是不是TYStoryObject，是的話才會給輸入，否則不動作
            if (storyType.equals("TYStoryObject")) {

                /*這個部分把答案存起來*/
                TYStoryObject storyObject = (TYStoryObject) StoryDatabase.getStoryObjectByIndex(storyID);

                UserDataObject userDataObject = StoryEvent.answerMap.get(author.getIdAsString());
                userDataObject.setReplyByIndex(storyID, new ReplyPackage(storyObject, answer));
                StoryEvent.answerMap.put(author.getIdAsString(), userDataObject);

                /* 特判StoryID=3的情況，要把學校縮寫轉成學校名稱 */
                if (storyID == 3){
                    String upperAns = answer.toUpperCase();
                    Map<String, String> schoolAbberMap = SchoolAbbrTableCrawler.map;
                    if(!schoolAbberMap.containsKey(upperAns)){
                        //goto story 9
                        StoryEvent.executeStoryByIndex(null, user, channel, 9);
                    }else{
                        userDataObject.setReplyByIndex(storyID, new ReplyPackage(storyObject, schoolAbberMap.get(upperAns)));
                        StoryEvent.executeStoryByIndex(null, user, channel, storyObject.getNext());
                        StoryEvent.answerMap.put(author.getIdAsString(), userDataObject);
                    }
                    return;
                }

                /* 特判StoryID=7的狀況，要把parameter丟給executeStoryByIndex做取代 */
                if (storyID == 7) {
                    userDataObject.setReplyByIndex(97, new ReplyPackage("DiscordTag", user.getDiscriminatedName()));
                    StoryEvent.answerMap.put(author.getIdAsString(), userDataObject);
                    Map<String, String> map = new HashMap<>();
                    map.put("school", userDataObject.getReplyByIndex(3).getAnswer());
                    map.put("name", userDataObject.getReplyByIndex(6).getAnswer());
                    StoryEvent.executeStoryByIndex(null, user, channel, storyObject.getNext());
                    return;
                }

                /* 特判StoryID=9的情況，要把名子輸入到StoryID=3 */
                if(storyID == 9){
                    userDataObject.setReplyByIndex(3, new ReplyPackage(storyObject, answer));
                    StoryEvent.answerMap.put(author.getIdAsString(), userDataObject);
                    StoryEvent.executeStoryByIndex(null, user, channel, storyObject.getNext());
                    return;
                }

                /* 繼續執行故事線 */
                StoryEvent.executeStoryByIndex(null, user, channel, storyObject.getNext());
            }
        }
    }
}
