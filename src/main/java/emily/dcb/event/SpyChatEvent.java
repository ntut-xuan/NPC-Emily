package emily.dcb.event;

import emily.dcb.utils.LogCreator;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class SpyChatEvent implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        if(!messageCreateEvent.isPrivateMessage()) return;
        if(messageCreateEvent.getMessageAuthor().isYourself()) return;
        LogCreator.chat(messageCreateEvent.getMessageAuthor().asUser().get(), messageCreateEvent.getMessage().getContent());
    }
}
