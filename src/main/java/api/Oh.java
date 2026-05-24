package api;

public enum Oh {
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

    Oh(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public long getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Oh fromCode(long code) {
        for (Oh c : Oh.values()) {
            if (c.getCode() == code) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}

