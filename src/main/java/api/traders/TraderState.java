package api.traders;

import java.util.Date;

public class TraderState {

    private long id;
    private long traderId;
    private Date date;
    public double volume;
    public double balance;
    public double payedFee;
    public boolean stopLossEnable;

    public TraderState() {
    }

    public TraderState(double volume, double balance, double payedFee) {
        this.volume = volume;
        this.balance = balance;
        this.payedFee = payedFee;
        this.stopLossEnable = false;
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