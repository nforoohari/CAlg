package api.exchanges;

import api.daos.Record;
import api.enums.Currency;
import api.orders.OrderRequest;

public interface IExc {

    void submit(OrderRequest orderRequest) throws Exception;

    void checkStatus(OrderRequest orderRequest) throws Exception;

    void cancel(OrderRequest orderRequest) throws Exception;

    void submitByConfirmation(OrderRequest orderRequest) throws Exception;

    Record fetchExcData(Currency currency, String interval) throws Exception;


}
