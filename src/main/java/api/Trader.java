package api;

public class Trader {

    private INet INet;
    private ICryptoAlg ICryptoAlg;

    public Trader(ICryptoAlg ICryptoAlg, INet INet) {

        this.INet = INet;
        this.ICryptoAlg = ICryptoAlg;

    }

    public void start(){

    }
}
