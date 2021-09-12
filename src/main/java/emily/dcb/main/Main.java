package emily.dcb.main;

import emily.dcb.database.ClubClassDatabase;
import emily.dcb.database.EmilySettingDatabase;
import emily.dcb.database.StoryDatabase;
import emily.dcb.database.UserDataBase;
import emily.dcb.thread.AutoSaveTimerTask;
import emily.dcb.thread.MemberRoleCheck;
import emily.dcb.utils.SchoolAbbrTableCrawler;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Timer;

public class Main {

    public static DiscordApi discordApi;

    public static void main(String[] args) throws IOException, GeneralSecurityException {

        //Token need to type in first arguments.
        String token = args[0];

        System.out.println("Running bot with this token: " + token);

        //Create a DiscordApiBuilder object to create DiscordApi.
        DiscordApiBuilder builder = new DiscordApiBuilder();

        //So here we will get the api object and do something.
        discordApi = builder.setToken(token).setAllIntents().login().join();

        new RegisterEvent(discordApi);

        EmilySettingDatabase.load();
        GoogleSheetsLoader.load();
        ClubClassDatabase.load();
        StoryDatabase.load();
        SchoolAbbrTableCrawler.load();
        UserDataBase.load();

        Timer autoSaveTimer = new Timer();
        Timer memberRoleCheckTimer = new Timer();

        autoSaveTimer.schedule(new AutoSaveTimerTask(), 0, 10000);
        memberRoleCheckTimer.schedule(new MemberRoleCheck(), 0, 10000);

        System.out.println("All task has been executed.");

    }

}
