package emily.dcb.event;

import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import emily.dcb.database.UserDataBase;
import emily.dcb.main.GoogleSheetsLoader;
import emily.dcb.utils.ClubClass;
import emily.dcb.utils.EmbedMessageCreator;
import emily.dcb.utils.UserDataObject;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.interaction.MessageComponentCreateEvent;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.listener.interaction.MessageComponentCreateListener;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

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
            /* add result into google spreadsheet */
            try {
                DateTime dateTime = DateTime.now();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String dateFormat = sdf.format(dateTime.toDate());
                ValueRange valueRange = new ValueRange();
                String isNTUTStudent = userDataObject.getReplyByIndex(1).getAnswer();
                String studentName = userDataObject.getReplyByIndex(6).getAnswer();
                String discordID = userDataObject.getReplyByIndex(97).getAnswer();
                String studentIDorSchool = userDataObject.getReplyByIndex(isNTUTStudent.equals("No") ? 3 : 2).getAnswer();
                valueRange.setValues(List.of(Arrays.asList(dateFormat, studentName, isNTUTStudent, discordID, studentIDorSchool, "", "v")));
                AppendValuesResponse appendResult = GoogleSheetsLoader.sheets.spreadsheets().values()
                        .append(GoogleSheetsLoader.spreadSheetID, "great", valueRange)
                        .setValueInputOption("USER_ENTERED")
                        .setInsertDataOption("INSERT_ROWS")
                        .setIncludeValuesInResponse(true)
                        .execute();
            /* done */
            }catch (Exception e){
                e.printStackTrace();
            }
            user.sendMessage(EmbedMessageCreator.successMessage("社課註冊完成囉!"));
        }else{
            ClubClassSelectMenuInteractionEvent.draftMap.remove(user);
        }

        messageComponentInteraction.getMessage().get().delete();
        messageComponentInteraction.acknowledge();
    }
}
