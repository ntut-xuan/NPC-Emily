package emily.dcb.utils;

import org.javacord.api.entity.message.embed.EmbedBuilder;

public abstract class StoryObject {
    String message;
    String type;
    String plainMessage;
    int index;
    public abstract EmbedBuilder getEmbedBuilder();
    public String getMessage() {
        return message;
    }
    public String getType(){
        return type;
    }
    public String getPlainMessage() {
        return plainMessage;
    }
}
