package emily.dcb.event;

import emily.dcb.database.ClubClassDatabase;
import emily.dcb.database.UserDataBase;
import emily.dcb.utils.*;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AdministratorEvent implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent messageCreateEvent) {

        MessageAuthor author = messageCreateEvent.getMessageAuthor();

        if(author.isYourself()) return;
        if(!author.isServerAdmin()) return;

        Optional<User> userOptional = author.asUser();
        Optional<Server> serverOptional = messageCreateEvent.getServer();

        if(userOptional.isEmpty()) return;
        if(serverOptional.isEmpty()) return;

        String content = messageCreateEvent.getMessageContent();
        String[] contentSplit = content.split(" ");
        User user = userOptional.get();
        Server server = serverOptional.get();
        TextChannel channel = messageCreateEvent.getChannel();

        if(!author.isServerAdmin()){
            return;
        }

        if(contentSplit[0].equals("!catch")){
            String type = contentSplit[1];
            String userID;
            if(type.equals("-ui")){
                userID = contentSplit[2];
            }else if(type.equals("-si")){
                String studentID = contentSplit[2];
                if(!UserDataBase.StudentToUID.containsKey(studentID)) {
                    channel.sendMessage(EmbedMessageCreator.errorMessage("找不到這個學號" + studentID + "的資料，可能是這個學號還沒註冊"));
                    return;
                }
                userID = UserDataBase.StudentToUID.get(studentID);
            }else{
                channel.sendMessage(EmbedMessageCreator.errorMessage("無效參數: " + type));
                return;
            }
            if(!UserDataBase.UIDDataObject.containsKey(userID)){
                channel.sendMessage(EmbedMessageCreator.errorMessage("找不到這個discordID" + userID + "的資料，可能是這個discordID還沒註冊或discordID的主人已經退出伺服器了"));
                return;
            }
            System.out.println(userID);
            UserDataObject userDataObject = UserDataBase.UIDDataObject.get(userID);
            channel.sendMessage(userDataObject.getEmbed());
        }else if(contentSplit[0].equals("!showjoin")){
            try {
                String userID = contentSplit[1];
                if(!UserDataBase.UIDDataObject.containsKey(userID)){
                    channel.sendMessage(EmbedMessageCreator.errorMessage("找不到這個ID"));
                    return;
                }
                UserDataObject userDataObject = UserDataBase.UIDDataObject.get(userID);
                String discordID = userDataObject.getReplyByIndex(97).getAnswer();
                String school = userDataObject.getReplyByIndex(3).getAnswer();
                String studentClass = userDataObject.getReplyByIndex(99).getAnswer();
                String studentName = userDataObject.getReplyByIndex(6).getAnswer();
                BufferedImage bufferedImage = PhotoSynthesis.photoSynthesis(discordID, school, studentClass, studentName);
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setImage(bufferedImage);
                LogCreator.info(discordID);
                channel.sendMessage(embedBuilder);
            }catch (IOException e){
                e.printStackTrace();
            }
        }else if(contentSplit[0].equals("!statistics")){
            if(contentSplit[1].equals("class")){
                DefaultPieDataset dataset = new DefaultPieDataset();
                Map<String, Integer> map = new HashMap<>();
                int size = UserDataBase.UIDDataObject.size();
                for(Map.Entry<String, UserDataObject> entry : UserDataBase.UIDDataObject.entrySet()){
                    UserDataObject userDataObject = entry.getValue();
                    ReplyPackage replyPackage = userDataObject.getReplyByIndex(99);
                    if(replyPackage == null || replyPackage.getAnswer().equals("")){
                        map.put("其他", map.getOrDefault("其他", 0)+1);
                    }else{
                        map.put(replyPackage.getAnswer().substring(0, 2), map.getOrDefault(replyPackage.getAnswer().substring(0, 2), 0)+1);
                    }
                }
                for(Map.Entry<String, Integer> entry : map.entrySet()){
                    dataset.setValue(entry.getKey(), entry.getValue());
                }
                JFreeChart chart = ChartFactory.createPieChart("社團會員班級分布", dataset,false,true,false);

                PiePlot plot = (PiePlot) chart.getPlot();
                PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator("{0} = {1}");
                plot.setLabelGenerator(gen);

                StandardChartTheme chartTheme = (StandardChartTheme)org.jfree.chart.StandardChartTheme.createJFreeTheme();
                chartTheme.setSmallFont(new Font("Noto Sans TC", Font.PLAIN, 30));
                chartTheme.setRegularFont(new Font("Noto Sans TC", Font.PLAIN, 30));
                chartTheme.setLargeFont(new Font("Noto Sans TC", Font.PLAIN, 30));
                chartTheme.setExtraLargeFont(new Font("Noto Sans TC", Font.PLAIN, 30));
                chartTheme.apply(chart);

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setImage(chart.createBufferedImage(1920, 1080));
                channel.sendMessage(embedBuilder);
            }
        }

    }
}
