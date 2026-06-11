package api.daos;

import api.enums.Currency;
import api.enums.Interval;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RecordDao {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    private static final ZoneId tehranZone = ZoneId.of("Asia/Tehran");

    public static List<Record> load(Interval interval, Currency currency, String startTime, String endTime) throws Exception {

        LocalDateTime startDate = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endTime, formatter);

        long startMs = startDate.toInstant(ZoneOffset.UTC).toEpochMilli();
        long endMs = endDate.toInstant(ZoneOffset.UTC).toEpochMilli();

//        long startMs = startDate.atZone(tehranZone).toInstant().toEpochMilli();
//        long endMs = endDate.atZone(tehranZone).toInstant().toEpochMilli();

        Connection conn = DB.getConnection();

        String sql = "SELECT * FROM " + interval.getTableName() + " WHERE (currency = ?) AND (interval_date BETWEEN ? AND ?) ORDER BY interval_date";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setInt(1, currency.getCode());
        ps.setTimestamp(2, new java.sql.Timestamp(startMs));
        ps.setTimestamp(3, new java.sql.Timestamp(endMs));
//        ps.setString(2, startTime);
//        ps.setString(3, endTime);

        ResultSet rs = ps.executeQuery();

        List<Record> list = new ArrayList<>();

        while (rs.next()) {

            list.add(new Record(
                    rs.getLong("id"),
                    Currency.fromCode(rs.getInt("currency")),
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
