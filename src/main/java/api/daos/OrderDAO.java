package api.daos;

import api.enums.Currency;
import api.orders.OrderState;
import api.orders.OrderTransaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDAO {

    // INSERT ORDER STATUS
    public static long insertOrderStatus(OrderState order) throws Exception {

        String sql = """
                INSERT INTO order_status
                (currency, side, volume, price, ordered_date, completed)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection con = DB.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setLong(1, order.getCrypto().getCode());
            ps.setString(2, order.getSide());
            ps.setDouble(3, order.getVolume());
            ps.setDouble(4, order.getPrice());

            ps.setTimestamp(
                    5,
                    new java.sql.Timestamp(order.getOrderedDate().getTime())
            );


            ps.setBoolean(6, order.isCompleted());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getLong(1);
            }

            return 0;
        }
    }

    // INSERT ORDER DETAIL
    public static void insertOrderDetail(OrderTransaction detail) throws Exception {

        String sql = """
                INSERT INTO order_details
                (order_status_id, volume, price, detail_date)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection con = DB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            ps.setLong(1, detail.getOrderStatusId());
            ps.setDouble(2, detail.getVolume());
            ps.setDouble(3, detail.getPrice());

            ps.setTimestamp(
                    4,
                    new java.sql.Timestamp(detail.getDetailDate().getTime())
            );

            ps.executeUpdate();
        }
    }

    // GET ORDER BY ID
    public static OrderState getOrderById(long id) throws Exception {

        String sql = "SELECT * FROM order_status WHERE id=?";

        try (
                Connection con = DB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                OrderState order = new OrderState();

                order.setId(rs.getLong("id"));
                order.setCrypto(Currency.fromCode(rs.getLong("currency")));
                order.setSide(rs.getString("side"));
                order.setVolume(rs.getDouble("volume"));
                order.setPrice(rs.getDouble("price"));
                order.setOrderedDate(rs.getTimestamp("ordered_date"));
                order.setCompleted(rs.getBoolean("completed"));
                order.setCompletedDate(rs.getTimestamp("completed_date"));
                return order;
            }
            return null;
        }
    }

    // GET ALL ORDERS
    public static List<OrderState> getAllOrders() throws Exception {

        List<OrderState> list = new ArrayList<>();

        String sql = """
                SELECT * FROM order_status
                ORDER BY ordered_date DESC
                """;

        try (
                Connection con = DB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                OrderState order = new OrderState();

                order.setId(rs.getLong("id"));
                order.setCrypto(Currency.fromCode(rs.getLong("currency")));
                order.setSide(rs.getString("side"));
                order.setVolume(rs.getDouble("volume"));
                order.setPrice(rs.getDouble("price"));
                order.setOrderedDate(rs.getTimestamp("ordered_date"));
                order.setCompleted(rs.getBoolean("completed"));
                order.setCompletedDate(rs.getTimestamp("completed_date"));

                list.add(order);
            }
        }

        return list;
    }

    // GET COMPLETED ORDERS
    public static List<OrderState> getOrdersByCompletion(
            Currency c, boolean completed
    ) throws Exception {

        List<OrderState> list = new ArrayList<>();

        String sql = """
                SELECT * FROM order_status
                WHERE completed=? AND currency=?
                ORDER BY ordered_date DESC
                """;

        try (
                Connection con = DB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setBoolean(1, completed);
            ps.setLong(2,c.getCode());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                OrderState order = new OrderState();

                order.setId(rs.getLong("id"));
                order.setCrypto(Currency.fromCode(rs.getLong("currency")));
                order.setSide(rs.getString("side"));
                order.setVolume(rs.getDouble("volume"));
                order.setPrice(rs.getDouble("price"));
                order.setOrderedDate(rs.getTimestamp("ordered_date"));
                order.setCompleted(rs.getBoolean("completed"));
                order.setCompletedDate(rs.getTimestamp("completed_date"));

                list.add(order);
            }
        }

        return list;
    }

    // GET DETAILS OF ORDER
    public static List<OrderTransaction> getOrderDetails(
            long orderId
    ) throws Exception {

        List<OrderTransaction> list = new ArrayList<>();

        String sql = """
                SELECT * FROM order_detail
                WHERE order_status_id=?
                ORDER BY detail_date
                """;

        try (
                Connection con = DB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setLong(1, orderId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                OrderTransaction detail = new OrderTransaction();

                detail.setId(rs.getLong("id"));
                detail.setOrderStatusId(rs.getLong("order_status_id"));
                detail.setVolume(rs.getDouble("volume"));
                detail.setPrice(rs.getDouble("price"));
                detail.setDetailDate(rs.getTimestamp("detail_date"));

                list.add(detail);
            }
        }

        return list;
    }


    public static boolean completeOrder(
            long id,
            Date d
    ) throws Exception {

        String sql = """
            UPDATE order_status
            SET completed=?,
                completed_date=?
            WHERE id=?
            """;

        try (
                Connection con = DB.getConnection();
                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setBoolean(1, true);

            ps.setTimestamp(2, new java.sql.Timestamp(d.getTime()));

            ps.setLong(3, id);

            int rows = ps.executeUpdate();

            return rows > 0;
        }
    }


    public static boolean deleteOrderStatusById(long id) throws Exception {

        String sql = "DELETE FROM order_status WHERE id=?";

        try (
                Connection con = DB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setLong(1, id);

            int rows = ps.executeUpdate();

            return rows > 0;
        }
    }

    public static boolean deleteOrderDetailsByStatusId(long statusId) throws Exception {

        String sql = "DELETE FROM order_details WHERE order_status_id=?";

        try (
                Connection con = DB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setLong(1, statusId);

            int rows = ps.executeUpdate();

            return rows > 0;
        }
    }

}