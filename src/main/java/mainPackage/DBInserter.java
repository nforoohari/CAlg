package mainPackage;

import java.sql.Connection;
import java.sql.Date;
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
        this.sql = "INSERT INTO " + this.tableName + "(crypto,interval_date,open,high,low,close,volume) VALUES (?,?,?,?,?,?,?)";
        this.ps = conn.prepareStatement(sql);
        this.counter = 0;
    }

    public void batchAddAndInsert(CryptoRecord cryptoRecord) throws SQLException {

        if (counter < 1000 && cryptoRecord != null) {

            ps.setLong(1, cryptoRecord.getCrypto().getCode());
            ps.setDate(2, (Date) cryptoRecord.getDate());
            ps.setDouble(3, cryptoRecord.getOpen());
            ps.setDouble(4, cryptoRecord.getHigh());
            ps.setDouble(5, cryptoRecord.getLow());
            ps.setDouble(6, cryptoRecord.getClose());
            ps.setDouble(7, cryptoRecord.getVolume());

            ps.addBatch();
            ++counter;

        }

        if (counter == 1000 && cryptoRecord != null) {
            ps.executeBatch();
            counter = 0;
        } else if (counter < 1000 && cryptoRecord != null) {

        } else if (counter == 1000 && cryptoRecord == null) {
            ps.executeBatch();
            counter = 0;
            conn.close();

        } else if (counter < 1000 && cryptoRecord == null) {
            if (counter != 0) {
                ps.executeBatch();
            }
            counter = 0;
            conn.close();
        }
    }
}
