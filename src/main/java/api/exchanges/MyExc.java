package api.exchanges;

import api.daos.*;
import api.daos.Record;
import api.enums.*;
import api.orders.OrderRequest;
import api.orders.OrderStatus;
import api.orders.OrderTransaction;
import api.traders.Trader;

import java.util.Date;
import java.util.List;

public class MyExc implements IExc {

    private Trader trader;
    private Exchange exchange;
    private double fee;
    private Interval interval;
    private Currency currency;
    private String startTime;
    private String endTime;

    private List<Record> lcr;
    private int lcrSize;
    private int cnt;

    public MyExc(Trader trader, Exchange exchange, double fee, Interval interval, Currency currency, String startTime, String endTime) throws Exception {
        this.trader = trader;
        this.exchange = exchange;
        this.fee = fee;
        this.interval = interval;
        this.currency = currency;
        this.startTime = startTime;
        this.endTime = endTime;

        this.lcr = RecordDao.load(interval, currency, startTime, endTime);
        this.lcrSize = lcr.size();
        this.cnt = 0;

    }

    @Override
    public void submit(OrderRequest orderRequest) throws Exception {
        OrderRequestDao.insert(orderRequest);
        orderRequest.getState().setRequestId(orderRequest.getId());
        OrderStateDao.insert(orderRequest.getState());
    }

    @Override
    public OrderRequest checkStatus(long orderRequestId) throws Exception {
        OrderRequest orderRequest = OrderRequestDao.getById(orderRequestId);
        assert orderRequest != null;
        orderRequest.setState(OrderStateDao.getByRequestId(orderRequestId));
        orderRequest.setTransactions(OrderTransactionDao.getByRequestId(orderRequestId));
        return orderRequest;
    }

    @Override
    public OrderStatus terminate(long orderRequestId) throws Exception {
        return OrderStateDao.terminate(orderRequestId);
    }

    @Override
    public OrderStatus cancel(long orderRequestId) throws Exception {
        return OrderStateDao.cancel(orderRequestId);
    }

    @Override
    public void submitByConfirmation(OrderRequest orderRequest, RetryTimes retryTimes) throws Exception {

        int n = 0;
        Record record = null;

        submit(orderRequest);

        do {
            record = fetchExcData();
        } while (trader.lastRecord != null && (trader.lastRecord.getDate().toString()).equals(record.getDate().toString()));
        trader.lastRecord = record;

        while (n < retryTimes.getValue() && record != null && orderRequest.getState().getStatus() == Status.In_Progress) {

            offlineCheckOrderStatus(orderRequest, record);

            if (orderRequest.getState().getStatus() == Status.In_Progress) {
                do {
                    record = fetchExcData();
                } while (trader.lastRecord != null && (trader.lastRecord.getDate().toString()).equals(record.getDate().toString()));
                trader.lastRecord = record;

                n++;
            }
        }
        if (orderRequest.getState().getStatus() == Status.In_Progress) {

            OrderStatus orderStatus = terminate(orderRequest.getId());
            orderRequest.getState().setStatus(orderStatus.getStatus());
            orderRequest.getState().setStatusDate(orderStatus.getStatusDate());
        }
    }

    @Override
    public Record fetchExcData() throws Exception {
        return ((cnt < lcrSize) ? lcr.get(cnt++) : null);
    }

    private void offlineCheckOrderStatus(OrderRequest orderRequest, Record record) throws Exception {

        OrderTransaction newOrderTransaction = new OrderTransaction(orderRequest.getState().getRequestId(), orderRequest.getState().getOrderId());

        if (orderRequest.getState().getStatus() == Status.In_Progress) {

            if (orderRequest.getSide() == Side.BUY && orderRequest.getState().getBalance() > 0) {
                if (orderRequest.getPrice() > record.getClose()) {

                    double price = orderRequest.getPrice();
                    double fee = orderRequest.getFee();

                    double balance = orderRequest.getState().getBalance();
                    double volume = orderRequest.getState().getVolume();
                    double payedFee = orderRequest.getState().getPayedFee();

                    double price_and_fee = price * (1 + (fee / 100));
                    double bought_volume = balance / price_and_fee;


                    if (bought_volume <= record.getVolume()) {

                        volume += bought_volume;
                        payedFee += (bought_volume * price * (fee / 100));
                        balance = 0;
                        orderRequest.getState().setStatus(Status.Completed);
                        orderRequest.getState().setStatusDate(new Date());

                    } else {

                        volume += record.getVolume();
                        payedFee += (record.getVolume() * price * (fee / 100));
                        balance -= (record.getVolume() * price_and_fee);

                    }
                    orderRequest.getState().setVolume(volume);
                    orderRequest.getState().setPayedFee(payedFee);
                    orderRequest.getState().setBalance(balance);

                    newOrderTransaction.setSide(Side.BUY);
                    newOrderTransaction.setPrice(price);
                    newOrderTransaction.setFee(fee);
                    newOrderTransaction.setVolume(Math.min(bought_volume, record.getVolume()));
                    newOrderTransaction.setBalance(Math.min(bought_volume, record.getVolume()) * price_and_fee);
                    newOrderTransaction.setPayedFee(Math.min(bought_volume, record.getVolume()) * price * (fee / 100));
                    newOrderTransaction.setDate(new Date());

                    orderRequest.getTransactions().add(newOrderTransaction);
                    OrderTransactionDao.insert(newOrderTransaction);
                    OrderStateDao.update(orderRequest.getState());
                }
            }

            if (orderRequest.getSide() == Side.SELL && orderRequest.getState().getVolume() > 0) {
                if (orderRequest.getPrice() < record.getClose()) {

                    double price = orderRequest.getPrice();
                    double fee = orderRequest.getFee();

                    double balance = orderRequest.getState().getBalance();
                    double volume = orderRequest.getState().getVolume();
                    double payedFee = orderRequest.getState().getPayedFee();

                    double price_minus_fee = price * (1 - (fee / 100));

                    if (volume <= record.getVolume()) {

                        payedFee += (volume * price * (fee / 100));
                        balance += volume * price_minus_fee;
                        volume = 0;
                        orderRequest.getState().setStatus(Status.Completed);
                        orderRequest.getState().setStatusDate(new Date());

                    } else {

                        payedFee += (record.getVolume() * price * (fee / 100));
                        balance += (record.getVolume() * price_minus_fee);
                        volume -= record.getVolume();

                    }
                    orderRequest.getState().setVolume(volume);
                    orderRequest.getState().setPayedFee(payedFee);
                    orderRequest.getState().setBalance(balance);

                    newOrderTransaction.setSide(Side.SELL);
                    newOrderTransaction.setPrice(price);
                    newOrderTransaction.setFee(fee);
                    newOrderTransaction.setVolume(Math.min(volume, record.getVolume()));
                    newOrderTransaction.setBalance(Math.min(volume, record.getVolume()) * price_minus_fee);
                    newOrderTransaction.setPayedFee(Math.min(volume, record.getVolume()) * price * (fee / 100));
                    newOrderTransaction.setDate(new Date());

                    orderRequest.getTransactions().add(newOrderTransaction);
                    OrderTransactionDao.insert(newOrderTransaction);
                    OrderStateDao.update(orderRequest.getState());
                }
            }

        }

    }
}
