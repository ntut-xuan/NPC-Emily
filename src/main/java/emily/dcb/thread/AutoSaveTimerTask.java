package emily.dcb.thread;

import emily.dcb.database.EmilySettingDatabase;
import emily.dcb.database.UserDataBase;
import emily.dcb.utils.LogCreator;

import java.io.IOException;
import java.util.TimerTask;

public class AutoSaveTimerTask extends TimerTask {
    @Override
    public void run() {
        try {
            UserDataBase.save();
            LogCreator.info("已自動儲存使用者檔案");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
