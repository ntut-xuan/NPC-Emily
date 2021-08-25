package emily.dcb.utils;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.awt.*;

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
}
