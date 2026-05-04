package mainPackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import java.time.ZoneId;

public class DBLoader {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId tehranZone = ZoneId.of("Asia/Tehran");


    public static List<CryptoRecord> load(String tableName, Crypto crypto, String startTime, String endTime) throws Exception {

        LocalDateTime startDate = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endTime, formatter);

//        long startMs = startDate.toInstant(ZoneOffset.UTC).toEpochMilli();
//        long endMs = endDate.toInstant(ZoneOffset.UTC).toEpochMilli();

        long startMs = startDate.atZone(tehranZone).toInstant().toEpochMilli();
        long endMs = endDate.atZone(tehranZone).toInstant().toEpochMilli();

        Connection conn = DB.getConnection();

        String sql = "SELECT * FROM " + tableName + " WHERE (crypto = " + crypto.getCode() + ") AND (interval_date BETWEEN ? AND ?) ORDER BY interval_date";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setTimestamp(1, new java.sql.Timestamp(startMs));
        ps.setTimestamp(2, new java.sql.Timestamp(endMs));

        ResultSet rs = ps.executeQuery();

        List<CryptoRecord> list = new ArrayList<>();

        while (rs.next()) {

            list.add(new CryptoRecord(
                    Crypto.fromCode(rs.getLong("crypto")),
                    rs.getTimestamp("interval_date"),
                    rs.getDouble("open"),
                    rs.getDouble("high"),
                    rs.getDouble("low"),
                    rs.getDouble("close"),
                    rs.getDouble("volume")
            ));
        }

        conn.close();

        return list;
    }
}
