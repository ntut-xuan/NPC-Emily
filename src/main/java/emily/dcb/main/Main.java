package emily.dcb.main;

import emily.dcb.database.StoryDatabase;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        //Token need to type in first arguments.
        String token = args[0];

        System.out.println("Running bot with this token: " + token);

        //Create a DiscordApiBuilder object to create DiscordApi.
        DiscordApiBuilder builder = new DiscordApiBuilder();

        //So here we will get the api object and do something.
        DiscordApi api = builder.setToken(token).setAllIntents().login().join();

        new RegisterEvent(api);
        StoryDatabase.load();

        System.out.println("All task has been executed.");

    }

}
