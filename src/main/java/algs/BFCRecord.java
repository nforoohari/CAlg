package algs;
public class BFCRecord {

    private final String date;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final double volume;

    public BFCRecord(String date, double open, double high,
                     double low, double close, double volume) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "BFCRecord{" +
                "date='" + date + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }
}
