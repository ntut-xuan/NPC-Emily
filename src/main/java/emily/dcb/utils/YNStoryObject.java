package emily.dcb.utils;

import com.vdurmont.emoji.EmojiParser;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import java.net.URL;
import java.util.Map;

public class YNStoryObject extends StoryObject{

    String yesEmoji = EmojiParser.parseToUnicode("✅");
    String noEmoji = EmojiParser.parseToUnicode("❎");
    String returnEmoji = EmojiParser.parseToUnicode("↩");
    int ifYes;
    int ifNo;

    public YNStoryObject(int index, String message, String plainMessage, String type, int ifYes, int ifNo, int returnStoryIndex, Map<String, Integer> messageTagReplaceIndex){
        this.plainMessage = plainMessage;
        this.message = message;
        this.type = type;
        this.index = index;
        this.ifNo = ifNo;
        this.ifYes = ifYes;
        this.returnStoryIndex = returnStoryIndex;
        this.messageTagReplaceIndex = messageTagReplaceIndex;
    }

    public int getIfYes(){
        return ifYes;
    }

    public int getIfNo(){
        return ifNo;
    }

    public String getPlainMessage(){
        return plainMessage;
    }

    public String getYesEmoji(){
        return yesEmoji;
    }

    public String getNoEmoji(){
        return noEmoji;
    }

    public String getReturnEmoji(){
        return returnEmoji;
    }

}
