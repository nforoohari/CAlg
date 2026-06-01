package api.daos;

import api.enums.Side;
import api.orders.OrderTransaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderTransactionDao {

    public static void insert(OrderTransaction entity) throws SQLException {

        String sql = """
                INSERT INTO order_transaction
                (
                    request_id,
                    order_id,
                    side,
                    volume,
                    price,
                    fee,
                    payedFee,
                    balance,
                    transaction_date
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DB.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setLong(1, entity.getRequestId());
            ps.setLong(2, entity.getOrderId());
            ps.setInt(3, entity.getSide().getCode());
            ps.setDouble(4, entity.getVolume());
            ps.setDouble(5, entity.getPrice());
            ps.setDouble(6, entity.getFee());
            ps.setDouble(7, entity.getPayedFee());
            ps.setDouble(8, entity.getBalance());
            ps.setTimestamp(9, new java.sql.Timestamp(entity.getDate().getTime()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
        }
    }

    public static OrderTransaction getById(long id) throws SQLException {

        String sql = """
                SELECT *
                FROM order_transaction
                WHERE id = ?
                """;

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

    public static List<OrderTransaction> getAll() throws SQLException {

        String sql = """
                SELECT *
                FROM order_transaction
                ORDER BY id
                """;

        List<OrderTransaction> result = new ArrayList<>();

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

    private static OrderTransaction map(ResultSet rs) throws SQLException {

        OrderTransaction e = new OrderTransaction();

        e.setId(rs.getLong("id"));
        e.setRequestId(rs.getLong("request_id"));
        e.setOrderId(rs.getLong("order_id"));
        e.setSide(Side.fromCode(rs.getInt("side")));
        e.setVolume(rs.getDouble("volume"));
        e.setPrice(rs.getDouble("price"));
        e.setFee(rs.getDouble("fee"));
        e.setPayedFee(rs.getDouble("payedFee"));
        e.setBalance(rs.getDouble("balance"));
        e.setDate(rs.getTimestamp("transaction_date"));

        return e;
    }
}