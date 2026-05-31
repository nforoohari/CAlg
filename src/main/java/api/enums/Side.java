package api.enums;

public enum Side {

    BUY(0, "BUY"),
    SELL(1, "SELL");

    private final int code;
    private final String name;

    Side(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Side fromCode(int code) {
        for (Side c : Side.values()) {
            if (c.getCode() == code) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
