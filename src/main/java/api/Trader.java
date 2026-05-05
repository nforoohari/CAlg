package api;

public class Trader {

    private NetInterface netInterface;
    private AlgInterface algInterface;

    public Trader(AlgInterface algInterface, NetInterface netInterface) {

        this.netInterface = netInterface;
        this.algInterface = algInterface;

    }

    public void start(){

    }
}
