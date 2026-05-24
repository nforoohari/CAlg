package api;

public interface INet {

    CryptoOrder buy(Crypto crypto, Double volume, Double price) throws Exception;

    CryptoOrder sell(Crypto crypto, Double volume, Double price) throws Exception;

    CryptoRecord getMarketInfo(Crypto crypto, String interval) throws Exception;

    CryptoOrder checkOrderStatus(Crypto crypto, long orderId) throws Exception;

}
