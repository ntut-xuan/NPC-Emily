package emily.dcb.event;

import emily.dcb.database.StoryDatabase;
import emily.dcb.database.UserDataBase;
import emily.dcb.utils.*;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.w3c.dom.Text;

import java.util.*;

public class StoryEvent implements MessageCreateListener {

    static Map<String, Integer> userStoryLoadMap = new HashMap<>();
    public static Map<String, AnswerObject> answerMap = new HashMap<>();

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        //If message is non-private message, return.
        if(!event.isPrivateMessage()){
            return;
        }

        //Get message.
        Message message = event.getMessage();

        //Get message content.
        String content = message.getContent();

        //If it's necessary, you can split message by space, and access this array.
        String[] contentSplit = content.split(" ");

        //Get message author.
        MessageAuthor author = message.getAuthor();

        //Do not catch any message from Emily.
        if(author.isYourself()){
            return;
        }

        //Get text channel object
        TextChannel channel = event.getChannel();

        //If user type message "!start", the story will start.
        if(contentSplit[0].equals("!start")){
            Optional<User> optionalUser = author.asUser();
            if(optionalUser.isEmpty()){
                return;
            }

            if(UserDataBase.containsReply(author.getIdAsString())){
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("你已經註冊過囉~");
                embedBuilder.setDescription("如果註冊上有資料需要更改，請聯繫社團幹部");
                embedBuilder.setTimestampToNow();
                channel.sendMessage(embedBuilder);
                return;
            }

            executeStoryByIndex(message, optionalUser.get(), channel, 1, null);
        }

        if(author.isBotOwner() && contentSplit[0].equals("!catch")){
            String type = contentSplit[1];
            if(type.equals("-ui")){
                String userID = contentSplit[2];
                AnswerObject answerObject = answerMap.get(userID);
                channel.sendMessage(answerObject.getEmbed());
            }else if(type.equals("-si")){
                String studentID = contentSplit[2];
                //search by student ID and find who is this (with black magic or something)
                //make search embed.
            }
        }

    }

    public static void executeStoryByIndex(Message message, User user, TextChannel channel, int index, Map<String, String> parameter){

        if(index == 0) return;

        if(index != 9 && userStoryLoadMap.containsKey(user.getIdAsString())) {
            int userStoryIndex = userStoryLoadMap.get(user.getIdAsString());
            StoryObject object = StoryDatabase.getStoryObjectByIndex(userStoryIndex);
            List<Integer> subNode = getSubAndParentNode(object);
            if (!subNode.contains(index)) {
                return;
            }
        }

        StoryObject storyObject = StoryDatabase.getStoryObjectByIndex(index);
        EmbedBuilder embedBuilder = storyObject.getEmbedBuilder();

        if(parameter != null) {
            String stringMessage = storyObject.getMessage();
            for (Map.Entry<String, String> entry : parameter.entrySet()) {
                stringMessage = stringMessage.replaceAll("\\{" + entry.getKey() + "\\}", entry.getValue());
                stringMessage = stringMessage.replaceAll("\\|", "\n");
            }
            embedBuilder.setDescription(stringMessage);
        }

        channel.sendMessage(embedBuilder);
        userStoryLoadMap.put(user.getIdAsString(), index);
    }

    private static List<Integer> getSubAndParentNode(StoryObject storyObject) {
        List<Integer> list = new ArrayList<>();
        if (storyObject.getType().equals("YN")) {
            YNStoryObject ynStoryObject = (YNStoryObject) storyObject;
            list.add(ynStoryObject.getIfYes());
            list.add(ynStoryObject.getIfNo());
            list.add(ynStoryObject.getReturnStoryIndex());
        } else if (storyObject.getType().equals("TY")) {
            TYStoryObject tyStoryObject = (TYStoryObject) storyObject;
            list.add(tyStoryObject.getNext());
            list.add(tyStoryObject.getReturnStoryIndex());
        } else if (storyObject.getType().equals("NTX")) {
            NTXStoryObject ntxStoryObject = (NTXStoryObject) storyObject;
            list.add(ntxStoryObject.getNext());
            list.add(ntxStoryObject.getReturnStoryIndex());
        }
        return list;
    }
}
