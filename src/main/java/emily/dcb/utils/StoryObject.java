package emily.dcb.utils;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.util.HashMap;
import java.util.Map;

public abstract class StoryObject {

    String message;
    String type;
    String plainMessage;
    int index;
    int returnStoryIndex;

    Map<String, Integer> messageTagReplaceIndex = new HashMap<>();

    public String getMessage() {
        return message;
    }
    public String getType(){
        return type;
    }
    public String getPlainMessage() {
        return plainMessage;
    }
    public int getReturnStoryIndex(){
        return returnStoryIndex;
    }
    public EmbedBuilder getEmbedBuilder(User user) {
        return EmbedMessageCreator.storyMessage(index, type, message, messageTagReplaceIndex, user);
    }
}
