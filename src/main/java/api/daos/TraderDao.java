package api.daos;

import api.enums.Exchange;
import api.enums.Interval;
import api.traders.Trader;
import api.enums.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TraderDao {

    public static void insert(Trader trader) throws SQLException {

        String sql = """
                INSERT INTO trader
                (exchange, currency, fee, interval_code, trader_date, start_time, end_time)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DB.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, trader.getExchange().getCode());
            ps.setInt(2, trader.getCurrency().getCode());
            ps.setDouble(3, trader.getFee());
            ps.setInt(4, trader.getInterval().getCode());
            ps.setTimestamp(5, new java.sql.Timestamp(trader.getDate().getTime()));
            ps.setString(6, trader.getStartTime());
            ps.setString(7, trader.getEndTime());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                trader.setId(rs.getLong(1));
            }
        }
    }

    public static boolean update(Trader trader) throws SQLException {

        String sql = """
                UPDATE trader
                SET exchange=?,
                    currency=?,
                    fee=?,
                    interval_code=?,
                    trader_date=?,
                    start_time=?,
                    end_time=?
                WHERE id=?
                """;

        try (
                Connection connection = DB.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, trader.getExchange().getCode());
            ps.setInt(2, trader.getCurrency().getCode());
            ps.setDouble(3, trader.getFee());
            ps.setInt(4, trader.getInterval().getCode());
            ps.setTimestamp(5, new java.sql.Timestamp(trader.getDate().getTime()));
            ps.setString(6, trader.getStartTime());
            ps.setString(7, trader.getEndTime());

            ps.setLong(8, trader.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public static boolean delete(long id) throws SQLException {

        String sql = "DELETE FROM trader WHERE id=?";

        try (
                Connection connection = DB.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    public static Trader findById(long id) throws SQLException {

        String sql = "SELECT * FROM trader WHERE id=?";

        try (
                Connection connection = DB.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return map(rs);
                }
            }
        }

        return null;
    }

    public static List<Trader> findAll() throws SQLException {

        List<Trader> result = new ArrayList<>();

        String sql = "SELECT * FROM trader";

        try (
                Connection connection = DB.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                result.add(map(rs));
            }
        }

        return result;
    }

    private static Trader map(ResultSet rs) throws SQLException {

        Trader trader = new Trader();

        trader.setId(rs.getLong("id"));
        trader.setExchange(Exchange.fromCode(rs.getInt("exchange")));
        trader.setCurrency(Currency.fromCode(rs.getInt("currency")));
        trader.setFee(rs.getDouble("fee"));
        trader.setInterval(Interval.fromCode(rs.getInt("interval_code")));
        trader.setDate(rs.getTimestamp("trader_date"));
        trader.setStartTime(rs.getString("start_time"));
        trader.setEndTime(rs.getString("end_time"));

        return trader;
    }

}