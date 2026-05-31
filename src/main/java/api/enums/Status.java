package api.enums;

public enum Status {

    In_Progress(0),
    Cancelled(1),
    Completed(2);

    private final int code;

    Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Status fromCode(int code) {
        for (Status c : Status.values()) {
            if (c.getCode() == code) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
