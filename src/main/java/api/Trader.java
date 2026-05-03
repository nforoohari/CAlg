package api;

public class Trader {

    private APIInterface apiInterface;
    private AlgInterface algInterface;

    public Trader(AlgInterface algInterface, APIInterface apiInterface) {

        this.apiInterface = apiInterface;
        this.algInterface = algInterface;

    }

    public void start(){

    }
}
