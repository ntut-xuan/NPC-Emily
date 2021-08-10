package emily.dcb.event;

import emily.dcb.database.StoryDatabase;
import emily.dcb.utils.StoryObject;
import emily.dcb.utils.TYStoryObject;
import emily.dcb.utils.YNStoryObject;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.w3c.dom.Text;

import java.util.*;

public class StoryEvent implements MessageCreateListener {

    static Map<String, Integer> userStoryLoadMap = new HashMap<>();

    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        //If message is non-private message, return.
        if(!event.isPrivateMessage()){
            return;
        }

        //Get message.
        Message message = event.getMessage();

        //Get message author.
        MessageAuthor author = message.getAuthor();

        //Do not catch any message from Emily.
        if(author.isYourself()){
            return;
        }

        //Get text channel object
        TextChannel channel = event.getChannel();

        //If user type message "!start", the story will start.
        if(message.getContent().equals("!start")){
            Optional<User> optionalUser = author.asUser();
            if(optionalUser.isEmpty()){
                return;
            }
            executeStoryByIndex(message, optionalUser.get(), channel, 1);
        }

    }

    public static void executeStoryByIndex(Message message, User user, TextChannel channel, int index){

        if(index == 0) return;

        if(userStoryLoadMap.containsKey(user.getIdAsString())) {
            int userStoryIndex = userStoryLoadMap.get(user.getIdAsString());
            StoryObject object = StoryDatabase.getStoryObjectByIndex(userStoryIndex);

            List<Integer> subNode = getSubAndParentNode(object);
            if (!subNode.contains(index)) {
                return;
            }
        }

        message.delete();
        channel.sendMessage(StoryDatabase.getStoryObjectByIndex(index).getEmbedBuilder());
        userStoryLoadMap.put(user.getIdAsString(), index);
    }

    private static List<Integer> getSubAndParentNode(StoryObject storyObject){
        List<Integer> list = new ArrayList<>();
        if(storyObject.getType().equals("YN")){
            YNStoryObject ynStoryObject = (YNStoryObject) storyObject;
            list.add(ynStoryObject.getIfYes());
            list.add(ynStoryObject.getIfNo());
            list.add(ynStoryObject.getReturnStoryIndex());
        }else if(storyObject.getType().equals("TY")){
            TYStoryObject tyStoryObject = (TYStoryObject) storyObject;
            list.add(tyStoryObject.getReturnStoryIndex());
        }
        return list;
    }
}
