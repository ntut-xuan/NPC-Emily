package emily.dcb.event;

import emily.dcb.database.ClubClassDatabase;
import emily.dcb.database.UserDataBase;
import emily.dcb.main.Main;
import emily.dcb.utils.ClubClass;
import emily.dcb.utils.EmbedMessageCreator;
import emily.dcb.utils.UserDataObject;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.InteractionCreateEvent;
import org.javacord.api.event.interaction.SelectMenuChooseEvent;
import org.javacord.api.interaction.Interaction;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SelectMenuInteraction;
import org.javacord.api.listener.interaction.SelectMenuChooseListener;

import java.util.HashMap;
import java.util.Map;

public class ClubClassSelectMenuInteractionEvent implements SelectMenuChooseListener {

    public static Map<User, ClubClass> draftMap = new HashMap<>();

    @Override
    public void onSelectMenuChoose(SelectMenuChooseEvent selectMenuChooseEvent) {

        Interaction interaction = selectMenuChooseEvent.getInteraction();

        SelectMenuInteraction selectMenuInteraction = selectMenuChooseEvent.getSelectMenuInteraction();

        assert selectMenuInteraction.getCustomId().equals("clubclass");
        assert selectMenuInteraction.getChosenOptions().size() != 0;

        String clubclassName = selectMenuInteraction.getChosenOptions().get(0).getValue();

        ClubClass clubClass = null;
        for(ClubClass cc : ClubClassDatabase.clubClassList){
            if(cc.getClassName().equals(clubclassName)){
                clubClass = cc;
            }
        }

        User user = selectMenuChooseEvent.getSelectMenuInteraction().getUser();

        assert clubClass != null;

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder = messageBuilder.setEmbed(EmbedMessageCreator.clubClassRegisterConfirmMessage(clubClass));
        messageBuilder = messageBuilder.addComponents(ActionRow.of(Button.success("accept", "確定註冊"), Button.danger("denied", "放棄註冊")));
        messageBuilder.send(user);

        draftMap.put(user, clubClass);

        selectMenuInteraction.acknowledge();

    }
}
