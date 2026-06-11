package api.daos;

import api.enums.Currency;
import api.enums.Exchange;
import api.enums.Side;
import api.orders.OrderRequest;
import api.traders.TraderState;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TraderStateDao {

    public static void insert(TraderState state) throws SQLException {

        String sql = """
                INSERT INTO trader_state
                (trader_id, state_date, volume, balance, payed_fee, stop_loss_enable)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DB.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setLong(1, state.getTraderId());
            ps.setTimestamp(2, new java.sql.Timestamp(state.getDate().getTime()));
            ps.setDouble(3, state.volume);
            ps.setDouble(4, state.balance);
            ps.setDouble(5, state.payedFee);
            ps.setBoolean(6,state.stopLossEnable);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                state.setId(rs.getLong(1));
            }
        }
    }

    public static boolean delete(long traderId) throws SQLException {

        String sql = "DELETE FROM trader_state WHERE trader_id=?";

        try (
                Connection connection = DB.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setLong(1, traderId);

            return ps.executeUpdate() > 0;
        }
    }

    public static TraderState findFirstByTraderId(long traderId) throws SQLException {

        String sql = """
                SELECT *
                FROM trader_state
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

                    TraderState state = new TraderState();

                    state.setId(rs.getLong("id"));
                    state.setTraderId(rs.getLong("trader_id"));
                    state.setDate(rs.getTimestamp("state_date"));
                    state.volume = rs.getDouble("volume");
                    state.balance = rs.getDouble("balance");
                    state.payedFee = rs.getDouble("payed_fee");
                    state.stopLossEnable = rs.getBoolean("stop_loss_enable");

                    return state;
                }
            }
        }

        return null;
    }

    public static TraderState findLastByTraderId(long traderId) throws SQLException {

        String sql = """
                SELECT *
                FROM trader_state
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

                    TraderState state = new TraderState();

                    state.setId(rs.getLong("id"));
                    state.setTraderId(rs.getLong("trader_id"));
                    state.setDate(rs.getTimestamp("state_date"));
                    state.volume = rs.getDouble("volume");
                    state.balance = rs.getDouble("balance");
                    state.payedFee = rs.getDouble("payed_fee");
                    state.stopLossEnable = rs.getBoolean("stop_loss_enable");

                    return state;
                }
            }
        }

        return null;
    }


    public static List<TraderState> findAllByTraderId(long traderId) throws SQLException {

        List<TraderState> list = new ArrayList<>();

        String sql = """
                SELECT *
                FROM trader_state
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

                    TraderState state = new TraderState();

                    state.setId(rs.getLong("id"));
                    state.setTraderId(rs.getLong("trader_id"));
                    state.setDate(rs.getTimestamp("state_date"));
                    state.volume = rs.getDouble("volume");
                    state.balance = rs.getDouble("balance");
                    state.payedFee = rs.getDouble("payed_fee");
                    state.stopLossEnable = rs.getBoolean("stop_loss_enable");

                    list.add(state);
                }
            }
        }

        return list;
    }
}