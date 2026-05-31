package api.enums;

public enum Currency {

    Bitcoin(1, "BTC"),
    Gold(2, "GOLD"),
    Silver(3, "SILVER"),
    USD(4, "USD"),
    Ethereum(5, "ETH"),
    Solana(6, "SOL"),
    Cardano(7, "ADA"),
    Tron(8, "TRX"),
    Toncoin(9, "TON"),
    Chainlink(10, "LINK"),
    BNB(11, "BNB");

    private final int code;

    private final String name;

    Currency(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Currency fromCode(int code) {
        for (Currency c : Currency.values()) {
            if (c.getCode() == code) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}

