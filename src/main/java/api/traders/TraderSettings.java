package api.traders;

import java.util.Date;

public class TraderSettings {

    private long id;
    private long traderId;
    private Date date;

    public double bidPrice;
    public double askPrice;
    public double lowerStopLossPrice;
    public double upperStopLossPrice;
    public double changePercent;
    public boolean topFix;
    public boolean bottomFix;

    public TraderSettings() {
    }

    public TraderSettings(double bidPrice, double askPrice, double lowerStopLossPrice, double upperStopLossPrice, double changePercent, boolean topFix, boolean bottomFix) {
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.lowerStopLossPrice = lowerStopLossPrice;
        this.upperStopLossPrice = upperStopLossPrice;
        this.changePercent = changePercent;
        this.topFix = topFix;
        this.bottomFix = bottomFix;
    }

    public TraderSettings(double bidPrice, double askPrice, double lowerStopLossPrice, double upperStopLossPrice) {
        this(bidPrice, askPrice, lowerStopLossPrice, upperStopLossPrice, 0, true, true);
    }

    public long getId() { return id; }

    public void setId(long id) {
        this.id = id;
    }

    public long getTraderId() {
        return traderId;
    }

    public void setTraderId(long traderId) { this.traderId = traderId; }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
