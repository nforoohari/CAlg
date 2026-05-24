package api;

public class Syc {
    private volatile boolean running;
    private boolean firstTime;
    private Double volume;
    private Double broughtInAmount;
    private Double soldAmount;
    private Double feeAmount;

    public Syc() {

        this.running = true;
        this.firstTime = true;
        this.volume = 0.0;
        this.broughtInAmount = 0.0;
        this.soldAmount = 0.0;
        this.feeAmount = 0.0;

    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getBroughtInAmount() {
        return broughtInAmount;
    }

    public void setBroughtInAmount(Double broughtInAmount) {
        this.broughtInAmount = broughtInAmount;
    }

    public Double getSoldAmount() {
        return soldAmount;
    }

    public void setSoldAmount(Double soldAmount) {
        this.soldAmount = soldAmount;
    }

    public Double getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Double feeAmount) {
        this.feeAmount = feeAmount;
    }
}
