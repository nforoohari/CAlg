package api.enums;

public enum Exchange {

    MyExc(0, "MyExc"),
    Binance_TestNet_NonOpr(1, "Binance_TestNet_NonOpr"),
    Binance_TestNet_Opr(2, "Binance_TestNet_Opr"),
    Binance_MainNet_NonOpr(3, "Binance_MainNet_NonOpr"),
    Binance_MainNet_Opr(4, "Binance_MainNet_Opr");


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
