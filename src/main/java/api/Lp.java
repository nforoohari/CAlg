package api;

public enum Lp {
    OneSecond("1s", "crypto_second", 1000),
    OneMinute("1m", "crypto_minute", 60_000),
    OneHour("1h", "crypto_hour", 3_600_000),
    OneDay("1d", "crypto_day", 86_400_000);

    private final String name;
    private final String tableName;
    private final long millis;

    Lp(String name, String tableName, long millis) {
        this.name = name;
        this.tableName = tableName;
        this.millis = millis;
    }

    public String getName() {
        return name;
    }

    public String getTableName() {
        return tableName;
    }

    public long getMillis() {
        return millis;
    }
}

