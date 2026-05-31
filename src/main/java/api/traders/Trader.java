package api.traders;

import api.enums.Currency;
import api.enums.Interval;
import api.enums.Side;
import api.exchanges.IExc;

import java.util.Date;

public abstract class Trader extends Thread {

    private long id;
    private IExc exchange;
    private Currency currency;
    private double fee;
    private Interval interval;
    private Date date;

    private TraderState traderState;
    private TraderSettings traderSettings;


    private ApplySettings applySettings;
    private SubmitRequest submitRequest;

    private boolean isInitialState;
    private Side currentSide;
    private boolean isRunning;
    private long sleepTime;

    public Trader(Currency currency, IExc exchange) {
        this.currency = currency;
        this.exchange = exchange;

        initialize();
    }

    private void initialize() {
    }

    public void run() {
        {

            System.out.println("Trading started at : " + new Date());

            while (isRunning) {

                printAllSettings();

                try {
                    trade();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                changeSettings();
            }

            printAllState();

            System.out.println("Trading stopped at : " + new Date());
        }
    }

    public void printAllSettings() {
        System.out.println("limitedPrice : " + traderSettings.thresholdPrice);
        System.out.println("stopLoss : " + traderSettings.stopLoss);
        System.out.println("deltaPrice : " + traderSettings.deltaPrice);
        System.out.println("limitedPrice + deltaPrice : " + (traderSettings.thresholdPrice + traderSettings.deltaPrice));
        System.out.println("ascendingPrice : " + traderSettings.ascendingPrice);
    }

    public void printAllState() {
        System.out.println("Thread stopped safely at : " + new Date());
        System.out.println("broughtInAmount : " + (((double) Math.round(initialState.balance * 100)) / 100));
        System.out.println("soldAmount : " + (((double) Math.round(currentState.balance * 100)) / 100));
        System.out.println("volume : " + currentState.volume);
        System.out.println("payedFeeAmount : " + (((double) Math.round(currentState.payedFee * 100)) / 100));
        System.out.println("((soldAmount / broughtInAmount) - 1) * 100  : " + ((double) Math.round(((currentState.balance / initialState.balance) - 1) * 10000)) / 100);
        System.out.println("((volume / baseVolume) - 1) * 100  : " + ((double) Math.round(((currentState.volume / initialState.volume) - 1) * 10000)) / 100);
    }

    public void printResult() {
        System.out.println("Trading result at : " + new Date());
    }

    protected abstract void changeSettings();

    private void trade() throws Exception {

        stepTrade();
        swapSide();
        stepTrade();
    }

    private void stepTrade() throws Exception {
        while (isRunning) {
            exchange.fetchExcData(currency, interval.getName());
            if (applySettings.apply()) {
                submitRequest.submit();
                break;
            }
        }
    }

    private void swapSide() {
        currentSide = (currentSide == Side.SELL) ? Side.BUY : Side.SELL;
        applySettings = (currentSide == Side.SELL) ? this::buyCheck : this::sellCheck;
        submitRequest = (currentSide == Side.SELL) ? this::buyAction : this::sellAction;
    }

    public void stopTrading() {
        isRunning = false;
    }

    private boolean buyCheck() {
        return true;
    }

    private boolean sellCheck() {
        return true;
    }

    private void buyAction() {
    }

    private void sellAction() {
    }


}
