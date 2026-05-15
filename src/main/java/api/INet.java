package api;

public interface INet {

    OrderStatus buy(Crypto crypto , Double volume, Double price) throws Exception;

    OrderStatus sell(Crypto crypto, Double volume, Double price) throws Exception;

    CryptoRecord getMarketInfo(Crypto crypto, String interval) throws Exception;

    OrderStatus checkOrderStatus(Crypto crypto, long orderId) throws Exception;

}
