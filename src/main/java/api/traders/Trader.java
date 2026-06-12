package api.traders;

import api.daos.Record;
import api.daos.TraderDao;
import api.daos.TraderSettingsDao;
import api.daos.TraderStateDao;
import api.enums.*;
import api.exchanges.NonOprBinance;
import api.exchanges.OprBinance;
import api.exchanges.IExc;
import api.exchanges.MyExc;
import api.orders.OrderRequest;

import java.sql.SQLException;
import java.util.Date;

public class Trader extends Thread {

    private long id;
    private Exchange exchange;
    private double fee;
    private Interval interval;
    private Currency currency;
    private String startTime;
    private String endTime;
    private Date date;

    private IExc iExc;
    private boolean isRunning;
    private Record record;

    private TraderState traderState;
    private TraderSettings traderSettings;


    public Trader() {
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

    public Trader(Exchange exchange, double fee, Interval interval, Currency currency) throws Exception {
        this(exchange, fee, interval, currency, "", "");
    }


    protected void init() throws Exception {
        assert exchange != null;
        iExc = switch (exchange) {
            case MyExc -> new MyExc(exchange, fee, interval, currency, startTime, endTime);
            case Binance_MainNet_NonOpr, Binance_TestNet_NonOpr -> new NonOprBinance(exchange, fee, interval, currency);
            case Binance_MainNet_Opr, Binance_TestNet_Opr -> new OprBinance(exchange, fee, interval, currency);
        };

        isRunning = true;
        record = null;

        TraderDao.insert(this);

    }

    public void run() {
        {

            System.out.println("Trading started at : " + new Date() + "\n");

            while (isRunning) {

                try {

                    traderSettings.setDate(new Date());
                    traderState.setDate(new Date());
                    TraderSettingsDao.insert(traderSettings);
                    TraderStateDao.insert(traderState);
                    printAllSettings();
                    printAllState();

                    trade();

                    if (isRunning) changeSettings();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }

            System.out.println("Trading stopped at : " + new Date() + "\n");
            System.out.println("****************** The Final State ****************");

            try {
                traderState.setDate(new Date());
                TraderStateDao.insert(traderState);
                printAllState();

                printResult();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void printAllSettings() {
        System.out.println("********************** Settings *******************");
        System.out.println("bidPrice : " + formatter(traderSettings.bidPrice));
        System.out.println("askPrice : " + formatter(traderSettings.askPrice));
        System.out.println("lowerStopLossPrice : " + formatter(traderSettings.lowerStopLossPrice));
        System.out.println("upperStopLossPrice : " + formatter(traderSettings.upperStopLossPrice));
        System.out.println("changePercent : " + formatter(traderSettings.changePercent));
        System.out.println("topFix : " + traderSettings.topFix);
        System.out.println("bottomFix : " + traderSettings.bottomFix);
        System.out.println();
    }

    public void printAllState() {
        System.out.println("*********************** State *********************");
        System.out.println("volume : " + formatter(traderState.volume));
        System.out.println("balance : " + formatter(traderState.balance));
        System.out.println("payedFee  : " + formatter(traderState.payedFee));
        System.out.println("stopLossEnable : " + traderState.stopLossEnable);
        System.out.println();
    }

    public void printResult() throws SQLException {

        TraderState initialState = TraderStateDao.findFirstByTraderId(this.getId());
        TraderState finalState = traderState;

        System.out.println("********************** Result ********************");
        System.out.println("Initial Volume : " + formatter(initialState.volume) + "  ,  Final Volume : " + formatter(finalState.volume) + "  , Growth Rate: " + growthRateMaker(initialState.volume, finalState.volume));
        System.out.println("Initial Balance : " + formatter(initialState.balance) + "  ,  Final Balance : " + formatter(finalState.balance) + "  , Growth Rate: " + growthRateMaker(initialState.balance, finalState.balance));
        System.out.println("PayedFee : " + formatter(finalState.payedFee));
        System.out.println("stopLossEnable : " + finalState.stopLossEnable);
        System.out.println();
    }

    private double formatter(double value) {
        return (((double) Math.round(value * 100)) / 100);
    }

    private double growthRateMaker(double initialValue, double finalValue) {
        if (initialValue > 0) return formatter(((finalValue / initialValue) - 1) * 100);
        else return -1;
    }

    protected void changeSettings() {
    }


    private void trade() throws Exception {

        OrderRequest orderRequest = null;
        while (isRunning) {
            if (exchange != Exchange.MyExc) Thread.sleep(interval.getMillis());
            if ((record = iExc.fetchExcData()) == null) {
                isRunning = false;
                break;
            }
            if ((orderRequest = marketCheck(record)) != null) {
                iExc.submitByConfirmation(orderRequest, RetryTimes.Normal);
                updateTraderState(orderRequest);
                if (traderState.stopLossEnable) isRunning = false;
                break;
            }
        }
    }

    public void stopTrading() {
        isRunning = false;
    }

    private OrderRequest marketCheck(Record record) {

        if (traderState.balance > 0) {
            if (record.getClose() < traderSettings.bidPrice && record.getClose() >= traderSettings.lowerStopLossPrice) {
                return createOrderRequest(Side.BUY, traderSettings.bidPrice);
            } else if (record.getClose() > traderSettings.upperStopLossPrice) {
                traderState.stopLossEnable = true;
                return createOrderRequest(Side.BUY, traderSettings.upperStopLossPrice);
            }
        }

        if (traderState.volume > 0) {
            if (record.getClose() > traderSettings.askPrice && record.getClose() <= traderSettings.upperStopLossPrice) {
                return createOrderRequest(Side.SELL, traderSettings.askPrice);
            } else if (record.getClose() < traderSettings.lowerStopLossPrice) {
                traderState.stopLossEnable = true;
                return createOrderRequest(Side.SELL, traderSettings.lowerStopLossPrice);
            }
        }

        return null;

    }

    private void updateTraderState(OrderRequest orderRequest) {

        Side side = orderRequest.getSide();
        if (side == Side.BUY) {
            traderState.balance = orderRequest.getState().getBalance();
            traderState.payedFee += orderRequest.getState().getPayedFee();
            traderState.volume += orderRequest.getState().getVolume();
        } else {
            traderState.balance += orderRequest.getState().getBalance();
            traderState.payedFee += orderRequest.getState().getPayedFee();
            traderState.volume = orderRequest.getState().getVolume();
        }

    }

    private OrderRequest createOrderRequest(Side side, double price) {
        double capital = side == Side.BUY ? traderState.balance : traderState.volume;
        return new OrderRequest(exchange, currency, side, capital, price, fee);
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
