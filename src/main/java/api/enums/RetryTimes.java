package api.enums;

public enum RetryTimes {

    Min(1),
    Normal(5),
    Max(9);

    private final int value;

    RetryTimes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
