package api;

public class Pdc {
    private final Oh oh;
    private final Lp lp;
    private Double limitedPrice;
    private Double stopLoss;
    private final Double baseVolume;
    private final Double delta;
    private Double deltaPrice;
    private final Double ascending;
    private Double ascendingPrice;
    private final Double fee;


    public Pdc(Oh oh, Lp lp, Double limitedPrice, Double stopLoss, Double baseVolume, Double deltaPercent, Double ascendingPercent, Double feePercent) {

        this.oh = oh;
        this.lp = lp;
        this.limitedPrice = limitedPrice;
        this.stopLoss = stopLoss;
        this.baseVolume = baseVolume;
        this.delta = deltaPercent / 100;
        this.deltaPrice = this.delta * limitedPrice;
        this.ascending = ascendingPercent / 100;
        this.ascendingPrice = this.ascending * limitedPrice;
        this.fee = feePercent / 100;

    }

    public Oh getCrypto() {
        return oh;
    }

    public Lp getInterval() {
        return lp;
    }

    public Double getLimitedPrice() {
        return limitedPrice;
    }

    public Double getStopLoss() {
        return stopLoss;
    }

    public Double getBaseVolume() {
        return baseVolume;
    }

    public Double getDelta() {
        return delta;
    }

    public Double getDeltaPrice() {
        return deltaPrice;
    }

    public Double getAscending() {
        return ascending;
    }

    public Double getAscendingPrice() {
        return ascendingPrice;
    }

    public Double getFee() {
        return fee;
    }

    public void setLimitedPrice(Double limitedPrice) {
        this.limitedPrice = limitedPrice;
    }

    public void setStopLoss(Double stopLoss) {
        this.stopLoss = stopLoss;
    }

    public void setDeltaPrice(Double deltaPrice) {
        this.deltaPrice = deltaPrice;
    }

    public void setAscendingPrice(Double ascendingPrice) {
        this.ascendingPrice = ascendingPrice;
    }
}
