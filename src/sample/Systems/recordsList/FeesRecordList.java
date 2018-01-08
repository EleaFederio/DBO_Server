package sample.Systems.recordsList;

public class FeesRecordList {
    private String feesName;
    private String presentOrAbsent;

    public FeesRecordList(String feesName, String presentOrAbsent) {
        this.feesName = feesName;
        this.presentOrAbsent = presentOrAbsent;
    }

    public String getFeesName() {
        return feesName;
    }

    public void setFeesName(String feesName) {
        this.feesName = feesName;
    }

    public String getPresentOrAbsent() {
        return presentOrAbsent;
    }

    public void setPresentOrAbsent(String presentOrAbsent) {
        this.presentOrAbsent = presentOrAbsent;
    }
}
