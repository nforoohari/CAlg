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
                    threshold_price,
                    stop_loss_percent,
                    stop_loss,
                    delta_percent,
                    delta,
                    ascending_percent,
                    ascending,
                    top_fix,
                    bottom_fix
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?,?)
                """;

        try (
                Connection connection = DB.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setLong(1, settings.getTraderId());
            ps.setTimestamp(2, new java.sql.Timestamp(settings.getDate().getTime()));
            ps.setDouble(3, settings.thresholdPrice);
            ps.setDouble(4, settings.stopLossPercent);
            ps.setDouble(5, settings.stopLoss);
            ps.setDouble(6, settings.deltaPercent);
            ps.setDouble(7, settings.delta);
            ps.setDouble(8, settings.ascendingPercent);
            ps.setDouble(9, settings.ascending);
            ps.setBoolean(10, settings.topFix);
            ps.setBoolean(11, settings.bottomFix);

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
                ORDER BY settings_date ASC
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
                    settings.thresholdPrice = rs.getDouble("threshold_price");
                    settings.stopLossPercent = rs.getDouble("stop_loss_percent");
                    settings.stopLoss = rs.getDouble("stop_loss");
                    settings.deltaPercent = rs.getDouble("delta_percent");
                    settings.delta = rs.getDouble("delta");
                    settings.ascendingPercent = rs.getDouble("ascending_percent");
                    settings.ascending = rs.getDouble("ascending");
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
                ORDER BY settings_date DESC
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
                    settings.thresholdPrice = rs.getDouble("threshold_price");
                    settings.stopLossPercent = rs.getDouble("stop_loss_percent");
                    settings.stopLoss = rs.getDouble("stop_loss");
                    settings.deltaPercent = rs.getDouble("delta_percent");
                    settings.delta = rs.getDouble("delta");
                    settings.ascendingPercent = rs.getDouble("ascending_percent");
                    settings.ascending = rs.getDouble("ascending");
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
                ORDER BY settings_date
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
                    settings.thresholdPrice = rs.getDouble("threshold_price");
                    settings.stopLossPercent = rs.getDouble("stop_loss_percent");
                    settings.stopLoss = rs.getDouble("stop_loss");
                    settings.deltaPercent = rs.getDouble("delta_percent");
                    settings.delta = rs.getDouble("delta");
                    settings.ascendingPercent = rs.getDouble("ascending_percent");
                    settings.ascending = rs.getDouble("ascending");
                    settings.topFix = rs.getBoolean("top_fix");
                    settings.bottomFix = rs.getBoolean("bottom_fix");

                    list.add(settings);
                }
            }
        }

        return list;
    }
}