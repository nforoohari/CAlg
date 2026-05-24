package api;

public class CryptoParams {
    private final Crypto crypto;
    private final Interval interval;
    private Double limitedPrice;
    private Double stopLoss;
    private final Double baseVolume;
    private final Double delta;
    private Double deltaPrice;
    private final Double ascending;
    private Double ascendingPrice;
    private final Double fee;


    public CryptoParams(Crypto crypto, Interval interval, Double limitedPrice, Double stopLoss, Double baseVolume, Double deltaPercent, Double ascendingPercent, Double feePercent) {

        this.crypto = crypto;
        this.interval = interval;
        this.limitedPrice = limitedPrice;
        this.stopLoss = stopLoss;
        this.baseVolume = baseVolume;
        this.delta = deltaPercent / 100;
        this.deltaPrice = this.delta * limitedPrice;
        this.ascending = ascendingPercent / 100;
        this.ascendingPrice = this.ascending * limitedPrice;
        this.fee = feePercent / 100;

    }

    public Crypto getCrypto() {
        return crypto;
    }

    public Interval getInterval() {
        return interval;
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
