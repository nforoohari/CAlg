package api.enums;

public enum UsablePercentage {

    Five_Percent(5),
    Ten_Percent(10),
    Twenty_Percent(20),
    Thirty_Percent(30),
    Fifty_Percent(50);

    private final double value;

    UsablePercentage(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public double getRatio() {
        return (value / 100.0);
    }

}
