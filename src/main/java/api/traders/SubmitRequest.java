package api.traders;

import api.orders.OrderRequest;

@FunctionalInterface
public interface SubmitRequest {
    void submit(OrderRequest orderRequest);
}
