package mainPackage;

public enum Currency {

    Bitcoin(1, "BTC", "BTCUSDT"),
    Gold(2, "GOLD", "GOLDUSDT"),
    Silver(3, "SILVER", "SILVERUSDT"),
    USD(4, "USD", "USDUSDT"),
    Ethereum(5, "ETH", "ETHUSDT"),
    Solana(6, "SOL", "SOLUSDT"),
    Cardano(7, "ADA", "ADAUSDT"),
    Tron(8, "TRX", "TRXUSDT"),
    Toncoin(9, "TON", "TONUSDT"),
    Chainlink(10, "LINK", "LINKUSDT"),
    BNB(11, "BNB", "BNBUSDT"),;

    private final int code;

    private final String name;

    private final String symbol;

    Currency(int code, String name, String symbol) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() { return symbol; }

    public static Currency fromCode(int code) {
        for (Currency c : Currency.values()) {
            if (c.getCode() == code) {
                return c;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}

