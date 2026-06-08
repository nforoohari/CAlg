package mainPackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBInserter {

    private final String tableName;
    private final Connection conn;
    private final String sql;
    private final PreparedStatement ps;
    private int counter;

    public DBInserter(String tableName) throws Exception {
        this.tableName = tableName;
        this.conn = DB.getConnection();
        this.sql = "INSERT INTO " + this.tableName + "(currency,interval_date,open,high,low,close,volume) VALUES (?,?,?,?,?,?,?)";
        this.ps = conn.prepareStatement(sql);
        this.counter = 0;
    }

    public void batchAddAndInsert(Record record) throws SQLException {

        if (counter < 1000 && record != null) {

            ps.setLong(1, record.getCurrency().getCode());
            ps.setTimestamp(2, new java.sql.Timestamp(record.getDate().getTime()));
            ps.setDouble(3, record.getOpen());
            ps.setDouble(4, record.getHigh());
            ps.setDouble(5, record.getLow());
            ps.setDouble(6, record.getClose());
            ps.setDouble(7, record.getVolume());

            ps.addBatch();
            ++counter;

        }

        if (counter == 1000 && record != null) {
            ps.executeBatch();
            counter = 0;
        } else if (counter < 1000 && record != null) {

        } else if (counter == 1000 && record == null) {
            ps.executeBatch();
            counter = 0;
            conn.close();

        } else if (counter < 1000 && record == null) {
            if (counter != 0) {
                ps.executeBatch();
            }
            counter = 0;
            conn.close();
        }
    }
}
