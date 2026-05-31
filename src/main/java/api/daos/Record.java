package api.daos;

import api.enums.Currency;

import java.util.Date;

public class Record {

    private long id;
    private Currency currency;
    private final Date date;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final double volume;

    public Record(long id, Currency currency, Date date, double open, double high,
                  double low, double close, double volume) {
        this.id = id;
        this.currency = currency;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public Record(Currency currency, Date date, double open, double high,
                  double low, double close, double volume) {
        this.currency = currency;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public Record(Date date, double open, double high,
                  double low, double close, double volume) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public long getId() { return id; }

    public Currency getCryptoCurrency() {
        return currency;
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

    public void setId(long id) { this.id = id; }

    public void setCrypto(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "Record{" +
                "currency=" + currency.getName() +
                ", date='" + date + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }
}