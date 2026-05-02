package mainPackage;

import java.util.Date;

public class CryptoRecord {

    private Crypto crypto;
    private final Date date;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final double volume;

    public CryptoRecord(Crypto crypto, Date date, double open, double high,
                        double low, double close, double volume) {
        this.crypto = crypto;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public CryptoRecord( Date date, double open, double high,
                        double low, double close, double volume) {
        this.crypto = Crypto.SAM;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public Crypto getCrypto() {
        return crypto;
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

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    @Override
    public String toString() {
        return "CryptoRecord{" +
                "crypto=" + crypto.getName() +
                ", date='" + date + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }
}