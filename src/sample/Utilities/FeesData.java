package sample.Utilities;

public class FeesData {
    public int feesId;
    public String feesName;
    public double feesAmount;

    public FeesData() {
        this.feesName = feesName = "";
        this.feesAmount = feesAmount = 0;
    }

    public FeesData(int feesId, String feesName, double feesAmount) {
        this.feesId = feesId;
        this.feesName = feesName;
        this.feesAmount = feesAmount;
    }

    public int getFeesId() {
        return feesId;
    }

    public void setFeesId(int feesId) {
        this.feesId = feesId;
    }

    public String getFeesName() {
        return feesName;
    }

    public void setFeesName(String feesName) {
        this.feesName = feesName;
    }

    public double getFeesAmount() {
        return feesAmount;
    }

    public void setFeesAmount(double feesAmount) {
        this.feesAmount = feesAmount;
    }
}
