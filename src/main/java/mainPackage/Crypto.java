package mainPackage;

public enum Crypto {
    Bitcoin(1,"BTC"),
    Ethereum(2,"ETH"),
    Toncoin(3,"TON"),
    Solana(4,"SOL"),
    Chainlink(5,"Link"),
    Cardano(6,"ADA"),
    PAXG(7,"PAXG"),
    SAM(1000,"SAM");


    private final int code;

    private final String name;

    Crypto(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

