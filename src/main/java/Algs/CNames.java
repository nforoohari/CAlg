package Algs;

public enum CNames {
    BBB(1),
    EEE(2),
    TTT(3);

    private final int code;

    CNames(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

