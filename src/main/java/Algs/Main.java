package Algs;

public class Main {
    public static void main(String[] args) throws Exception {

        System.out.println("Hello Algorithms");

        Alg1Thread t = new Alg1Thread(CNames.BBB,3.1F,9F,0.8F);
        t.buy(CNames.EEE, 1.0F, 2.3F);
        t.sell(CNames.TTT, 3.0F, 4.5F);

        t.start();
        Thread.sleep(18000);
        t.stopThread();
    }
}