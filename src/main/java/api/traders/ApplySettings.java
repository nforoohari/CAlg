package api.traders;

import api.daos.Record;

@FunctionalInterface
public interface ApplySettings {
    boolean apply(Record record);
}
