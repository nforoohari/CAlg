package api.exchanges;

import api.daos.OrderRequestDao;
import api.daos.OrderStateDao;
import api.daos.OrderTransactionDao;
import api.daos.Record;
import api.enums.*;
import api.orders.OrderRequest;
import api.orders.OrderStatus;
import api.orders.OrderTransaction;
import org.json.JSONArray;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NonOprBinance implements IExc {

    private static final String MAIN_NET_BASE_URL = "https://api.binance.com";

    private static final String TEST_NET_BASE_URL = "https://testnet.binance.vision";

    private String BASE_URL;
    private String END_POINT;

    private Exchange exchange;
    private double fee;
    private Interval interval;
    private Currency currency;

    public NonOprBinance(Exchange exchange, double fee, Interval interval, Currency currency) {
        this.exchange = exchange;
        this.fee = fee;
        this.interval = interval;
        this.currency = currency;

        switch (exchange) {
            case Binance_MainNet_NonOpr -> {
                this.BASE_URL = MAIN_NET_BASE_URL;
            }
            case Binance_TestNet_NonOpr -> {
                this.BASE_URL = TEST_NET_BASE_URL;
            }
        }
        this.END_POINT = this.BASE_URL + "/api/v3/klines?symbol=" + currency.getSymbol() + "&interval=" + interval.getName() + "&limit=2";
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
    public OrderStatus cancel(long orderRequestId) throws Exception {
        return OrderStateDao.cancel(orderRequestId);
    }

    @Override
    public void submitByConfirmation(OrderRequest orderRequest, RetryTimes retryTimes) throws Exception {

        int n = 0;

        submit(orderRequest);
        Thread.sleep(interval.getMillis());
        Record record = fetchExcData();

        while (n < retryTimes.getValue() && record != null && orderRequest.getState().getStatus() == Status.In_Progress) {

            offlineCheckOrderStatus(orderRequest, record);
            Thread.sleep(interval.getMillis());
            record = fetchExcData();
            n++;
        }
        if (orderRequest.getState().getStatus() == Status.In_Progress) {

            OrderStatus orderStatus = cancel(orderRequest.getId());
            orderRequest.getState().setStatus(orderStatus.getStatus());
            orderRequest.getState().setStatusDate(orderStatus.getStatusDate());
        }
    }

    @Override
    public Record fetchExcData() throws Exception {

        List<Record> records = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(END_POINT)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("HTTP Error: " + response.statusCode());
        }

        // --- دریافت JSON ---
        JSONArray json = new JSONArray(response.body());

        for (int i = 0; i < json.length(); i++) {

            JSONArray rowJson = json.getJSONArray(i);

            long openTimeMs = rowJson.getLong(0);
            double open = rowJson.getDouble(1);
            double high = rowJson.getDouble(2);
            double low = rowJson.getDouble(3);
            double close = rowJson.getDouble(4);
            double volume = rowJson.getDouble(5);

            records.add(new Record(currency, new Date(openTimeMs), open, high, low, close, volume));

        }
        return records.getFirst();
    }


    private void offlineCheckOrderStatus(OrderRequest orderRequest, Record record) throws Exception {

        OrderTransaction newOrderTransaction = new OrderTransaction(orderRequest.getState().getRequestId(), orderRequest.getState().getOrderId());

        if (orderRequest.getState().getStatus() == Status.In_Progress) {

            if (orderRequest.getSide() == Side.BUY && orderRequest.getState().getBalance() > 0) {
                if (orderRequest.getPrice() > record.getLow()) {

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
                    newOrderTransaction.setPayedFee(Math.min(bought_volume, record.getVolume()) * fee);
                    newOrderTransaction.setDate(new Date());

                    orderRequest.getTransactions().add(newOrderTransaction);
                    OrderTransactionDao.insert(newOrderTransaction);
                    OrderStateDao.update(orderRequest.getState());
                }
            }

            if (orderRequest.getSide() == Side.SELL && orderRequest.getState().getVolume() > 0) {
                if (orderRequest.getPrice() < record.getHigh()) {

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
                    newOrderTransaction.setPayedFee(Math.min(volume, record.getVolume()) * fee);
                    newOrderTransaction.setDate(new Date());

                    orderRequest.getTransactions().add(newOrderTransaction);
                    OrderTransactionDao.insert(newOrderTransaction);
                    OrderStateDao.update(orderRequest.getState());
                }
            }

        }

    }

}