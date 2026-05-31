package api.enums;

public enum RetryTimes {

    Min(1),
    Normal(3),
    Max(5);

    private final int value;

    RetryTimes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
