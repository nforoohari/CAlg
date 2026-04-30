package dataInserter;

import dataPresenter.Candle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CandleRepository {

    public static List<Candle> load(long start, long end) throws Exception {

        Connection conn = DB.getConnection();

        String sql =
                "SELECT * FROM eth_usdt_1s WHERE open_time BETWEEN ? AND ? ORDER BY open_time";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setLong(1,start);
        ps.setLong(2,end);

        ResultSet rs = ps.executeQuery();

        List<Candle> list = new ArrayList<>();

        while(rs.next()){

            list.add(new Candle(
                    rs.getLong("open_time"),
                    rs.getDouble("open_price"),
                    rs.getDouble("high_price"),
                    rs.getDouble("low_price"),
                    rs.getDouble("close_price"),
                    rs.getDouble("volume")
            ));
        }

        conn.close();

        return list;
    }
}
