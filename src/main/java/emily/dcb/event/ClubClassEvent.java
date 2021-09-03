package emily.dcb.event;

import emily.dcb.database.ClubClassDatabase;
import emily.dcb.utils.ClubClass;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.SelectMenuBuilder;
import org.javacord.api.entity.message.component.SelectMenuOptionBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClubClassEvent implements MessageCreateListener {

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {

        TextChannel textChannel = messageCreateEvent.getChannel();
        String content = messageCreateEvent.getMessageContent();

        assert messageCreateEvent.getMessageAuthor().isYourself();

        if(content.equals("!社課")){
            SelectMenuBuilder selectMenuBuilder = new SelectMenuBuilder();
            for(ClubClass clubClass : ClubClassDatabase.clubClassList){
                String dateFormat = clubClass.getSchedule().toString(DateTimeFormat.forPattern("yyyy/MM/dd HH:mm"));
                SelectMenuOptionBuilder selectMenuOptionBuilder = new SelectMenuOptionBuilder();
                selectMenuOptionBuilder.setLabel(clubClass.getClassName());
                selectMenuOptionBuilder.setDescription(clubClass.getClassDescription() + " - " + dateFormat);
                selectMenuOptionBuilder.setValue(clubClass.getClassName());
                selectMenuBuilder.addOption(selectMenuOptionBuilder.build());
            }
            selectMenuBuilder.setCustomId("clubclass");
            selectMenuBuilder.setPlaceholder("已經預訂好ㄉ社課列表");
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder = messageBuilder.setContent("這是已經預訂好的社課列表喔(Beta, 資料皆假)");
            messageBuilder = messageBuilder.addComponents(ActionRow.of(selectMenuBuilder.build()));
            messageBuilder.send(textChannel);
        }
    }
}
