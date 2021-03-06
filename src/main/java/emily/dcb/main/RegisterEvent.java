package emily.dcb.main;

import emily.dcb.event.*;
import org.javacord.api.DiscordApi;

public class RegisterEvent {

    public RegisterEvent(DiscordApi api){
        api.addMessageCreateListener(new StoryEvent());
        api.addMessageCreateListener(new AddReactionEvent());
        api.addReactionAddListener(new ClickReactionEvent());
        api.addReactionAddListener(new EmbedRemoveEvent());
        api.addMessageCreateListener(new TextAnswerEvent());
        api.addMessageCreateListener(new NTXEvent());
        api.addMessageCreateListener(new SpyChatEvent());
        api.addMessageCreateListener(new AdministratorEvent());
        api.addMessageCreateListener(new DebugEvent());
        api.addMessageCreateListener(new ClubClassEvent());
        api.addMessageCreateListener(new ScheduleEvent());
        api.addSelectMenuChooseListener(new ClubClassSelectMenuInteractionEvent());
        api.addMessageComponentCreateListener(new ClubClassButtonInteractionEvent());
    }

}
