package api.traders;

import api.daos.Record;
import api.enums.Currency;
import api.enums.Interval;
import api.exchanges.IExc;
import api.exchanges.MyExc;
import api.orders.OrderRequest;

import java.util.Date;

public class NormalRangeTrader extends Trader {

    private IExc inet;
    private TraderSettings params;
    private CryptoState state;
    private long sleepTime;

    public NormalRangeTrader(Currency currency, IExc exchange) {
        super(currency, exchange);
    }


    @Override
    public void initialize() throws Exception {

        this.inet = new MyExc(Interval.OneMinute, Currency.Bitcoin, "2025-01-01 00:00:00", "2025-01-03 00:00:00");
        this.params = new TraderSettings(Currency.Bitcoin, Interval.OneMinute, 36000.0, 34200.0, 10.0, 2.0, 0.0, 0.5);
        this.state = new CryptoState();
        this.sleepTime = this.inet instanceof MyExc ? 1 : Interval.OneMinute.getMillis();
        System.out.println("System is ready.");
    }

    @Override
    public void run() {

        System.out.println("Algorithm started at : " + new Date());

        while (state.isRunning()) {
            System.out.println("limitedPrice : " + params.getLimitedPrice());
            System.out.println("stopLoss : " + params.getStopLoss());
            System.out.println("deltaPrice : " + params.getDeltaPrice());
            System.out.println("limitedPrice + deltaPrice : " + (params.getLimitedPrice() + params.getDeltaPrice()));
            System.out.println("ascendingPrice : " + params.getAscendingPrice());

            try {
                doAlg();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Thread stopped safely at : " + new Date());
        System.out.println("broughtInAmount : " + (((double) Math.round(state.getBroughtInAmount() * 100)) / 100));
        System.out.println("soldAmount : " + (((double) Math.round(state.getSoldAmount() * 100)) / 100));
        System.out.println("volume : " + state.getVolume());
        System.out.println("payedFeeAmount : " + (((double) Math.round(state.getFeeAmount() * 100)) / 100));
        System.out.println("((soldAmount / broughtInAmount) - 1) * 100  : " + ((double) Math.round(((state.getSoldAmount() / state.getBroughtInAmount()) - 1) * 10000)) / 100);
        System.out.println("((volume / baseVolume) - 1) * 100  : " + ((double) Math.round(((state.getVolume() / params.getBaseVolume()) - 1) * 10000)) / 100);
    }

    @Override
    protected void changeSettings() {

    }

    public void doAlg() throws Exception {

        boolean buyCheck = false;
        boolean sellCheck = false;
        Record rec = null;
        OrderRequest order = null;

        while (!buyCheck) {
            rec = inet.fetchTradeData(params.getCrypto(), params.getInterval().getName());
            if (rec != null) {
                if (rec.getLow() < params.getLimitedPrice()) {

                    state.setBroughtInAmount(state.isFirstTime() ? params.getBaseVolume() * rec.getLow() * (1 + params.getFee()) : state.getBroughtInAmount());
                    state.setVolume(state.isFirstTime() ? params.getBaseVolume() : state.getSoldAmount() / (rec.getLow() * (1 + params.getFee())));
                    order = inet.buy(rec.getCrypto(), rec.getLow(), state.getVolume());
                    while (!(order.getOrderStatus().isCompleted()) && rec != null) {
                        Thread.sleep(sleepTime);
                        rec = inet.fetchTradeData(params.getCrypto(), params.getInterval().getName());
                        order = inet.checkStatus(params.getCrypto(), order.getOrderStatus().getId());
                    }
                    ;
                    if (rec != null) {
                        state.setFeeAmount(state.getFeeAmount() + (state.getVolume() * (rec.getLow() * params.getFee())));
                        state.setSoldAmount(0.0);
                        state.setFirstTime(false);
                    } else {

//                        state.setFeeAmount(state.getFeeAmount() + (state.getVolume() * (rec.getLow() * params.getFee())));
//                        state.setSoldAmount(0.0);
//                        state.setFirstTime(false);

                        state.setRunning(false);
                        sellCheck = true;
                        break;
                    }

                }
            } else {

                state.setRunning(false);
                sellCheck = true;
                break;

            }
            Thread.sleep(sleepTime);
        }

        while (!sellCheck) {
            rec = inet.fetchTradeData(params.getCrypto(), params.getInterval().getName());
            if (rec != null) {
                if (rec.getHigh() > (params.getLimitedPrice() + params.getDeltaPrice())) {

//                    sellCheck = sell(rec.getC(), rec.getHigh(), volume);
                    order = inet.sell(rec.getCrypto(), rec.getHigh(), state.getVolume());
                    while (!(order.getOrderStatus().isCompleted()) && rec != null) {
                        Thread.sleep(sleepTime);
                        rec = inet.fetchTradeData(params.getCrypto(), params.getInterval().getName());
                        order = inet.checkStatus(params.getCrypto(), order.getOrderStatus().getId());
                    }

                    if (rec != null) {
                        state.setSoldAmount(state.getVolume() * rec.getHigh() * (1 - params.getFee()));
                        state.setFeeAmount(state.getFeeAmount() + (state.getVolume() * (rec.getHigh() * params.getFee())));
                        state.setVolume(0.0);

                        params.setLimitedPrice(params.getLimitedPrice() + params.getAscendingPrice());
                        params.setStopLoss(params.getStopLoss() + params.getAscendingPrice());
                        params.setDeltaPrice(params.getDelta() * params.getLimitedPrice());
                        params.setAscendingPrice(params.getAscending() * params.getDeltaPrice());

                    } else {


//                        state.setSoldAmount(state.getVolume() * rec.getHigh() * (1 - params.getFee()));
//                        state.setFeeAmount(state.getFeeAmount() + (state.getVolume() * (rec.getHigh() * params.getFee())));
//                        state.setVolume(0.0);
//
//                        params.setLimitedPrice(params.getLimitedPrice() + params.getAscendingPrice());
//                        params.setStopLoss(params.getStopLoss() + params.getAscendingPrice());
//                        params.setDeltaPrice(params.getDelta() * params.getLimitedPrice());
//                        params.setAscendingPrice(params.getAscending() * params.getDeltaPrice());

                        state.setRunning(false);
                        break;

                    }


                } else if (rec.getClose() < params.getStopLoss()) {

                    order = inet.sell(rec.getCrypto(), rec.getClose(), state.getVolume());
                    while (!(order.getOrderStatus().isCompleted()) && rec != null) {
                        Thread.sleep(sleepTime);
                        rec = inet.fetchTradeData(params.getCrypto(), params.getInterval().getName());
                        order = inet.checkStatus(params.getCrypto(), order.getOrderStatus().getId());
                    }
                    if (rec != null) {

                        state.setSoldAmount(state.getVolume() * rec.getClose() * (1 - params.getFee()));
                        state.setFeeAmount(state.getFeeAmount() + (state.getVolume() * (rec.getClose() * params.getFee())));
                        state.setVolume(0.0);

                        params.setLimitedPrice(params.getStopLoss() - params.getDeltaPrice());
                        params.setStopLoss(params.getLimitedPrice() - params.getDeltaPrice());
                        params.setDeltaPrice(params.getDelta() * params.getLimitedPrice());
                        params.setAscendingPrice(params.getAscending() * params.getLimitedPrice());

//                        state.setRunning(false);
                    } else {

//                        state.setSoldAmount(state.getVolume() * rec.getClose() * (1 - params.getFee()));
//                        state.setFeeAmount(state.getFeeAmount() + (state.getVolume() * (rec.getClose() * params.getFee())));
//                        state.setVolume(0.0);
//
//                        params.setLimitedPrice(params.getStopLoss() - params.getDeltaPrice());
//                        params.setStopLoss(params.getLimitedPrice() - params.getDeltaPrice());
//                        params.setDeltaPrice(params.getDelta() * params.getLimitedPrice());
//                        params.setAscendingPrice(params.getAscending() * params.getLimitedPrice());
//
                        state.setRunning(false);

                    }

                }
            } else {

                state.setRunning(false);
                break;

            }
            Thread.sleep(sleepTime);
        }

    }

    @Override
    public void stopAlg() {
        state.setRunning(false);
    }

}
