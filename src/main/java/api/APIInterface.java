package api;

public interface APIInterface {

    Double buy(Crypto crypto , Double price, Double volume);

    Double sell(Crypto crypto, Double price, Double volume);

    CryptoRecord getMinuteInfo(Crypto crypto);

    CryptoRecord getSecondInfo(Crypto crypto);

}
