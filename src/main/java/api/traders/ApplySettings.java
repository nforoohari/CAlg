package api.traders;

import api.daos.Record;
import api.orders.OrderRequest;

@FunctionalInterface
public interface ApplySettings {
    OrderRequest apply(Record record);
}
