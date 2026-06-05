package api.traders;

import api.daos.Record;
import api.enums.Currency;
import api.enums.Exchange;
import api.enums.Interval;
import api.enums.Side;
import api.exchanges.NonOprBinance;
import api.exchanges.OprBinance;
import api.exchanges.IExc;
import api.exchanges.MyExc;
import api.orders.OrderRequest;

import java.util.Date;

public class Trader extends Thread {

    private long id;
    private Exchange exchange;
    private Currency currency;
    private double fee;
    private Interval interval;
    private Date date;
    private String startTime;
    private String endTime;

    private TraderState traderState;
    private TraderSettings traderSettings;

    private ApplySettings applySettings;
    private SubmitRequest submitRequest;

    private IExc iExc;
    private boolean isInitialState;
    private Side currentSide;
    private boolean isRunning;
    private long sleepTime;
    private Record record;

    public Trader() {
    }

    public Trader(Exchange exchange, double fee, Interval interval, Currency currency) throws Exception {
        this.exchange = exchange;
        this.fee = fee;
        this.interval = interval;
        this.currency = currency;
        this.startTime = "";
        this.endTime = "";

        this.date = new Date();
        init();
    }

    public Trader(Exchange exchange, double fee, Interval interval, Currency currency, String startTime, String endTime) throws Exception {
        this.exchange = exchange;
        this.fee = fee;
        this.interval = interval;
        this.currency = currency;
        this.startTime = startTime;
        this.endTime = endTime;

        this.date = new Date();
        init();
    }


    private void init() throws Exception {

        assert exchange != null;
        iExc = switch (exchange) {
            case MyExc -> new MyExc(exchange, fee, interval, currency, startTime, endTime);
            case Binance_MainNet_NonOpr, Binance_TestNet_NonOpr -> new NonOprBinance(exchange, fee, interval, currency);
            case Binance_MainNet_Opr, Binance_TestNet_Opr -> new OprBinance(exchange, fee, interval, currency);
        };
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

    protected void changeSettings() {
    }

    private void trade() throws Exception {

        stepTrade();
        swapSide();
        stepTrade();
    }

    private void stepTrade() throws Exception {
        while (isRunning) {
            record = iExc.fetchExcData(currency);
            if (applySettings.apply(record)) {
                submitRequest.submit(new OrderRequest());
                break;
            }
        }
    }

    private void swapSide() {
        currentSide = (currentSide == Side.SELL) ? Side.BUY : Side.SELL;
        applySettings = (currentSide == Side.SELL) ? (Record record) -> buyCheck() : (Record record) -> sellCheck();
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

    private void buyAction(OrderRequest orderRequest) {
    }

    private void sellAction(OrderRequest orderRequest) {
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public TraderState getTraderState() {
        return traderState;
    }

    public void setTraderState(TraderState traderState) {
        this.traderState = traderState;
    }

    public TraderSettings getTraderSettings() {
        return traderSettings;
    }

    public void setTraderSettings(TraderSettings traderSettings) {
        this.traderSettings = traderSettings;
    }
}
