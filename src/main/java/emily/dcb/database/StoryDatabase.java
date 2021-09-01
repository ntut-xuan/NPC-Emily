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
        JsonObject mainObject = jsonElement.getAsJsonObject();
        JsonArray array = mainObject.get("story").getAsJsonArray();
        int arraySize = array.size();
        for(int index = 0; index < arraySize; index++){
            JsonObject storyObject = array.get(index).getAsJsonObject();

            int storyIndex = storyObject.get("index").getAsInt();
            String type = storyObject.get("type").getAsString();
            String message = storyObject.get("message").getAsString();
            int returnStoryIndex = storyObject.get("returnStoryIndex").getAsInt();

            Map<String, Integer> messageTagReplaceIndex = new HashMap<>();

            if(storyObject.get("tag") != null){
                JsonObject tagObject = storyObject.get("tag").getAsJsonObject();
                for(String key : tagObject.keySet()){
                    messageTagReplaceIndex.put(key, tagObject.get(key).getAsInt());
                }
            }

            StoryObject object = null;

            if(type.equals("YN")){
                String plainMessage = storyObject.get("plainMessage").getAsString();
                int ifYes = storyObject.get("ifYes").getAsInt();
                int ifNo = storyObject.get("ifNo").getAsInt();
                object = new YNStoryObject(storyIndex, message, plainMessage, type, ifYes, ifNo, returnStoryIndex, messageTagReplaceIndex);
            }else if(type.equals("TY")){
                String plainMessage = storyObject.get("plainMessage").getAsString();
                int next = storyObject.get("next").getAsInt();
                String returnEmoji = EmojiParser.parseToUnicode("↩");
                object = new TYStoryObject(storyIndex, message, plainMessage, type, returnEmoji, returnStoryIndex, next, messageTagReplaceIndex);
            }else if(type.equals("NTX")){
                int next = storyObject.get("next").getAsInt();
                object = new NTXStoryObject(message, type, next, storyIndex, returnStoryIndex, messageTagReplaceIndex);
            }

            map.put(storyIndex, object);
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
