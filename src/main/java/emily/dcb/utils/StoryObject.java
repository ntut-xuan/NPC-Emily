package emily.dcb.utils;

import org.javacord.api.entity.message.embed.EmbedBuilder;

public abstract class StoryObject {
    String message;
    String type;
    int index;
    public abstract EmbedBuilder getEmbedBuilder();
    public String getType(){
        return type;
    }
}
