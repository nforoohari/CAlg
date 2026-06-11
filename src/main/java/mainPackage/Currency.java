package mainPackage;

public enum Currency {

    Bitcoin(0, "BTC", "BTCUSDT"),
    XAUT(1, "XAUT", "XAUTUSDT"),
    SLVON(2, "SLVON", "SLVONUSDT"),
    Tether(3, "USDT", ""),
    Ethereum(4, "ETH", "ETHUSDT"),
    Solana(5, "SOL", "SOLUSDT"),
    Cardano(6, "ADA", "ADAUSDT"),
    Tron(7, "TRX", "TRXUSDT"),
    Toncoin(8, "TON", "TONUSDT"),
    Chainlink(9, "LINK", "LINKUSDT"),
    BNB(10, "BNB", "BNBUSDT");

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

