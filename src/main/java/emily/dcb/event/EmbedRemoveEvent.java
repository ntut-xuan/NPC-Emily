package emily.dcb.event;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.Reaction;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.listener.message.reaction.ReactionAddListener;

import java.util.Optional;

public class EmbedRemoveEvent implements ReactionAddListener {

    @Override
    public void onReactionAdd(ReactionAddEvent reactionAddEvent) {
        TextChannel textChannel = reactionAddEvent.getChannel();
        Optional<User> optionalUser = reactionAddEvent.getUser();

        if(optionalUser.isEmpty()){
            return;
        }

        User user = optionalUser.get();

        if(user.isYourself()){
            return;
        }

        Optional<Message> optionalMessage = reactionAddEvent.getMessage();

        if(optionalMessage.isEmpty()){
            return;
        }

        Message message = optionalMessage.get();
        MessageAuthor author = message.getAuthor();

        if(!author.isYourself()){
            return;
        }

        if(!author.getIdAsString().equals(reactionAddEvent.getUserIdAsString()) && !author.isServerAdmin()){
            return;
        }

        Optional<Reaction> optionalReaction = reactionAddEvent.getReaction();

        if(optionalReaction.isEmpty()){
            return;
        }

        Reaction reaction = optionalReaction.get();
        Emoji emoji = reaction.getEmoji();
        String emojiString = emoji.asUnicodeEmoji().get();

        if(emojiString.equals("❌")){
            message.delete();
        }
    }
}
