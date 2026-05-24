package api;

public enum Crypto {
    BBB(1,"BBB"),
    EEE(2,"EEE"),
    TTT(3,"TTT"),
    SSS(4,"SSS"),
    LLL(5,"LLL"),
    CCC(6,"CCC"),
    PPP(7,"PPP"),
    SAM(1000,"SAM");


    private final long code;

    private final String name;

    Crypto(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public long getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Crypto fromCode(long code) {
        for (Crypto c : Crypto.values()) {
            if (c.getCode() == code) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}

