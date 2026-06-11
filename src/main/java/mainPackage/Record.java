package mainPackage;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class Record {

    private long id;
    private Currency currency;
    private Date date;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;

    private SimpleDateFormat df;

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

    }

    public Record(Currency currency, Date date, double open, double high,
                  double low, double close, double volume) {
        this(0, currency, date, open, high, low, close, volume);
    }

    public Record(Date date, double open, double high,
                  double low, double close, double volume) {
        this(0, null, date, open, high, low, close, volume);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", currency=" + currency.getName() +
                ", date='" + df.format(date) + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }

    public Date getTradingViewDate() {

        String tradingViewStringDate = df.format(date);
        LocalDateTime tradingViewLocalDate = LocalDateTime.parse(tradingViewStringDate, formatter);
        long tradingViewMs = tradingViewLocalDate.toInstant(ZoneOffset.UTC).toEpochMilli();
        return new Date(tradingViewMs);

    }
}