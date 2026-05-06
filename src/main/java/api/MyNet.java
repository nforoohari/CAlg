package api;

public class MyNet implements NetInterface {
    @Override
    public String buy(Crypto crypto , Double volume, Double price) {
        return null;
    }

    @Override
    public String sell(Crypto crypto, Double volume, Double price) {
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
