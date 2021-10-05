package emily.dcb.database;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import emily.dcb.main.Main;
import emily.dcb.utils.LogCreator;
import org.apache.commons.logging.Log;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.w3c.dom.Text;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class EmilySettingDatabase {

    public static String token;
    public static Server server;
    public static TextChannel welcomeChannel;
    public static Role memberRole;
    public static Role clubMemberRole;

    public static void load(boolean tokenOnly) {

        try {

            File file = new File("EmilySetting.json");

            /* check file exist */
            if (!file.exists()) {

                file.createNewFile();
                LogCreator.info("由於EmilySetting.json不存在，所以創立一個");

                JsonObject object = new JsonObject();
                object.addProperty("token", "");
                object.addProperty("serverID", "");
                object.addProperty("welcomeChannelID", "");
                object.addProperty("memberRoleID", "");

                PrintWriter printWriter = new PrintWriter(file);
                printWriter.println(object.toString());
                printWriter.close();

            }

            /* load file character-by-character */
            String jsonFile = "";
            FileInputStream fileInputStream = new FileInputStream(file);
            Reader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            Reader buffer = new BufferedReader(reader);
            int r;
            while ((r = buffer.read()) != -1) {
                jsonFile += (char) r;
            }
            reader.close();
            buffer.close();
            fileInputStream.close();

            /* Parse text into Json object */
            JsonElement element = JsonParser.parseString(jsonFile);
            JsonObject object = element.getAsJsonObject();

            if(tokenOnly){
                token = object.get("token").getAsString();
                return;
            }

            if (object.get("serverID").getAsString().equals("")) {
                LogCreator.error("檢測狀態：serverID尚未設置，請設置才能得到完整功能");
                return;
            }

            String serverID = object.get("serverID").getAsString();

            if (object.get("welcomeChannelID").getAsString().equals("")) {
                LogCreator.error("檢測狀態：welcomeChannelID尚未設置，請設置才能得到完整功能");
                return;
            }

            String welcomeChannelID = object.get("welcomeChannelID").getAsString();

            if (object.get("memberRoleID").getAsString().equals("")){
                LogCreator.error("檢測狀態：memberRoleID尚未設置，請設置才能得到完整功能");
                return;
            }

            String memberRoleID = object.get("memberRoleID").getAsString();

            Optional<Server> serverOptional = Main.discordApi.getServerById(serverID);

            if(serverOptional.isEmpty()){
                LogCreator.error("檢測狀態：伺服器不存在，因此發生錯誤");
                return;
            }

            server = serverOptional.get();

            if (object.get("clubMemberRoleID").getAsString().equals("")){
                LogCreator.error("檢測狀態：clubMemberRoleID尚未設置，請設置才能得到完整功能");
                return;
            }

            String clubMemberRoleID = object.get("clubMemberRoleID").getAsString();


            Optional<ServerTextChannel> channelOptional = server.getTextChannelById(welcomeChannelID);

            if(channelOptional.isEmpty()){
                LogCreator.error("檢測狀態：歡迎頻道不存在，因此發生錯誤");
                return;
            }

            welcomeChannel = channelOptional.get();

            Optional<Role> memberRoleOptional = server.getRoleById(memberRoleID);

            if(memberRoleOptional.isEmpty()){
                LogCreator.error("檢測狀態：會員身分組不存在，因此發生錯誤");
                return;
            }

            memberRole = memberRoleOptional.get();

            Optional<Role> clubMemberRoleOptional = server.getRoleById(clubMemberRoleID);

            if(clubMemberRoleOptional.isEmpty()){
                LogCreator.error("檢測狀態：社員身分組不存在，因此發生錯誤");
                return;
            }

            clubMemberRole = clubMemberRoleOptional.get();

            LogCreator.info("成功讀取EmilySetting.json");

        }catch (IOException e){
            LogCreator.error("EmilySetting.json檔案發生IO問題，請聯繫bot作者");
        }catch (NullPointerException e){
            LogCreator.error("檔案狀況：EmilySetting.json檔案已損毀，請刪除檔案後再次重開bot");
        }
    }
}
