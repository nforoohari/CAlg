package api;

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

public class BinanceNet implements INet {

    private Interval interval;
    private Crypto crypto;
    private String API_KEY;
    private String SECRET_KEY;
    private String BASE_URL;
    private boolean operational;

    DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public BinanceNet(Interval interval, Crypto crypto) {
        this.interval = interval;
        this.crypto = crypto;
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

    public Crypto getCrypto() {
        return crypto;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    public boolean isOperational() {
        return operational;
    }

    public void setOperational(boolean operational) {
        this.operational = operational;
    }

    @Override
    public CryptoOrder buy(Crypto crypto, Double volume, Double price) throws Exception {

        CryptoOrder co = null;
        OrderStatus os = null;

        System.out.println("Buy : " + crypto.getName() + ", Volume : " + volume + ", Price : " + price);

        if (operational) {
            co = placeOrder(crypto.getName() + "USDT", "BUY", volume, price);
            OrderDAO.insertOrderStatus(co.getOrderStatus());
            for (OrderDetails o : co.getDetails()) {
                OrderDAO.insertOrderDetail(o);
            }
        } else {
            co = new CryptoOrder();
            os = new OrderStatus(crypto, "BUY", volume, price, new Date());
            OrderDAO.insertOrderStatus(os);
            co.setOrderStatus(os);
        }
        return co;
    }

    @Override
    public CryptoOrder sell(Crypto crypto, Double volume, Double price) throws Exception {

        CryptoOrder co = null;
        OrderStatus os = null;

        System.out.println("Sell : " + crypto.getName() + ", Volume : " + volume + ", Price : " + price);

        if (operational) {
            co = placeOrder(crypto.getName() + "USDT", "SELL", volume, price);
            OrderDAO.insertOrderStatus(co.getOrderStatus());
            for (OrderDetails o : co.getDetails()) {
                OrderDAO.insertOrderDetail(o);
            }
        } else {
            co = new CryptoOrder();
            os = new OrderStatus(crypto, "SELL", volume, price, new Date());
            OrderDAO.insertOrderStatus(os);
            co.setOrderStatus(os);
        }
        return co;
    }

    @Override
    public CryptoRecord getMarketInfo(Crypto crypto, String interval) throws Exception {

        CryptoRecord cr;

        if (this.crypto != crypto || !this.interval.getName().equals(interval)) {
            return null;
        }
        cr = getKlines(crypto.getName() + "USDT", interval);

        if (!operational) {
            offlineCheckOrderStatus(cr);
        }

        return cr;
    }

    @Override
    public CryptoOrder checkOrderStatus(Crypto crypto, long orderId) throws Exception {
        CryptoOrder co = null;
        OrderStatus os = null;

        if (operational) {
            co = getOrderStatus(crypto.getName() + "USDT", orderId);
            OrderDAO.deleteOrderStatusById(orderId);
            OrderDAO.deleteOrderDetailsByStatusId(orderId);
            OrderDAO.insertOrderStatus(co.getOrderStatus());
            for (OrderDetails o : co.getDetails()) {
                OrderDAO.insertOrderDetail(o);
            }
        } else {
            os = OrderDAO.getOrderById(orderId);
            List<OrderDetails> lod = OrderDAO.getOrderDetails(orderId);
            co = new CryptoOrder();
            co.setOrderStatus(os);
            co.setDetails(lod);
        }
        return co;
    }

    private void offlineCheckOrderStatus(CryptoRecord cr) throws Exception {

        List<OrderStatus> los;
        List<OrderDetails> lod;

        los = OrderDAO.getOrdersByCompletion(cr.getCrypto(),false);

        for (OrderStatus os : los) {

            if ("BUY".equals(os.getSide()) && os.getPrice() > cr.getLow()) {

                lod = OrderDAO.getOrderDetails(os.getId());
                double bv = 0.0;
                double lv = 0.0;
                double ov = os.getVolume();
                double cv = cr.getVolume();
                boolean comp = false;

                for (OrderDetails od : lod) {
                    bv += od.getVolume();
                }

                lv = Math.min((ov - bv), cv);
                comp = (ov - bv) <= cv;

                OrderDAO.insertOrderDetail(new OrderDetails(os.getId(), lv, (cr.getLow() + cr.getClose()) / 2, cr.getDate()));

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

                for (OrderDetails od : lod) {
                    sv += od.getVolume();
                }

                lv = Math.min((ov - sv), cv);
                comp = (ov - sv) <= cv;

                OrderDAO.insertOrderDetail(new OrderDetails(os.getId(), lv, (cr.getHigh() + cr.getClose()) / 2, new Date()));

                if (comp) {
                    OrderDAO.completeOrder(os.getId(),cr.getDate());
                }
            }
        }

    }

    // 🛒 ثبت سفارش
    public CryptoOrder placeOrder(String symbol, String side, double quantity, double price) throws Exception {

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
    public CryptoRecord getKlines(String symbol, String interval) throws Exception {

        List<CryptoRecord> cryptoRecords = new ArrayList<>();

        if (this.crypto != crypto || !this.interval.getName().equals(interval)) return null;

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

            cryptoRecords.add(new CryptoRecord(crypto, new Date(openTimeMs), open, high, low, close, volume));

        }
        return cryptoRecords.get(0);
    }

    public CryptoOrder getOrderStatus(String symbol, long orderId) throws Exception {

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

        BinanceNet bn = new BinanceNet(Interval.OneMinute, Crypto.BBB);
        // دریافت کندل 1 دقیقه اخیر
        bn.getKlines("BTCUSDT", "1m");

        // ثبت سفارش خرید
//        bn.placeOrder("BTCUSDT", "BUY", 0.001, 30000);
    }

}