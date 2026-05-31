package api.traders;

import api.enums.Currency;
import api.exchanges.IExc;

public class BottomRangeTrader extends Trader {

    public BottomRangeTrader(Currency currency, IExc exchange) {
        super(currency, exchange);
    }

    @Override
    protected void changeSettings() {

    }
}
