package api.traders;

import api.enums.Currency;
import api.enums.Interval;

public class TraderSettings {

    public double thresholdPrice;
    public double stopLoss;
    public final double delta;
    public double deltaPrice;
    public final double ascending;
    public double ascendingPrice;

    //    private final Currency cryptoCurrency;
    //    private final Interval interval;
    //    private final Double baseVolume;
    //    private final Double fee;


    public TraderSettings(double thresholdPrice, double stopLoss, double deltaPercent, double ascendingPercent) {

        this.thresholdPrice = thresholdPrice;
        this.stopLoss = stopLoss;
        this.delta = deltaPercent / 100;
        this.deltaPrice = this.delta * thresholdPrice;
        this.ascending = ascendingPercent / 100;
        this.ascendingPrice = this.ascending * thresholdPrice;
        //        this.currency = currency;
        //        this.interval = interval;
        //        this.baseVolume = baseVolume;
        //        this.fee = feePercent / 100;

    }

}
