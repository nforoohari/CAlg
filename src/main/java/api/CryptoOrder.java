package api;

import java.util.ArrayList;
import java.util.List;

public class CryptoOrder {
    private OrderStatus orderStatus;
    private List<OrderDetails> details;

    public CryptoOrder() {
        this.details = new ArrayList<>();
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderDetails> getDetails() {
        return this.details;
    }

    public void setDetails(List<OrderDetails> details) {
        this.details = details;
    }
}
