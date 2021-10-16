package emily.dcb.utils;

import com.ibm.icu.text.Transliterator;
import emily.dcb.event.StoryEvent;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmbedMessageCreator {

    public static EmbedBuilder errorMessage(String errorMessage){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Emily", "", "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/8efad515-12d3-4a4d-8f77-0eaeee12fb0f/d8sk8k0-00e546ba-db9c-48eb-9f7b-0652db666384.png/v1/fill/w_743,h_1076,strp/anime_pink_hair_girl_render_by_meilichan15_d8sk8k0-pre.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9MTE1OSIsInBhdGgiOiJcL2ZcLzhlZmFkNTE1LTEyZDMtNGE0ZC04Zjc3LTBlYWVlZTEyZmIwZlwvZDhzazhrMC0wMGU1NDZiYS1kYjljLTQ4ZWItOWY3Yi0wNjUyZGI2NjYzODQucG5nIiwid2lkdGgiOiI8PTgwMCJ9XV0sImF1ZCI6WyJ1cm46c2VydmljZTppbWFnZS5vcGVyYXRpb25zIl19.zkcwS2SIdcXJdYYY9ftU4khFEU69a8J6MQopfswQlsU");
        embedBuilder.setTitle("喔不，好像發生了一點問題");
        embedBuilder.setDescription(errorMessage.replaceAll("\\|", "\n"));
        embedBuilder.setColor(Color.red);
        embedBuilder.setTimestampToNow();
        return embedBuilder;
    }

    public static EmbedBuilder successMessage(String errorMessage){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Emily", "", "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/8efad515-12d3-4a4d-8f77-0eaeee12fb0f/d8sk8k0-00e546ba-db9c-48eb-9f7b-0652db666384.png/v1/fill/w_743,h_1076,strp/anime_pink_hair_girl_render_by_meilichan15_d8sk8k0-pre.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9MTE1OSIsInBhdGgiOiJcL2ZcLzhlZmFkNTE1LTEyZDMtNGE0ZC04Zjc3LTBlYWVlZTEyZmIwZlwvZDhzazhrMC0wMGU1NDZiYS1kYjljLTQ4ZWItOWY3Yi0wNjUyZGI2NjYzODQucG5nIiwid2lkdGgiOiI8PTgwMCJ9XV0sImF1ZCI6WyJ1cm46c2VydmljZTppbWFnZS5vcGVyYXRpb25zIl19.zkcwS2SIdcXJdYYY9ftU4khFEU69a8J6MQopfswQlsU");
        embedBuilder.setTitle("喔給!");
        embedBuilder.setDescription(errorMessage.replaceAll("\\|", "\n"));
        embedBuilder.setColor(Color.green);
        embedBuilder.setTimestampToNow();
        return embedBuilder;
    }

    public static EmbedBuilder storyMessage(int storyIndex, String type, String description, Map<String, Integer> messageTagReplaceIndex, User user){
        String formatMessage = description;
        if(messageTagReplaceIndex.size() > 0){
            UserDataObject userDataObject = StoryEvent.answerMap.get(user.getIdAsString());
            for(Map.Entry<String, Integer> entry : messageTagReplaceIndex.entrySet()){
                String tag = entry.getKey();
                int index = entry.getValue();
                formatMessage = formatMessage.replaceAll("\\{"+tag+"\\}", userDataObject.getReplyByIndex(index).getAnswer());
            }
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Emily", "", "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/8efad515-12d3-4a4d-8f77-0eaeee12fb0f/d8sk8k0-00e546ba-db9c-48eb-9f7b-0652db666384.png/v1/fill/w_743,h_1076,strp/anime_pink_hair_girl_render_by_meilichan15_d8sk8k0-pre.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9MTE1OSIsInBhdGgiOiJcL2ZcLzhlZmFkNTE1LTEyZDMtNGE0ZC04Zjc3LTBlYWVlZTEyZmIwZlwvZDhzazhrMC0wMGU1NDZiYS1kYjljLTQ4ZWItOWY3Yi0wNjUyZGI2NjYzODQucG5nIiwid2lkdGgiOiI8PTgwMCJ9XV0sImF1ZCI6WyJ1cm46c2VydmljZTppbWFnZS5vcGVyYXRpb25zIl19.zkcwS2SIdcXJdYYY9ftU4khFEU69a8J6MQopfswQlsU");
        embedBuilder.setDescription(formatMessage.replaceAll("\\|", "\n"));
        embedBuilder.setTimestampToNow();
        embedBuilder.setFooter(String.format("%d-"+type, storyIndex));
        return embedBuilder;
    }

    public static EmbedBuilder clubClassRegisterConfirmMessage(ClubClass clubClass){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("社課" + clubClass.getClassName() + "的註冊確認");
        embedBuilder.setDescription("確定要註冊這個社課 " + clubClass.getClassName() + " 嗎，請核對以下資料喔!\n\n若按鈕失效請再註冊一次社課");
        embedBuilder.addField("時間", clubClass.getSchedule().toString(DateTimeFormat.forPattern("yyyy/MM/dd hh:mm")));
        embedBuilder.setColor(Color.CYAN);
        return embedBuilder;
    }

    public static EmbedBuilder calendarFormatEmbed(Map<Integer, Integer> plannedCount){
        DateTime dateTime = DateTime.now();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        java.util.List<java.util.List<String>> array = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            List<String> temp = new ArrayList<>();
            for(int j = 0; j < 7; j++){
                temp.add("－－");
            }
            array.add(temp);
        }
        dateTime.withZone(DateTimeZone.forID("Asia/Taipei"));
        dateTime = dateTime.withDate(year, month, 1);
        dateTime = dateTime.withTime(0,0,0, 0);
        int startWeek = 1;
        int startDay = dateTime.getDayOfWeek();
        int day = 1;
        while(month == dateTime.getMonthOfYear()){
            day = dateTime.getDayOfMonth();
            startDay = dateTime.getDayOfWeek();
            if(startDay == 7) startWeek += 1;
            Transliterator tl = Transliterator.getInstance("Halfwidth-Fullwidth");
            int value = plannedCount.getOrDefault(day, 0);
            String data = tl.transliterate(String.valueOf(String.format("%2d", day)));
            if(value > 0) {
                data = "__***" + data + "***__";
            }
            array.get(startWeek).set(startDay % 7, data);
            dateTime = dateTime.plusDays(1);
        }
        String data = "";
        for(int i = 1; i < array.size()-1; i++){
            List<String> list = array.get(i);
            data += "｜" + String.join("｜", list) + "｜" + "\n";
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(data);
        return embedBuilder;
    }
}
