package emily.dcb.event;

import emily.dcb.database.EmilySettingDatabase;
import emily.dcb.main.Main;
import emily.dcb.utils.EmbedMessageCreator;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.nio.channels.Channel;
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
        String[] splitContent = content.split(" ");
        User user = userOptional.get();
        Server server = serverOptional.get();
        TextChannel channel = messageCreateEvent.getChannel();


    }
}
