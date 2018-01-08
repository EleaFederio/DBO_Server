package sample.Systems.recordsList;

public class EventRecordList {
    private String eventName;
    private String presentOrAbsent;

    public EventRecordList(String eventName, String presentOrAbsent) {
        this.eventName = eventName;
        this.presentOrAbsent = presentOrAbsent;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getPresentOrAbsent() {
        return presentOrAbsent;
    }

    public void setPresentOrAbsent(String presentOrAbsent) {
        this.presentOrAbsent = presentOrAbsent;
    }
}
