package api.daos;

import api.enums.Status;
import api.orders.OrderState;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderStateDao {

    public static void insert(OrderState entity) throws SQLException {

        String sql = """
                INSERT INTO order_state
                (
                    request_id,
                    order_id,
                    order_date,
                    volume,
                    balance,
                    payedFee,
                    status,
                    status_date
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DB.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setLong(1, entity.getRequestId());
            ps.setLong(2, entity.getOrderId());
            ps.setTimestamp(3, new java.sql.Timestamp(entity.getOrderDate().getTime()));
            ps.setDouble(4, entity.getVolume());
            ps.setDouble(5, entity.getBalance());
            ps.setDouble(6, entity.getPayedFee());
            ps.setInt(7, entity.getStatus().getCode());
            ps.setTimestamp(8, new java.sql.Timestamp(entity.getStatusDate().getTime()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entity.setId(rs.getLong(1));
                }
            }
        }
    }

    public static OrderState getById(long id) throws SQLException {

        String sql = "SELECT * FROM order_state WHERE id=?";

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

    public static List<OrderState> getAll() throws SQLException {

        String sql = "SELECT * FROM order_state ORDER BY id";

        List<OrderState> result = new ArrayList<>();

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

    private static OrderState map(ResultSet rs) throws SQLException {

        OrderState e = new OrderState();

        e.setId(rs.getLong("id"));
        e.setRequestId(rs.getLong("request_id"));
        e.setOrderId(rs.getLong("order_id"));
        e.setOrderDate(rs.getTimestamp("order_date"));
        e.setVolume(rs.getDouble("volume"));
        e.setBalance(rs.getDouble("balance"));
        e.setPayedFee(rs.getDouble("payedFee"));
        e.setStatus(Status.fromCode(rs.getInt("status")));
        e.setStatusDate(rs.getTimestamp("status_date"));

        return e;
    }
}