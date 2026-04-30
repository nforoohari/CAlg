package dataInserter;

import org.json.JSONArray;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class BinanceToMySQL {

    public static void main(String[] args) throws Exception {

        String url =
                "https://api.binance.com/api/v3/klines?symbol=ETHUSDT&interval=1s&limit=1000";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONArray data = new JSONArray(response.body());

        Connection conn = DB.getConnection();

        String sql =
                "INSERT INTO eth_usdt_1s(open_time,open_price,high_price,low_price,close_price,volume) VALUES (?,?,?,?,?,?)";

        PreparedStatement ps = conn.prepareStatement(sql);

        for (int i = 0; i < data.length(); i++) {

            JSONArray k = data.getJSONArray(i);

            ps.setLong(1, k.getLong(0));
            ps.setDouble(2, Double.parseDouble(k.getString(1)));
            ps.setDouble(3, Double.parseDouble(k.getString(2)));
            ps.setDouble(4, Double.parseDouble(k.getString(3)));
            ps.setDouble(5, Double.parseDouble(k.getString(4)));
            ps.setDouble(6, Double.parseDouble(k.getString(5)));

            ps.addBatch();
        }

        ps.executeBatch();

        conn.close();

        System.out.println("Data inserted into MySQL");
    }
}
