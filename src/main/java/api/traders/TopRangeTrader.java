package api.traders;

import api.enums.Currency;
import api.exchanges.IExc;

public class TopRangeTrader extends Trader {

    public TopRangeTrader(Currency currency, IExc exchange) {
        super(currency, exchange);
    }

    @Override
    protected void changeSettings() {

    }
}


