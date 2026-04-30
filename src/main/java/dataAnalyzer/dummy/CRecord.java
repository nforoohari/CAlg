package dataAnalyzer.dummy;

public class CRecord {

    private final C c;
    private final String date;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final double volume;

    public CRecord(C c, String date, double open, double high, double low, double close, double volume) {
        this.c = c;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public C getC() {
        return c;
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
        return "CRecord{" +
                "name=" + c +
                ", date='" + date + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }
}
