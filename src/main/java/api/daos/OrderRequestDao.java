package api.daos;

import api.enums.Currency;
import api.enums.Exchange;
import api.enums.Side;
import api.orders.OrderRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRequestDao {

    public static void insert(OrderRequest entity) throws SQLException {

        String sql = """
                INSERT INTO order_request
                (
                    exchange,
                    currency,
                    side,
                    capital,
                    price,
                    fee,
                    request_date
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DB.getConnection();
                PreparedStatement ps =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setInt(1, entity.getExchange().getCode());
            ps.setInt(2, entity.getCurrency().getCode());
            ps.setInt(3, entity.getSide().getCode());
            ps.setDouble(4, entity.getCapital());
            ps.setDouble(5, entity.getPrice());
            ps.setDouble(6, entity.getFee());
            ps.setTimestamp(7, new java.sql.Timestamp(entity.getDate().getTime()));

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                entity.setId(rs.getLong(1));
            }
        }
    }

    public static OrderRequest getById(long id) throws SQLException {

        String sql = """
                SELECT *
                FROM order_request
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

    public static List<OrderRequest> getAll() throws SQLException {

        String sql = """
                SELECT *
                FROM order_request
                ORDER BY id
                """;

        List<OrderRequest> result = new ArrayList<>();

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

    private static OrderRequest map(ResultSet rs) throws SQLException {

        OrderRequest e = new OrderRequest();

        e.setId(rs.getLong("id"));
        e.setExchange(Exchange.fromCode(rs.getInt("exchange")));
        e.setCurrency(Currency.fromCode(rs.getInt("currency")));
        e.setSide(Side.fromCode(rs.getInt("side")));
        e.setCapital(rs.getDouble("capital"));
        e.setPrice(rs.getDouble("price"));
        e.setFee(rs.getDouble("fee"));
        e.setDate(rs.getTimestamp("request_date"));

        return e;
    }
}