package api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Lwd {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId tehranZone = ZoneId.of("Asia/Tehran");


    public static List<Rxc> load(String tableName, Oh oh, String startTime, String endTime) throws Exception {

        LocalDateTime startDate = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endTime, formatter);

//        long startMs = startDate.toInstant(ZoneOffset.UTC).toEpochMilli();
//        long endMs = endDate.toInstant(ZoneOffset.UTC).toEpochMilli();

        long startMs = startDate.atZone(tehranZone).toInstant().toEpochMilli();
        long endMs = endDate.atZone(tehranZone).toInstant().toEpochMilli();

        Connection conn = Byd.getConnection();

        String sql = "SELECT * FROM " + tableName + " WHERE (crypto = " + oh.getCode() + ") AND (interval_date BETWEEN ? AND ?) ORDER BY interval_date";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setTimestamp(1, new java.sql.Timestamp(startMs));
        ps.setTimestamp(2, new java.sql.Timestamp(endMs));

        ResultSet rs = ps.executeQuery();

        List<Rxc> list = new ArrayList<>();

        while (rs.next()) {

            list.add(new Rxc(
                    Oh.fromCode(rs.getLong("crypto")),
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
