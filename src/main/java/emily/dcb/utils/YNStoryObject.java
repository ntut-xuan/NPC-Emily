package emily.dcb.utils;

import org.javacord.api.entity.emoji.Emoji;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.net.URL;

public class YNStoryObject extends StoryObject{

    String yesEmoji;
    String noEmoji;
    String returnEmoji;
    int ifYes;
    int ifNo;
    int returnStoryIndex;

    public YNStoryObject(int index, String message, String plainMessage, String type, String yesEmoji, String noEmoji, String returnEmoji, int ifYes, int ifNo, int returnStoryIndex){
        this.yesEmoji = yesEmoji;
        this.noEmoji = noEmoji;
        this.returnEmoji = returnEmoji;
        this.plainMessage = plainMessage;
        this.message = message;
        this.type = type;
        this.index = index;
        this.returnStoryIndex = returnStoryIndex;
        this.ifNo = ifNo;
        this.ifYes = ifYes;
    }

    public int getIfYes(){
        return ifYes;
    }

    public int getIfNo(){
        return ifNo;
    }

    public String getPlainMessage(){
        return plainMessage;
    }

    public int getReturnStoryIndex(){
        return returnStoryIndex;
    }

    public String getYesEmoji(){
        return yesEmoji;
    }

    public String getNoEmoji(){
        return noEmoji;
    }

    public String getReturnEmoji(){
        return returnEmoji;
    }

    public EmbedBuilder getEmbedBuilder(){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Emily", "", "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/8efad515-12d3-4a4d-8f77-0eaeee12fb0f/d8sk8k0-00e546ba-db9c-48eb-9f7b-0652db666384.png/v1/fill/w_743,h_1076,strp/anime_pink_hair_girl_render_by_meilichan15_d8sk8k0-pre.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7ImhlaWdodCI6Ijw9MTE1OSIsInBhdGgiOiJcL2ZcLzhlZmFkNTE1LTEyZDMtNGE0ZC04Zjc3LTBlYWVlZTEyZmIwZlwvZDhzazhrMC0wMGU1NDZiYS1kYjljLTQ4ZWItOWY3Yi0wNjUyZGI2NjYzODQucG5nIiwid2lkdGgiOiI8PTgwMCJ9XV0sImF1ZCI6WyJ1cm46c2VydmljZTppbWFnZS5vcGVyYXRpb25zIl19.zkcwS2SIdcXJdYYY9ftU4khFEU69a8J6MQopfswQlsU");
        embedBuilder.setDescription(message.replaceAll("\\|", "\n"));
        embedBuilder.setTimestampToNow();
        embedBuilder.setFooter(String.format("%d-YN", index));
        return embedBuilder;
    }
}
