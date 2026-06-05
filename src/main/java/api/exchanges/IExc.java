package api.exchanges;

import api.daos.Record;
import api.enums.Currency;
import api.enums.RetryTimes;
import api.orders.OrderRequest;
import api.orders.OrderStatus;

public interface  IExc {

    void submit(OrderRequest orderRequest) throws Exception;
    OrderRequest checkStatus(long orderRequestId) throws Exception;

    OrderStatus cancel(long orderRequestId) throws Exception;

    void submitByConfirmation(OrderRequest orderRequest, RetryTimes retryTimes) throws Exception;

    Record fetchExcData() throws Exception;

}
