package emily.dcb.main;

import emily.dcb.event.AddReactionEvent;
import emily.dcb.event.ClickReactionEvent;
import emily.dcb.event.StoryEvent;
import org.javacord.api.DiscordApi;

public class RegisterEvent {

    public RegisterEvent(DiscordApi api){
        api.addMessageCreateListener(new StoryEvent());
        api.addMessageCreateListener(new AddReactionEvent());
        api.addReactionAddListener(new ClickReactionEvent());
    }

}
