package emily.dcb.event;

import emily.dcb.database.EmilySettingDatabase;
import emily.dcb.database.StoryDatabase;
import emily.dcb.database.UserDataBase;
import emily.dcb.exception.StudentIDNotFoundException;
import emily.dcb.utils.*;
import org.apache.commons.lang3.tuple.Pair;
import org.javacord.api.entity.channel.PrivateChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.embed.EmbedFooter;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

public class NTXEvent implements MessageCreateListener {

    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        //If message is non-private message, return.
        if(!messageCreateEvent.isPrivateMessage()){
            return;
        }

        //Get message.
        Message message = messageCreateEvent.getMessage();

        //Get message content.
        String content = message.getContent();

        //If it's necessary, you can split message by space, and access this array.
        String[] contentSplit = content.split(" ");

        //Get message author.
        MessageAuthor author = message.getAuthor();

        //Do not catch any message from Emily.
        if(!author.isYourself()){
            return;
        }

        Optional<User> userOptional = author.asUser();

        if(userOptional.isEmpty()){
            return;
        }

        User emily = userOptional.get();

        Optional<PrivateChannel> channelOptional = messageCreateEvent.getChannel().asPrivateChannel();

        if(channelOptional.isEmpty()){
            return;
        }

        PrivateChannel privateChannel = channelOptional.get();

        Optional<User> recipientOptional = privateChannel.getRecipient();

        if(recipientOptional.isEmpty()){
            return;
        }

        User receipient = recipientOptional.get();

        long ID = privateChannel.getRecipientId().get();

        if(message.getEmbeds().size() == 0) return;

        Embed embed = message.getEmbeds().get(0);

        Optional<EmbedFooter> footerOptional = embed.getFooter();

        if(footerOptional.isEmpty()) return;

        EmbedFooter embedFooter = footerOptional.orElse(null);
        Optional<String> optionalString = embedFooter.getText();

        if(optionalString.isEmpty()) return;

        String footerText = optionalString.get();

        int storyID = Integer.parseInt(footerText.split("-")[0]);
        String storyType = footerText.split("-")[1];

        if(!storyType.equals("NTX")) return;

        if(storyID == 4){
            NTXStoryObject ntxStoryObject = (NTXStoryObject) StoryDatabase.getStoryObjectByIndex(4);
            try {
                message.getChannel().type();
                UserDataObject userDataObject = StoryEvent.answerMap.get(String.valueOf(ID));
                String studentID = userDataObject.getReplyByIndex(2).getAnswer();
                StudentInfoCrawler sic = new StudentInfoCrawler();
                Pair<String, String> studentInfo = sic.getStudentNameAndClass(studentID);
                userDataObject.setReplyByIndex(3, new ReplyPackage("學校", SchoolAbbrTableCrawler.map.get("NTUT")));
                userDataObject.setReplyByIndex(97, new ReplyPackage("DiscordTag", receipient.getDiscriminatedName()));
                userDataObject.setReplyByIndex(6, new ReplyPackage("名子", studentInfo.getLeft()));
                userDataObject.setReplyByIndex(99, new ReplyPackage("班級", studentInfo.getRight()));
                StoryEvent.executeStoryByIndex(null, receipient, privateChannel, ntxStoryObject.getNext());
            } catch (StudentIDNotFoundException e) {
                privateChannel.sendMessage(EmbedMessageCreator.errorMessage("找不到這個學號，請確認拼字有沒有錯誤QQ"));
                StoryEvent.userStoryLoadMap.put(receipient.getIdAsString(), ntxStoryObject.getReturnStoryIndex());
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        if(storyID == 5){
            try {
                /* save file */
                UserDataBase.saveReply(String.valueOf(ID));

                /* send welcome photo and message */
                UserDataObject userDataObject = StoryEvent.answerMap.get(String.valueOf(ID));
                String school = "國立臺北科技大學";
                String discordTag = userDataObject.getReplyByIndex(97).getAnswer();
                String name = userDataObject.getReplyByIndex(6).getAnswer();
                String studentClass = userDataObject.getReplyByIndex(99).getAnswer();
                BufferedImage bufferedImage = PhotoSynthesis.photoSynthesis(discordTag, "國立臺北科技大學", studentClass, name);
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.magenta);
                embedBuilder.setImage(bufferedImage);
                EmilySettingDatabase.welcomeChannel.sendMessage("歡迎 " + receipient.getMentionTag() + "成為北科程式設計社的會員!");
                EmilySettingDatabase.welcomeChannel.sendMessage(embedBuilder);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        if(storyID == 8){
            try {
                /* save file */
                UserDataBase.saveReply(String.valueOf(ID));

                /* send welcome photo and message */
                UserDataObject userDataObject = StoryEvent.answerMap.get(String.valueOf(ID));
                String discordTag = userDataObject.getReplyByIndex(97).getAnswer();
                String name = userDataObject.getReplyByIndex(6).getAnswer();
                String school = userDataObject.getReplyByIndex(3).getAnswer();
                BufferedImage bufferedImage = PhotoSynthesis.photoSynthesis(discordTag, school, "", name);
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.magenta);
                embedBuilder.setImage(bufferedImage);
                EmilySettingDatabase.welcomeChannel.sendMessage("歡迎 " + receipient.getMentionTag() + " 成為北科程式設計社的會員!");
                EmilySettingDatabase.welcomeChannel.sendMessage(embedBuilder);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
