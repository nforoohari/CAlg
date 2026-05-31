package api.exchanges;

import api.daos.Record;
import api.enums.Currency;
import api.enums.Interval;
import api.daos.OrderDAO;
import api.orders.OrderRequest;
import api.orders.OrderState;
import api.orders.OrderTransaction;
import org.json.JSONArray;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Binance implements IExc {

    private Interval interval;
    private Currency currency;
    private String API_KEY;
    private String SECRET_KEY;
    private String BASE_URL;
    private boolean operational;

    DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Binance(Interval interval, Currency currency) {
        this.interval = interval;
        this.currency = currency;
        this.API_KEY = "YOUR_API_KEY";
        this.SECRET_KEY = "YOUR_SECRET_KEY";
        this.BASE_URL = "https://testnet.binance.vision"; //"https://api.binance.com"
        this.operational = false;
    }

    public String getAPI_KEY() {
        return API_KEY;
    }

    public void setAPI_KEY(String API_KEY) {
        this.API_KEY = API_KEY;
    }

    public String getSECRET_KEY() {
        return SECRET_KEY;
    }

    public void setSECRET_KEY(String SECRET_KEY) {
        this.SECRET_KEY = SECRET_KEY;
    }

    public String getBASE_URL() {
        return BASE_URL;
    }

    public void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public Currency getCrypto() {
        return currency;
    }

    public void setCrypto(Currency currency) {
        this.currency = currency;
    }

    public boolean isOperational() {
        return operational;
    }

    public void setOperational(boolean operational) {
        this.operational = operational;
    }

    @Override
    public OrderRequest buy(Currency currency, Double volume, Double price) throws Exception {

        OrderRequest co = null;
        OrderState os = null;

        System.out.println("Buy : " + currency.getName() + ", Volume : " + volume + ", Price : " + price);

        if (operational) {
            co = placeOrder(currency.getName() + "USDT", "BUY", volume, price);
            OrderDAO.insertOrderStatus(co.getOrderStatus());
            for (OrderTransaction o : co.getTransactions()) {
                OrderDAO.insertOrderDetail(o);
            }
        } else {
            co = new OrderRequest();
            os = new OrderState(currency, "BUY", volume, price, new Date());
            OrderDAO.insertOrderStatus(os);
            co.setOrderStatus(os);
        }
        return co;
    }

    @Override
    public OrderRequest sell(Currency currency, Double volume, Double price) throws Exception {

        OrderRequest co = null;
        OrderState os = null;

        System.out.println("Sell : " + currency.getName() + ", Volume : " + volume + ", Price : " + price);

        if (operational) {
            co = placeOrder(currency.getName() + "USDT", "SELL", volume, price);
            OrderDAO.insertOrderStatus(co.getOrderStatus());
            for (OrderTransaction o : co.getTransactions()) {
                OrderDAO.insertOrderDetail(o);
            }
        } else {
            co = new OrderRequest();
            os = new OrderState(currency, "SELL", volume, price, new Date());
            OrderDAO.insertOrderStatus(os);
            co.setOrderStatus(os);
        }
        return co;
    }

    @Override
    public Record fetchTradeData(Currency currency, String interval) throws Exception {

        Record cr;

        if (this.currency != currency || !this.interval.getName().equals(interval)) {
            return null;
        }
        cr = getKlines(currency.getName() + "USDT", interval);

        if (!operational) {
            offlineCheckOrderStatus(cr);
        }

        return cr;
    }

    @Override
    public OrderRequest checkOrderStatus(Currency currency, long orderId) throws Exception {
        OrderRequest co = null;
        OrderState os = null;

        if (operational) {
            co = getOrderStatus(currency.getName() + "USDT", orderId);
            OrderDAO.deleteOrderStatusById(orderId);
            OrderDAO.deleteOrderDetailsByStatusId(orderId);
            OrderDAO.insertOrderStatus(co.getOrderStatus());
            for (OrderTransaction o : co.getTransactions()) {
                OrderDAO.insertOrderDetail(o);
            }
        } else {
            os = OrderDAO.getOrderById(orderId);
            List<OrderTransaction> lod = OrderDAO.getOrderDetails(orderId);
            co = new OrderRequest();
            co.setOrderStatus(os);
            co.setTransactions(lod);
        }
        return co;
    }

    private void offlineCheckOrderStatus(Record cr) throws Exception {

        List<OrderState> los;
        List<OrderTransaction> lod;

        los = OrderDAO.getOrdersByCompletion(cr.getCrypto(),false);

        for (OrderState os : los) {

            if ("BUY".equals(os.getSide()) && os.getPrice() > cr.getLow()) {

                lod = OrderDAO.getOrderDetails(os.getId());
                double bv = 0.0;
                double lv = 0.0;
                double ov = os.getVolume();
                double cv = cr.getVolume();
                boolean comp = false;

                for (OrderTransaction od : lod) {
                    bv += od.getVolume();
                }

                lv = Math.min((ov - bv), cv);
                comp = (ov - bv) <= cv;

                OrderDAO.insertOrderDetail(new OrderTransaction(os.getId(), lv, (cr.getLow() + cr.getClose()) / 2, cr.getDate()));

                if (comp) {
                    OrderDAO.completeOrder(os.getId(),cr.getDate());
                }

            } else if ("SELL".equals(os.getSide()) && os.getPrice() < cr.getHigh()) {

                lod = OrderDAO.getOrderDetails(os.getId());
                double sv = 0.0;
                double lv = 0.0;
                double ov = os.getVolume();
                double cv = cr.getVolume();
                boolean comp = false;

                for (OrderTransaction od : lod) {
                    sv += od.getVolume();
                }

                lv = Math.min((ov - sv), cv);
                comp = (ov - sv) <= cv;

                OrderDAO.insertOrderDetail(new OrderTransaction(os.getId(), lv, (cr.getHigh() + cr.getClose()) / 2, new Date()));

                if (comp) {
                    OrderDAO.completeOrder(os.getId(),cr.getDate());
                }
            }
        }

    }

    // 🛒 ثبت سفارش
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

    // 📊 دریافت اطلاعات کندل (قیمت و حجم)
    public Record getKlines(String symbol, String interval) throws Exception {

        List<Record> records = new ArrayList<>();

        if (this.currency != currency || !this.interval.getName().equals(interval)) return null;

        String endpoint = "/api/v3/klines?symbol=" + symbol + "&interval=" + interval + "&limit=2";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + endpoint)).GET().build();

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
        return records.get(0);
    }

    public OrderRequest getOrderStatus(String symbol, long orderId) throws Exception {

        long timestamp = System.currentTimeMillis();

        String query = "symbol=" + symbol + "&orderId=" + orderId + "&timestamp=" + timestamp;

        String signature = hmacSHA256(query, SECRET_KEY);

        String url = BASE_URL + "/api/v3/order?" + query + "&signature=" + signature;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("X-MBX-APIKEY", API_KEY).GET().build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Order Status: " + response.body());

        return null;
    }

    public static void main(String[] args) throws Exception {

        Binance bn = new Binance(Interval.OneMinute, Currency.Bitcoin);
        // دریافت کندل 1 دقیقه اخیر
        bn.getKlines("BTCUSDT", "1m");

        // ثبت سفارش خرید
//        bn.placeOrder("BTCUSDT", "BUY", 0.001, 30000);
    }

}