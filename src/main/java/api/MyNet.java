package api;

public class MyNet implements INet {
    @Override
    public OrderStatus buy(Crypto crypto , Double volume, Double price) {
        return null;
    }

    @Override
    public OrderStatus sell(Crypto crypto, Double volume, Double price) {
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
