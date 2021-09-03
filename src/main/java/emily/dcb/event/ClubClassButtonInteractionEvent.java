package emily.dcb.event;

import emily.dcb.database.UserDataBase;
import emily.dcb.utils.ClubClass;
import emily.dcb.utils.EmbedMessageCreator;
import emily.dcb.utils.UserDataObject;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.MessageComponentCreateEvent;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.listener.interaction.MessageComponentCreateListener;

public class ClubClassButtonInteractionEvent implements MessageComponentCreateListener {

    @Override
    public void onComponentCreate(MessageComponentCreateEvent messageComponentCreateEvent) {
        MessageComponentInteraction messageComponentInteraction = messageComponentCreateEvent.getMessageComponentInteraction();
        String customId = messageComponentInteraction.getCustomId();
        User user = messageComponentInteraction.getUser();

        if(!(messageComponentInteraction.getCustomId().equals("accept") || messageComponentInteraction.getCustomId().equals("denied"))){
            return;
        }
        if(messageComponentInteraction.getMessage().isEmpty()){
            return;
        }

        ClubClass clubClass = ClubClassSelectMenuInteractionEvent.draftMap.get(user);


        if(customId.equals("accept")){
            UserDataObject userDataObject = UserDataBase.UIDDataObject.get(user.getIdAsString());
            userDataObject.regiseredClubClass(clubClass);
            UserDataBase.UIDDataObject.put(user.getIdAsString(), userDataObject);
            user.sendMessage(EmbedMessageCreator.successMessage("社課註冊完成囉!"));
        }else{
            ClubClassSelectMenuInteractionEvent.draftMap.remove(user);
        }

        messageComponentInteraction.getMessage().get().delete();
        messageComponentInteraction.acknowledge();
    }
}
