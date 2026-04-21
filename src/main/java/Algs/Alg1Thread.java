package Algs;

import java.util.Date;

public class Alg1Thread extends Thread {

    private CNames cNames;
    private Float priceLimit;
    private Float volume;
    private Float deltaPrice;
    private volatile boolean running;

    public Alg1Thread(CNames cNames, Float priceLimit, Float volume, Float deltaPrice) {
        this.cNames = cNames;
        this.priceLimit = priceLimit;
        this.volume = volume;
        this.deltaPrice = deltaPrice;
        this.running = true;
    }


    public void stopThread() {
        running = false;
    }

    boolean buy(CNames cNames, Float priceLimit, Float volume) {
        return buyProxy(cNames, priceLimit, volume);
    }

    boolean sell(CNames cNames, Float priceLimit, Float volume) {
        return sellProxy(cNames, priceLimit, volume);
    }

    MInfo getLastMI(CNames cNames) {
        return getLastMIProxy(cNames);
    }

    boolean buyProxy(CNames cNames, Float priceLimit, Float volume) {
        System.out.println("BuyProxy  , " + "priceLimit : " + priceLimit + " cNames : " + cNames + " , code : " + cNames.getCode() + " , volume : " + volume);
        return true;
    }

    boolean sellProxy(CNames cNames, Float priceLimit, Float volume) {
        System.out.println("SellProxy , " + "priceLimit : " + priceLimit + " cNames : " + cNames + " , code : " + cNames.getCode() + " , volume : " + volume);
        return true;
    }

    MInfo getLastMIProxy(CNames cNames) {
        // WebService Call
        return new MInfo(CNames.BBB, 1F, 2F, 0.5F, 4.5F, 10F);
    }

    @Override
    public void run() {
        System.out.println("Thread started at : " + new Date());
        while (running) {

            System.out.println("Thread is running...");

            startAlg1(this.cNames, this.priceLimit, this.volume, this.deltaPrice);

        }

        System.out.println("Thread stopped safely at : " + new Date());
    }

    void startAlg1(CNames cNames, Float priceLimit, Float volume, Float deltaPrice) {

        boolean buyCheck = false;
        boolean sellCheck = false;

        while (!buyCheck) {
            MInfo mi = getLastMI(cNames);
            if (mi.lp() < priceLimit) {
                buyCheck = buy(cNames, mi.lp(), volume);
                if (!buyCheck) {
                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        System.out.println("Buy Sleep Exception");
                    }
                }
            }
        }

        while (!sellCheck) {
            MInfo mi = getLastMI(cNames);
            if (mi.hp() > (priceLimit + deltaPrice)) {
                sellCheck = sell(cNames, mi.hp(), volume);
                if (!sellCheck) {
                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        System.out.println("Sell Sleep Exception");
                    }
                }
            }
        }
    }
}



