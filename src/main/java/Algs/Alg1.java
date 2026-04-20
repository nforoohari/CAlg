package Algs;

public class Alg1 {

    public static void main(String[] args) {
        System.out.println("Hello Algorithms");
        buy(CNames.EEE, 1.0F, 2.3F);
        sell(CNames.TTT, 3.0F, 4.5F);

    }

    static void buy(CNames cNames, Float priceLimit, Float volume) {
//        System.out.println("Buy  , " + "priceLimit : " + priceLimit + " cNames : " + cNames + " , code : " + cNames.getCode() + " , volume : " + volume);
        buyProxy(cNames, priceLimit, volume);
    }

    static void sell(CNames cNames, Float priceLimit, Float volume) {
//        System.out.println("Sell , " + "priceLimit : " + priceLimit + " cNames : " + cNames + " , code : " + cNames.getCode() + " , volume : " + volume);
        sellProxy(cNames, priceLimit, volume);
    }

    static void buyProxy(CNames cNames, Float priceLimit, Float volume) {
        System.out.println("BuyProxy  , " + "priceLimit : " + priceLimit + " cNames : " + cNames + " , code : " + cNames.getCode() + " , volume : " + volume);
    }

    static void sellProxy(CNames cNames, Float priceLimit, Float volume) {
        System.out.println("SellProxy , " + "priceLimit : " + priceLimit + " cNames : " + cNames + " , code : " + cNames.getCode() + " , volume : " + volume);
    }
}
