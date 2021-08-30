package emily.dcb.thread;

import emily.dcb.database.UserDataBase;

import java.util.TimerTask;

public class MemberRoleCheck extends TimerTask {
    @Override
    public void run() {
        UserDataBase.checkUserMemberRole();
    }
}
