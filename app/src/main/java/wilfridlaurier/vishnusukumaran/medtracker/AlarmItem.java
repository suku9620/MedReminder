package wilfridlaurier.vishnusukumaran.medtracker;

public class AlarmItem {
    private String title;
    private String time;
    private String Date;
    private String repeatKey;
    private String repeatInterval;
    private String repeatType;
    private String activeKey;


    public AlarmItem(String title, String time, String Date, String repeatKey,String repeatInterval,
                     String repeatType, String activeKey){
        this.title = title ;
        this.Date = Date ;
        this.repeatKey = repeatKey ;
        this.repeatInterval = repeatInterval ;
        this.repeatType = repeatType ;
        this.time = time ;
        this.activeKey = activeKey ;

    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getDate() {
        return Date;
    }
    public void setDate(String Date) {
        this.Date = Date;
    }
    public String getRepeatKey() {
        return repeatKey;
    }
    public void setRepeatKey(String repeatKey) {
        this.repeatKey = repeatKey;
    }
    public String getRepeatInterval() {
        return repeatInterval;
    }
    public void setRepeatInterval(String repeatInterval) {
        this.repeatInterval = repeatInterval;
    }
    public String getRepeatType() {
        return repeatType;
    }
    public void setActiveKey(String activeKey) {
        this.activeKey = repeatType;
    }
    public void setRepeatType(String activeKey) {
        this.activeKey = activeKey;
    }


}
