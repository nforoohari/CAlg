package api.traders;

import java.util.Date;

public class TraderSettings {

    private long id;
    private long traderId;
    private Date date;

    public double thresholdPrice;
    public double stopLoss;
    public double deltaPercent;
    public double deltaPrice;
    public double ascendingPercent;
    public double ascendingPrice;

    public TraderSettings() {
    }

    public TraderSettings(double thresholdPrice, double stopLoss, double deltaPercent, double ascendingPercent) {

        this.thresholdPrice = thresholdPrice;
        this.stopLoss = stopLoss;
        this.deltaPercent = deltaPercent;
        this.deltaPrice = this.deltaPercent * thresholdPrice / 100;
        this.ascendingPercent = ascendingPercent;
        this.ascendingPrice = this.ascendingPercent * thresholdPrice / 100;

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
