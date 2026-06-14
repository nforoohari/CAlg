package api.exchanges;

import api.daos.OrderRequestDao;
import api.daos.OrderStateDao;
import api.daos.Record;
import api.enums.*;
import api.orders.OrderRequest;
import api.orders.OrderStatus;
import api.traders.Trader;
import org.json.JSONArray;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OprBinance implements IExc {

    private static final String MAIN_NET_API_KEY = "YOUR_API_KEY";
    private static final String MAIN_NET_SECRET_KEY = "YOUR_SECRET_KEY";
    private static final String MAIN_NET_BASE_URL = "https://api.binance.com";

    private static final String TEST_NET_API_KEY = "YOUR_API_KEY";
    private static final String TEST_NET_SECRET_KEY = "YOUR_SECRET_KEY";
    private static final String TEST_NET_BASE_URL = "https://testnet.binance.vision";

    private String API_KEY;
    private String SECRET_KEY;
    private String BASE_URL;
    private String END_POINT;

    private Trader trader;
    private Exchange exchange;
    private double fee;
    private Interval interval;
    private Currency currency;


    public OprBinance(Trader trader, Exchange exchange, double fee, Interval interval, Currency currency) {
        this.trader = trader;
        this.exchange = exchange;
        this.fee = fee;
        this.interval = interval;
        this.currency = currency;

        switch (exchange) {
            case Binance_MainNet_Opr -> {
                this.API_KEY = MAIN_NET_API_KEY;
                this.SECRET_KEY = MAIN_NET_SECRET_KEY;
                this.BASE_URL = MAIN_NET_BASE_URL;
            }
            case Binance_TestNet_Opr -> {
                this.API_KEY = TEST_NET_API_KEY;
                this.SECRET_KEY = TEST_NET_SECRET_KEY;
                this.BASE_URL = TEST_NET_BASE_URL;
            }
        }
        this.END_POINT = this.BASE_URL + "/api/v3/klines?symbol=" + currency.getSymbol() + "&interval=" + interval.getName() + "&limit=2";

    }

    @Override
    public void submit(OrderRequest orderRequest) throws Exception {
        OrderRequest newOrderRequest = placeOrder(orderRequest.getCurrency().getSymbol(), orderRequest.getSide().getName(), orderRequest.getCapital(), orderRequest.getPrice());
        // merge newOrderRequest into orderRequest
        OrderRequestDao.insert(orderRequest);
        orderRequest.getState().setRequestId(orderRequest.getId());
        OrderStateDao.insert(orderRequest.getState());
    }

    @Override
    public OrderRequest checkStatus(long orderRequestId) throws Exception {
        //extract orderId from orderRequest based on orderRequestId
        //OrderRequest newOrderRequest = getOrderStatus(currency.getSymbol(),orderId);
        // merge newOrderRequest into orderRequest
        // Update orderRequest on DB
        //return orderRequest;
        return null;
    }


    @Override
    public OrderStatus terminate(long orderRequestId) throws Exception {
        //extract orderId from orderRequest based on orderRequestId
        //cancelOrder(currency.getSymbol(),orderId);
        // Update orderRequest on DB
        //return orderStatus;
        return null;
    }

    @Override
    public OrderStatus cancel(long orderRequestId) throws Exception {
        //extract orderId from orderRequest based on orderRequestId
        //cancelOrder(currency.getSymbol(),orderId);
        // Update orderRequest on DB
        //return orderStatus;
        return null;
    }

    @Override
    public void submitByConfirmation(OrderRequest orderRequest, RetryTimes retryTimes) throws Exception {

        int n = 0;

        submit(orderRequest);

        while (n < retryTimes.getValue() && orderRequest.getState().getStatus() == Status.In_Progress) {

            Thread.sleep(SleepTime.Binance.getMillis());
            OrderRequest newOrderRequest = checkStatus(orderRequest.getId());
            // merge newOrderRequest into orderRequest
            n++;
        }
        if (orderRequest.getState().getStatus() == Status.In_Progress) {

            OrderStatus orderStatus = terminate(orderRequest.getId());
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


    public OrderRequest placeOrder(String symbol, String side, double quantity, double price) throws Exception {

        long timestamp = System.currentTimeMillis();

        String query = "symbol=" + symbol + "&side=" + side + "&type=LIMIT" + "&timeInForce=GTC" + "&quantity=" + quantity + "&price=" + price + "&timestamp=" + timestamp;

        String signature = hmacSHA256(query, SECRET_KEY);

        String fullUrl = BASE_URL + "/api/v3/order?" + query + "&signature=" + signature;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fullUrl)).header("X-MBX-APIKEY", API_KEY).POST(HttpRequest.BodyPublishers.noBody()).build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Order Response: " + response.body());

        return null;
    }

    // 🔐 امضای درخواست
    public String hmacSHA256(String data, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKey);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            String s = Integer.toHexString(0xff & b);
            if (s.length() == 1) hex.append('0');
            hex.append(s);
        }
        return hex.toString();
    }

    public OrderRequest getOrderStatus(String symbol, long orderId) throws Exception {

        long timestamp = System.currentTimeMillis();

        String query = "symbol=" + symbol + "&orderId=" + orderId + "&timestamp=" + timestamp;

        String signature = hmacSHA256(query, SECRET_KEY);

        String url = BASE_URL + "/api/v3/order?" + query + "&signature=" + signature;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("X-MBX-APIKEY", API_KEY).GET().build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Order OrderStatus: " + response.body());

        return null;
    }

    public void cancelOrder(String symbol, long orderId) throws Exception {

        long timestamp = System.currentTimeMillis();

        String query = "symbol=" + symbol +
                "&orderId=" + orderId +
                "&timestamp=" + timestamp;

        String signature = hmacSHA256(query, SECRET_KEY);

        String url = BASE_URL + "/api/v3/order?" +
                query + "&signature=" + signature;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-MBX-APIKEY", API_KEY)
                .DELETE()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Cancel Response: " + response.body());
    }

}