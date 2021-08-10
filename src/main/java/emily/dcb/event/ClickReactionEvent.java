package emily.dcb.event;

import emily.dcb.database.StoryDatabase;
import emily.dcb.utils.StoryObject;
import emily.dcb.utils.TYStoryObject;
import emily.dcb.utils.YNStoryObject;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.Reaction;
import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.message.embed.EmbedFooter;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.listener.message.reaction.ReactionAddListener;

import java.util.Optional;

public class ClickReactionEvent implements ReactionAddListener {
    @Override
    public void onReactionAdd(ReactionAddEvent reactionAddEvent) {

//        System.out.println("clicked");

        TextChannel textChannel = reactionAddEvent.getChannel();
        Optional<User> optionalUser = reactionAddEvent.getUser();

        if(optionalUser.isEmpty()){
//            System.out.println("user is empty, return");
            return;
        }

        User user = optionalUser.get();

        if(user.isYourself()){
//            System.out.println("user is yourself, return");
            return;
        }

        Optional<Message> optionalMessage = reactionAddEvent.getMessage();

        if(optionalMessage.isEmpty()){
//            System.out.println("message is empty, return");
            return;
        }

        Message message = optionalMessage.get();
        MessageAuthor author = message.getAuthor();

        if(!author.isYourself()){
//            System.out.println("author is yourself, return");
            return;
        }

        Optional<Reaction> optionalReaction = reactionAddEvent.getReaction();

        if(optionalReaction.isEmpty()){
//            System.out.println("no reaction, return");
            return;
        }

        Reaction reaction = optionalReaction.get();
        Emoji emoji = reaction.getEmoji();
        String emojiString = emoji.asUnicodeEmoji().get();

        Embed embed = message.getEmbeds().get(0);
        Optional<EmbedFooter> embedFooterOptional = embed.getFooter();

        if(embedFooterOptional.isEmpty()){
//            System.out.println("no embed footer, return");
            return;
        }

        EmbedFooter footer = embedFooterOptional.get();
        Optional<String> footerText = footer.getText();


        if(footerText.isEmpty()){
//            System.out.println("no footer text, return");
            return;
        }

        String text = footerText.get();
        if(text.split("-")[1].equals("YN")){
            int index = Integer.parseInt(text.split("-")[0]);
            YNStoryObject object = (YNStoryObject) StoryDatabase.getStoryObjectByIndex(index);
            if(emojiString.equals("✅")){
                StoryEvent.executeStoryByIndex(message, user, textChannel, object.getIfYes());
            }else if(emojiString.equals("❎")){
                StoryEvent.executeStoryByIndex(message, user, textChannel, object.getIfNo());
            }else if(emojiString.equals("↩")){
                StoryEvent.executeStoryByIndex(message, user, textChannel, object.getReturnStoryIndex());
            }
        }else if(text.split("-")[1].equals("TY")){
            int index = Integer.parseInt(text.split("-")[0]);
            TYStoryObject object = (TYStoryObject) StoryDatabase.getStoryObjectByIndex(index);
            if(emojiString.equals("↩")){
                StoryEvent.executeStoryByIndex(message, user, textChannel, object.getReturnStoryIndex());
            }
        }
    }
}
