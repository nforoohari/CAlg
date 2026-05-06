package api;

public interface NetInterface {

    String buy(Crypto crypto , Double volume, Double price) throws Exception;

    String sell(Crypto crypto, Double volume, Double price) throws Exception;

    CryptoRecord getMarketInfo(Crypto crypto, String interval) throws Exception;

    OrderStatus checkOrderStatus(Crypto crypto, long orderId) throws Exception;

}
