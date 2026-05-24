package api;

public interface Ngi {

    Ofc buy(Oh oh, Double volume, Double price) throws Exception;

    Ofc sell(Oh oh, Double volume, Double price) throws Exception;

    Rxc getMarketInfo(Oh oh, String interval) throws Exception;

    Ofc checkOrderStatus(Oh oh, long orderId) throws Exception;

}
