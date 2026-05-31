package api.enums;

public enum Exchange {

    MyExc(0, "MyExc"),
    Binance(1, "Binance");

    private final int code;
    private final String name;

    Exchange(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public static Exchange fromCode(int code) {
        for (Exchange c : Exchange.values()) {
            if (c.getCode() == code) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }

}
