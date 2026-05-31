package api.enums;

public enum SleepTime {

    Binance(1000),
    MyExc(1);

    private final long millis;

    SleepTime(long millis) {
        this.millis = millis;
    }

}
