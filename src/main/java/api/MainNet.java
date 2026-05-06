package api;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class MainNet implements NetInterface {

    private static final String API_KEY = "YOUR_API_KEY";
    private static final String SECRET_KEY = "YOUR_SECRET_KEY";
    private static final String BASE_URL = "https://api.binance.com";

    @Override
    public String buy(Crypto crypto, Double volume, Double price) throws Exception {

        return placeOrder(crypto.getName() + "USDT", "BUY", volume, price);
    }

    @Override
    public String sell(Crypto crypto, Double volume, Double price) throws Exception {

        return placeOrder(crypto.getName() + "USDT", "SELL", volume, price);
    }

    @Override
    public CryptoRecord getMarketInfo(Crypto crypto, String interval) throws Exception {
        getKlines(crypto.getName()+"USDT", interval);
        return null;
    }

    @Override
    public OrderStatus checkOrderStatus(Crypto crypto, long orderId) throws Exception {
        getOrderStatus(crypto.getName()+"USDT",orderId);
        return null;
    }


    // 🛒 ثبت سفارش
    public static String placeOrder(String symbol, String side, double quantity, double price) throws Exception {

        long timestamp = System.currentTimeMillis();

        String query = "symbol=" + symbol +
                "&side=" + side +
                "&type=LIMIT" +
                "&timeInForce=GTC" +
                "&quantity=" + quantity +
                "&price=" + price +
                "&timestamp=" + timestamp;

        String signature = hmacSHA256(query, SECRET_KEY);

        String fullUrl = BASE_URL + "/api/v3/order?" + query + "&signature=" + signature;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .header("X-MBX-APIKEY", API_KEY)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Order Response: " + response.body());

        return ("Order Response: " + response.body());
    }

    // 🔐 امضای درخواست
    public static String hmacSHA256(String data, String key) throws Exception {
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
    public static void getKlines(String symbol, String interval) throws Exception {
        String endpoint = "/api/v3/klines?symbol=" + symbol + "&interval="+interval+"&limit=1";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Kline Data: " + response.body());
    }

    public static void getOrderStatus(String symbol, long orderId) throws Exception {

        long timestamp = System.currentTimeMillis();

        String query = "symbol=" + symbol +
                "&orderId=" + orderId +
                "&timestamp=" + timestamp;

        String signature = hmacSHA256(query, SECRET_KEY);

        String url = BASE_URL + "/api/v3/order?" + query + "&signature=" + signature;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-MBX-APIKEY", API_KEY)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Order Status: " + response.body());
    }

    public static void main(String[] args) throws Exception {

        // دریافت کندل 1 دقیقه اخیر
        getKlines("BTCUSDT","1m");

        // ثبت سفارش خرید
        placeOrder("BTCUSDT", "BUY", 0.001, 30000);
    }

}