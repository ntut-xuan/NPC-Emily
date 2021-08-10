package emily.dcb.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.vdurmont.emoji.EmojiParser;
import emily.dcb.utils.StoryObject;
import emily.dcb.utils.TYStoryObject;
import emily.dcb.utils.YNStoryObject;
import org.javacord.api.entity.emoji.Emoji;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StoryDatabase {

    static Map<Integer, StoryObject> map = new HashMap<Integer, StoryObject>();

    public static void load() throws FileNotFoundException {
        String data = "";
        Scanner cin = new Scanner(new File("story.json"));
        while(cin.hasNextLine()){
            data += cin.nextLine() + "\n";
        }
        JsonElement jsonElement = JsonParser.parseString(data);
        JsonObject mainObject = jsonElement.getAsJsonObject();
        JsonArray array = mainObject.get("story").getAsJsonArray();
        int arraySize = array.size();
        System.out.println(arraySize);
        for(int index = 0; index < arraySize; index++){
            JsonObject storyObject = array.get(index).getAsJsonObject();
            String type = storyObject.get("type").getAsString();
            if(type.equals("YN")){
                int storyIndex = storyObject.get("index").getAsInt();
                String message = storyObject.get("message").getAsString();
                int ifYes = storyObject.get("ifYes").getAsInt();
                int ifNo = storyObject.get("ifNo").getAsInt();
                int ifReturn = storyObject.get("returnStoryIndex").getAsInt();
                String yesEmoji = EmojiParser.parseToUnicode("✅");
                String noEmoji = EmojiParser.parseToUnicode("❎");
                String returnEmoji = EmojiParser.parseToUnicode("↩");
                YNStoryObject YNstoryObject = new YNStoryObject(storyIndex, message, type, yesEmoji, noEmoji, returnEmoji, ifYes, ifNo, ifReturn);
                System.out.println("load YN-type story object: " + storyIndex + " - " + message);
                map.put(storyIndex, YNstoryObject);
            }else if(type.equals("TY")){
                int storyIndex = storyObject.get("index").getAsInt();
                int ifReturn = storyObject.get("returnStoryIndex").getAsInt();
                String message = storyObject.get("message").getAsString();
                String returnEmoji = EmojiParser.parseToUnicode("↩");
                TYStoryObject TYStoryObject = new TYStoryObject(storyIndex, message, type, returnEmoji, ifReturn);
                System.out.println("load TY-type story object: " + storyIndex + " - " + message);
                map.put(storyIndex, TYStoryObject);
            }
        }
    }

    public static StoryObject getStoryObjectByIndex(int index){
        return map.get(index);
    }
}
