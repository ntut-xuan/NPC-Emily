package emily.dcb.event;

import emily.dcb.utils.EmbedMessageCreator;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class ScheduleEvent implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {
        TextChannel textChannel = messageCreateEvent.getChannel();
        String content = messageCreateEvent.getMessageContent();
        MessageAuthor author = messageCreateEvent.getMessageAuthor();

        if(author.isYourself()){
            return;
        }

        Optional<User> userOptional = messageCreateEvent.getMessageAuthor().asUser();

        if(userOptional.isEmpty()){
            return;
        }

        User user = userOptional.get();

        if(content.equals("!ss")){
            try {
                File file = new File("Schedule.txt");
                if(!file.exists()){
                    file.createNewFile();
                }
                Scanner cin = new Scanner(file);
                Map<Integer, Integer> plannedDay = new HashMap<>();
                Map<DateTime, String> eventMap = new HashMap<>();
                DateTime now = DateTime.now();
                while(cin.hasNextLine()){
                    String line = cin.nextLine();
                    String[] split = line.split(" ");
                    String date = split[0];
                    String time = split[1];
                    String event = split[2];
                    DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm");
                    DateTime dateTime = DateTime.parse(date + " " + time, dateTimeFormatter);
                    if(dateTime.getMonthOfYear() != now.getMonthOfYear()){
                        continue;
                    }
                    plannedDay.put(dateTime.getDayOfMonth(), 1);
                    eventMap.put(dateTime, event);
                }
                EmbedBuilder embedBuilder = EmbedMessageCreator.calendarFormatEmbed(plannedDay);
                embedBuilder.setTitle(now.getMonthOfYear() + "月行程表");
                if(eventMap.size() > 0) {
                    for (Map.Entry<DateTime, String> entry : eventMap.entrySet()) {
                        String dateFormat = entry.getKey().toString(DateTimeFormat.forPattern("yyyy/MM/dd HH:mm"));
                        embedBuilder.addField(dateFormat, entry.getValue());
                    }
                }else{
                    embedBuilder.addField("暫無行程", "QQ");
                }
                embedBuilder.setColor(Color.CYAN);
                embedBuilder.setFooter(user.getName() + " " + user.getMentionTag(), user.getAvatar());
                messageCreateEvent.getMessage().delete();
                textChannel.sendMessage(embedBuilder);
                cin.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
