package api;

import java.util.Date;

public class Rxc {

    private Oh oh;
    private final Date date;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final double volume;

    public Rxc(Oh oh, Date date, double open, double high,
               double low, double close, double volume) {
        this.oh = oh;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public Rxc(Date date, double open, double high,
               double low, double close, double volume) {
        this.oh = Oh.SAM;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public Oh getCrypto() {
        return oh;
    }

    public Date getDate() {
        return date;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public double getVolume() {
        return volume;
    }

    public void setCrypto(Oh oh) {
        this.oh = oh;
    }

    @Override
    public String toString() {
        return "CryptoRecord{" +
                "crypto=" + oh.getName() +
                ", date='" + date + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }
}