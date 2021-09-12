package emily.dcb.utils;

import org.joda.time.DateTime;

public class ClubClass {

    String className;
    String classDescription;
    String spreadSheetID;
    DateTime schedule;
    int maximumRegisterCount;

    public ClubClass(String className, String classDescription, String spreadSheetID, DateTime dateTime, int maximumRegisterCount) {
        this.className = className;
        this.classDescription = classDescription;
        this.schedule = dateTime;
        this.spreadSheetID = spreadSheetID;
        this.maximumRegisterCount = maximumRegisterCount;
    }

    public String getClassName() {
        return className;
    }

    public String getClassDescription() {
        return classDescription;
    }

    public String getSpreadSheetID(){
        return spreadSheetID;
    }

    public DateTime getSchedule() {
        return schedule;
    }

    public int getMaximumRegisterCount(){
        return maximumRegisterCount;
    }

}
