package emily.dcb.event;

import emily.dcb.utils.AnswerObject;
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
            if(type.equals("-ui")){
                String userID = contentSplit[2];
                AnswerObject answerObject = StoryEvent.answerMap.get(userID);
                channel.sendMessage(answerObject.getEmbed());
            }else if(type.equals("-si")){
                String studentID = contentSplit[2];
                //search by student ID and find who is this (with black magic or something)
                //make search embed.
            }
        }

    }
}
