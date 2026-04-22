package Algs;

public class CRecord {

    private final CNames cNames;
    private final String date;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final double volume;

    public CRecord(CNames cNames, String date, double open, double high, double low, double close, double volume) {
        this.cNames = cNames;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public CNames getcNames() {
        return cNames;
    }

    public String getDate() {
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

    @Override
    public String toString() {
        return "BTCRecord{" +
                "name=" + cNames +
                "date='" + date + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }
}
