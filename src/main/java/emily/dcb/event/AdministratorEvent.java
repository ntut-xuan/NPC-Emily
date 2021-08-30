package emily.dcb.event;

import emily.dcb.database.UserDataBase;
import emily.dcb.utils.AnswerObject;
import emily.dcb.utils.EmbedMessageCreator;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.Optional;

public class AdministratorEvent implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {

        MessageAuthor author = messageCreateEvent.getMessageAuthor();

        if(author.isYourself()) return;
        if(!author.isServerAdmin()) return;

        Optional<User> userOptional = author.asUser();
        Optional<Server> serverOptional = messageCreateEvent.getServer();

        if(userOptional.isEmpty()) return;
        if(serverOptional.isEmpty()) return;

        String content = messageCreateEvent.getMessageContent();
        String[] contentSplit = content.split(" ");
        User user = userOptional.get();
        Server server = serverOptional.get();
        TextChannel channel = messageCreateEvent.getChannel();

        if(author.isBotOwner() && contentSplit[0].equals("!catch")){
            String type = contentSplit[1];
            String userID;
            if(type.equals("-ui")){
                userID = contentSplit[2];
            }else if(type.equals("-si")){
                String studentID = contentSplit[2];
                if(!UserDataBase.StudentToUID.containsKey(studentID)) {
                    channel.sendMessage(EmbedMessageCreator.errorMessage("找不到這個學號" + studentID + "的資料，可能是這個學號還沒註冊"));
                    return;
                }
                userID = UserDataBase.StudentToUID.get(studentID);
            }else{
                channel.sendMessage(EmbedMessageCreator.errorMessage("無效參數: " + type));
                return;
            }
            if(!UserDataBase.UIDAnswerObject.containsKey(userID)){
                channel.sendMessage(EmbedMessageCreator.errorMessage("找不到這個discordID" + userID + "的資料，可能是這個discordID還沒註冊或discordID的主人已經退出伺服器了"));
                return;
            }
            System.out.println(userID);
            AnswerObject answerObject = UserDataBase.UIDAnswerObject.get(userID);
            channel.sendMessage(answerObject.getEmbed());
        }

    }
}
