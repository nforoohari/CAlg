package api;

public interface NetInterface {

    Double buy(Crypto crypto , Double price, Double volume);

    Double sell(Crypto crypto, Double price, Double volume);

    CryptoRecord getMarketInfo(Crypto crypto, String interval);

    OrderStatus checkOrderStatus(Crypto crypto, long orderId);

}
