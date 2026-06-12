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

            double ap = ts.askPrice + (ts.askPrice * (ts.changePercent / 100));

            if ((ap - ts.bidPrice) > (5 * ts.askPrice * getFee() / 100)) {
                ts.askPrice += (ts.askPrice * (ts.changePercent / 100));
                ts.upperStopLossPrice += (ts.upperStopLossPrice * (ts.changePercent / 100));
            }

        } else if (ts.topFix) {

            double bp = ts.bidPrice + (ts.bidPrice * (ts.changePercent / 100));

            if ((ts.askPrice - bp) > (5 * ts.askPrice * getFee() / 100)) {
                ts.bidPrice += (ts.bidPrice * (ts.changePercent / 100));
                ts.lowerStopLossPrice += (ts.lowerStopLossPrice * (ts.changePercent / 100));
            }

        } else {

            double ap = ts.askPrice + (ts.askPrice * (ts.changePercent / 100));
            double bp = ts.bidPrice + (ts.bidPrice * (ts.changePercent / 100));

            if ((ap - bp) > (5 * ap * getFee() / 100)) {
                ts.askPrice += (ts.askPrice * (ts.changePercent / 100));
                ts.upperStopLossPrice += (ts.upperStopLossPrice * (ts.changePercent / 100));

                ts.bidPrice += (ts.bidPrice * (ts.changePercent / 100));
                ts.lowerStopLossPrice += (ts.lowerStopLossPrice * (ts.changePercent / 100));
            }

        }
    }
}
