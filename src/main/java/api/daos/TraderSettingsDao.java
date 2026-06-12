package api.daos;

import api.traders.TraderSettings;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TraderSettingsDao {

    public static void insert(TraderSettings settings) throws SQLException {

        String sql = """
                INSERT INTO trader_settings
                (
                    trader_id,
                    settings_date,
                    bid_price,
                    ask_price,
                    lower_stop_loss_price,
                    upper_stop_loss_price,
                    change_percent,
                    top_fix,
                    bottom_fix
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)
                """;

        try (
                Connection connection = DB.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setLong(1, settings.getTraderId());
            ps.setTimestamp(2, new java.sql.Timestamp(settings.getDate().getTime()));
            ps.setDouble(3, settings.bidPrice);
            ps.setDouble(4, settings.askPrice);
            ps.setDouble(5, settings.lowerStopLossPrice);
            ps.setDouble(6, settings.upperStopLossPrice);
            ps.setDouble(7, settings.changePercent);
            ps.setBoolean(8, settings.topFix);
            ps.setBoolean(9, settings.bottomFix);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                settings.setId(rs.getLong(1));
            }
        }
    }

    public static boolean delete(long traderId) throws SQLException {

        String sql = "DELETE FROM trader_settings WHERE trader_id=?";

        try (
                Connection connection = DB.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setLong(1, traderId);

            return ps.executeUpdate() > 0;
        }
    }

    public static TraderSettings findFirstByTraderId(long traderId) throws SQLException {

        String sql = """
                SELECT *
                FROM trader_settings
                WHERE trader_id=?
                ORDER BY id ASC
                LIMIT 1
                """;

        try (
                Connection connection = DB.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setLong(1, traderId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    TraderSettings settings = new TraderSettings();

                    settings.setId(rs.getLong(1));
                    settings.setTraderId(rs.getLong(2));
                    settings.setDate(rs.getTimestamp(3));
                    settings.bidPrice = rs.getDouble("bid_price");
                    settings.askPrice = rs.getDouble("ask_price");
                    settings.lowerStopLossPrice = rs.getDouble("lower_stop_loss_price");
                    settings.upperStopLossPrice = rs.getDouble("upper_stop_loss_price");
                    settings.changePercent = rs.getDouble("change_percent");
                    settings.topFix = rs.getBoolean("top_fix");
                    settings.bottomFix = rs.getBoolean("bottom_fix");

                    return settings;
                }
            }
        }

        return null;
    }

    public static TraderSettings findLastByTraderId(long traderId) throws SQLException {

        String sql = """
                SELECT *
                FROM trader_settings
                WHERE trader_id=?
                ORDER BY id DESC
                LIMIT 1
                """;

        try (
                Connection connection = DB.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setLong(1, traderId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    TraderSettings settings = new TraderSettings();

                    settings.setId(rs.getLong(1));
                    settings.setTraderId(rs.getLong(2));
                    settings.setDate(rs.getTimestamp(3));
                    settings.bidPrice = rs.getDouble("bid_price");
                    settings.askPrice = rs.getDouble("ask_price");
                    settings.lowerStopLossPrice = rs.getDouble("lower_stop_loss_price");
                    settings.upperStopLossPrice = rs.getDouble("upper_stop_loss_price");
                    settings.changePercent = rs.getDouble("change_percent");
                    settings.topFix = rs.getBoolean("top_fix");
                    settings.bottomFix = rs.getBoolean("bottom_fix");

                    return settings;
                }
            }
        }

        return null;
    }

    public static List<TraderSettings> findAllByTraderId(long traderId) throws SQLException {

        List<TraderSettings> list = new ArrayList<>();

        String sql = """
                SELECT *
                FROM trader_settings
                WHERE trader_id=?
                ORDER BY id
                """;

        try (
                Connection connection = DB.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setLong(1, traderId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    TraderSettings settings = new TraderSettings();

                    settings.setId(rs.getLong(1));
                    settings.setTraderId(rs.getLong(2));
                    settings.setDate(rs.getTimestamp(3));
                    settings.bidPrice = rs.getDouble("bid_price");
                    settings.askPrice = rs.getDouble("ask_price");
                    settings.lowerStopLossPrice = rs.getDouble("lower_stop_loss_price");
                    settings.upperStopLossPrice = rs.getDouble("upper_stop_loss_price");
                    settings.changePercent = rs.getDouble("change_percent");
                    settings.topFix = rs.getBoolean("top_fix");
                    settings.bottomFix = rs.getBoolean("bottom_fix");

                    list.add(settings);
                }
            }
        }

        return list;
    }
}