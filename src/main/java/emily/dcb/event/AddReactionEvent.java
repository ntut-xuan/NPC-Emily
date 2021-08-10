package emily.dcb.event;

import emily.dcb.database.StoryDatabase;
import emily.dcb.utils.TYStoryObject;
import emily.dcb.utils.YNStoryObject;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.message.embed.EmbedFooter;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.Optional;

public class AddReactionEvent implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent event) {

        MessageAuthor author = event.getMessageAuthor();

        if(!author.isYourself()){
            return;
        }

        Message message = event.getMessage();
        Embed embed = message.getEmbeds().get(0);
        Optional<EmbedFooter> embedFooterOptional = embed.getFooter();

        if(embedFooterOptional.isEmpty()){
            return;
        }

        EmbedFooter footer = embedFooterOptional.get();
        Optional<String> footerText = footer.getText();

        if(footerText.isEmpty()){
            return;
        }

        String text = footerText.get();
        if(text.split("-")[1].equals("YN")){
            int index = Integer.parseInt(text.split("-")[0]);
            YNStoryObject ynStoryObject = (YNStoryObject) StoryDatabase.getStoryObjectByIndex(index);
            message.addReactions(ynStoryObject.getYesEmoji());
            message.addReactions(ynStoryObject.getNoEmoji());
            message.addReactions(ynStoryObject.getReturnEmoji());
        }else if(text.split("-")[1].equals("TY")){
            int index = Integer.parseInt(text.split("-")[0]);
            TYStoryObject tyStoryObject = (TYStoryObject) StoryDatabase.getStoryObjectByIndex(index);
            message.addReactions(tyStoryObject.getReturnEmoji());
        }

    }
}
