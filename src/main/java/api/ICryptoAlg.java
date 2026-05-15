package api;

public interface ICryptoAlg {

    void start();
    void stop();
    void pause();
    void resume();
    void setParameters();
    void initialize();
    void setNet(INet INet);
    INet getNet();
    void setAlgPrameters(CryptoAlgParams cryptoAlgParams);
    CryptoAlgParams getAlgPrameters();
}
