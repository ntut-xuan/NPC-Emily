package emily.dcb.utils;

import emily.dcb.event.StoryEvent;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.util.Map;

public class NTXStoryObject extends StoryObject{

    int next;

    public NTXStoryObject(String message, String type, int next, int index, int returnIndex, Map<String, Integer> messageTagReplaceIndex){
        this.message = message;
        this.next = next;
        this.index = index;
        this.type = type;
        this.returnStoryIndex = returnIndex;
        this.messageTagReplaceIndex = messageTagReplaceIndex;
    }

    public int getNext(){
        return next;
    }
}
