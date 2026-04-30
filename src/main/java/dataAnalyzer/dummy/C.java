package dataAnalyzer.dummy;

public enum C {
    BTC(1),
    ETH(2),
    TON(3),
    SAM(4);

    private final int code;

    C(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

