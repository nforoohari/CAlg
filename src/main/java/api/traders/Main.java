package api.traders;

import api.enums.Currency;
import api.enums.Exchange;
import api.enums.Interval;

public class Main {
    public static void main(String[] args) throws Exception {

        RangeTrader trader = new RangeTrader(Exchange.MyExc,0.05, Interval.OneSecond, Currency.Bitcoin,"2025-01-01 00:00:00","2025-01-01 01:00:00");
//        RangeTrader trader = new RangeTrader(Exchange.Binance_MainNet_NonOpr,0.05, Interval.OneSecond, Currency.Bitcoin);

//        TraderState traderState = new TraderState(10,650000,0);
        TraderState traderState = new TraderState(1,65000,0);
        traderState.setTraderId(trader.getId());
        trader.setTraderState(traderState);

        TraderSettings traderSettings = new TraderSettings(93550,93640,91000,97000);
//        TraderSettings traderSettings = new TraderSettings(63450,63800,63000,64000);
        traderSettings.setTraderId(trader.getId());
        trader.setTraderSettings(traderSettings);

        trader.start();
    }
}
