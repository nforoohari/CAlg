package api.traders;

import api.daos.TraderSettingsDao;
import api.daos.TraderStateDao;
import api.enums.Currency;
import api.enums.Exchange;
import api.enums.Interval;

import java.util.Date;

public class Main {
    public static void main(String[] args) throws Exception {

        RangeTrader trader = new RangeTrader(Exchange.MyExc,0.2, Interval.OneSecond, Currency.Bitcoin,"2025-01-01 00:00:00","2025-01-01 01:00:00");

        TraderState traderState = new TraderState(10,650000,0);
        traderState.setTraderId(trader.getId());
        trader.setTraderState(traderState);

        TraderSettings traderSettings = new TraderSettings(93560,3,1,0,false,true);
        traderSettings.setTraderId(trader.getId());
        trader.setTraderSettings(traderSettings);

        trader.start();
    }
}
