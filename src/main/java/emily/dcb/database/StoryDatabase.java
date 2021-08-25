package emily.dcb.database;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.vdurmont.emoji.EmojiParser;
import emily.dcb.utils.*;
import org.apache.xml.serializer.Encodings;
import org.javacord.api.entity.emoji.Emoji;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StoryDatabase {

    static Map<Integer, StoryObject> map = new HashMap<Integer, StoryObject>();

    public static void load() throws IOException {
        String data = "";
        File file = new File("Story.json");
        FileInputStream fileInputStream = new FileInputStream(file);
        Reader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
        Reader buffer = new BufferedReader(reader);
        int r;
        while((r = buffer.read()) != -1){
            data += (char) r;
        }
        JsonElement jsonElement = JsonParser.parseString(data);
        //System.out.println(System.getProperty("user.dir") + "\\" + "story.json");
        JsonObject mainObject = jsonElement.getAsJsonObject();
        JsonArray array = mainObject.get("story").getAsJsonArray();
        int arraySize = array.size();
        //System.out.println(arraySize);
        for(int index = 0; index < arraySize; index++){
            JsonObject storyObject = array.get(index).getAsJsonObject();
            String type = storyObject.get("type").getAsString();
            if(type.equals("YN")){
                int storyIndex = storyObject.get("index").getAsInt();
                String message = storyObject.get("message").getAsString();
                String plainMessage = storyObject.get("plainMessage").getAsString();
                int ifYes = storyObject.get("ifYes").getAsInt();
                int ifNo = storyObject.get("ifNo").getAsInt();
                int ifReturn = storyObject.get("returnStoryIndex").getAsInt();
                String yesEmoji = EmojiParser.parseToUnicode("✅");
                String noEmoji = EmojiParser.parseToUnicode("❎");
                String returnEmoji = EmojiParser.parseToUnicode("↩");
                YNStoryObject YNstoryObject = new YNStoryObject(storyIndex, message, plainMessage, type, yesEmoji, noEmoji, returnEmoji, ifYes, ifNo, ifReturn);
                //System.out.println("load YN-type story object: " + storyIndex + " - " + message);
                map.put(storyIndex, YNstoryObject);
            }else if(type.equals("TY")){
                int storyIndex = storyObject.get("index").getAsInt();
                int ifReturn = storyObject.get("returnStoryIndex").getAsInt();
                String message = storyObject.get("message").getAsString();
                String plainMessage = storyObject.get("plainMessage").getAsString();
                int next = storyObject.get("next").getAsInt();
                String returnEmoji = EmojiParser.parseToUnicode("↩");
                TYStoryObject TYStoryObject = new TYStoryObject(storyIndex, message, plainMessage, type, returnEmoji, ifReturn, next);
                //System.out.println("load TY-type story object: " + storyIndex + " - " + message);
                map.put(storyIndex, TYStoryObject);
            }else if(type.equals("NTX")){
                int storyIndex = storyObject.get("index").getAsInt();
                String message = storyObject.get("message").getAsString();
                int next = storyObject.get("next").getAsInt();
                int ifReturn = storyObject.get("returnStoryIndex").getAsInt();
                NTXStoryObject ntxStoryObject = new NTXStoryObject(message, type, next, storyIndex, ifReturn);
                //System.out.println("load NTX-type story object: " + storyIndex + " - " + message);
                map.put(storyIndex, ntxStoryObject);
            }
        }
        LogCreator.info("讀取到了" + map.size() + "筆故事線");
    }

    public static StoryObject getStoryObjectByIndex(int index){
        return map.get(index);
    }

    public static int getStoryObjectCount(){
        return map.size();
    }
}
