package emily.dcb.utils;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.util.Map;

public class TYStoryObject extends StoryObject{

    String returnEmoji;
    int next;

    public TYStoryObject(int index, String message, String plainMessage, String type, String returnEmoji, int returnStoryIndex, int next, Map<String, Integer> messageTagReplaceIndex){
        this.index = index;
        this.plainMessage = plainMessage;
        this.message = message;
        this.type = type;
        this.returnEmoji = returnEmoji;
        this.returnStoryIndex = returnStoryIndex;
        this.next = next;
        this.messageTagReplaceIndex = messageTagReplaceIndex;
    }

    public String getReturnEmoji(){
        return returnEmoji;
    }

    public int getNext(){
        return next;
    }

}
