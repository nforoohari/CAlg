package api.traders;

import java.util.Date;

public class TraderSettings {

    private long id;
    private long traderId;
    private Date date;

    public double thresholdPrice;
    public double stopLossPercent;
    public double stopLoss;
    public double deltaPercent;
    public double delta;
    public double ascendingPercent;
    public double ascending;
    public boolean topFix;
    public boolean bottomFix;

    public TraderSettings() {
    }

    public TraderSettings(double thresholdPrice, double stopLossPercent, double deltaPercent, double ascendingPercent, boolean topFix, boolean bottomFix) {

        this.thresholdPrice = thresholdPrice;
        this.stopLossPercent = stopLossPercent;
        this.stopLoss = thresholdPrice * stopLossPercent / 100;
        this.deltaPercent = deltaPercent;
        this.delta = thresholdPrice * deltaPercent / 100;
        this.ascendingPercent = ascendingPercent;
        this.ascending = thresholdPrice * ascendingPercent / 100;
        this.topFix = topFix;
        this.bottomFix = bottomFix;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTraderId() {
        return traderId;
    }

    public void setTraderId(long traderId) {
        this.traderId = traderId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
