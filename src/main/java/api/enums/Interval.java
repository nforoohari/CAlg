package api.enums;

public enum Interval {

    OneSecond(0, "1s", "crypto_second", 1000),
    OneMinute(1, "1m", "crypto_minute", 60_000),
    OneHour(2, "1h", "crypto_hour", 3_600_000),
    OneDay(3, "1d", "crypto_day", 86_400_000);

    private final int code;
    private final String name;
    private final String tableName;
    private final long millis;

    Interval(int code, String name, String tableName, long millis) {
        this.code = code;
        this.name = name;
        this.tableName = tableName;
        this.millis = millis;
    }

    public int getCode() { return code; }

    public String getName() {
        return name;
    }

    public String getTableName() {
        return tableName;
    }

    public long getMillis() {
        return millis;
    }

    public long getMillisDividedByTwo() {
        return (millis / 2);
    }

    public static Interval fromCode(int code) {
        for (Interval c : Interval.values()) {
            if (c.getCode() == code) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}

