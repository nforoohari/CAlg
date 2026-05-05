package api;

public interface AlgInterface {

    void start();
    void stop();
    void pause();
    void resume();
    void setParameters();
    void initialize();
    void setNet(NetInterface netInterface);
    NetInterface getNet();
    void setAlgPrameters(AlgParameters algParameters);
    AlgParameters getAlgPrameters();
}
