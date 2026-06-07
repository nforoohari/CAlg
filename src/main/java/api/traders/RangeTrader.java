package api.traders;

import api.enums.Currency;
import api.enums.Exchange;
import api.enums.Interval;

public class RangeTrader extends Trader {
    public RangeTrader() {
        super();
    }

    public RangeTrader(Exchange exchange, double fee, Interval interval, Currency currency) throws Exception {
        super(exchange, fee, interval, currency);
    }

    public RangeTrader(Exchange exchange, double fee, Interval interval, Currency currency, String startTime, String endTime) throws Exception {
        super(exchange, fee, interval, currency, startTime, endTime);
    }

    @Override
    protected void changeSettings() {

        TraderSettings ts = getTraderSettings();

        if (ts.bottomFix && ts.topFix) {

        } else if (ts.bottomFix) {

//            ts.thresholdPrice =;
//            ts.stopLossPercent =;
//            ts.stopLoss = ts.thresholdPrice * ts.stopLossPercent / 100;
//            ts.deltaPercent =;
            ts.delta = ((ts.delta + ts.ascending) > (5 * ts.thresholdPrice * getFee() / 100)) ? (ts.delta + ts.ascending) : ts.delta;
//            ts.ascendingPercent =;
//            ts.ascending = ts.thresholdPrice * ts.ascendingPercent / 100;


        } else if (ts.topFix) {

//            ts.thresholdPrice =;
//            ts.stopLossPercent =;
//            ts.stopLoss = ts.thresholdPrice * ts.stopLossPercent / 100;
//            ts.deltaPercent =;
            ts.delta = ((ts.delta + ts.ascending) > (5 * ts.thresholdPrice * getFee() / 100)) ? (ts.delta + ts.ascending) : ts.delta;
//            ts.ascendingPercent =;
//            ts.ascending = ts.thresholdPrice * ts.ascendingPercent / 100;

        } else {

            if ((ts.delta + ts.ascending) > (5 * ts.thresholdPrice * getFee() / 100)) {

                ts.thresholdPrice += ts.ascending / 2;
//            ts.deltaPercent =;
                ts.delta += ts.ascending;
//            ts.stopLossPercent =;
                ts.stopLoss = ts.thresholdPrice * ts.stopLossPercent / 100;
//            ts.ascendingPercent =;
                ts.ascending = ts.thresholdPrice * ts.ascendingPercent / 100;
            }

        }
    }
}
