package api;

public class MyNet implements NetInterface {
    @Override
    public Double buy(Crypto crypto, Double price, Double volume) {
        return null;
    }

    @Override
    public Double sell(Crypto crypto, Double price, Double volume) {
        return null;
    }

    @Override
    public CryptoRecord getMarketInfo(Crypto crypto, String interval) {
        return null;
    }

    @Override
    public OrderStatus checkOrderStatus(Crypto crypto, long orderId) {
        return null;
    }

}
