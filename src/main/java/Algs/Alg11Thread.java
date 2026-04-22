package Algs;

import java.io.IOException;
import java.util.Date;

public class Alg11Thread extends Thread {

    private final CNames cNames;
    private final Double priceLimit;
    private final Double volume;
    private final Double deltaPrice;
    private volatile boolean running;
    private CExcelReader reader;


    public Alg11Thread(CNames cNames, Double priceLimit, Double volume, Double deltaPrice) throws IOException {
        this.cNames = cNames;
        this.priceLimit = priceLimit;
        this.volume = volume;
        this.deltaPrice = deltaPrice;
        this.running = true;
        try {
             this.reader = new CExcelReader("C:\\Users\\n_foroohari\\Desktop\\Mine\\Code\\CAlg\\src\\main\\resources\\btc_usdt_dummy_year.xlsx");
             System.out.println("The file was read.");
        } catch (Exception e) {
            e.printStackTrace();
            this.reader.close();

        }
    }


    public void stopThread() {
        running = false;
    }

    boolean buy(CNames cNames, Double priceLimit, Double volume) {
        return buyProxy(cNames, priceLimit, volume);
    }

    boolean sell(CNames cNames, Double priceLimit, Double volume) {
        return sellProxy(cNames, priceLimit, volume);
    }

    MInfo getLastMI(CNames cNames) {
        return getLastMIProxy(cNames);
    }

    boolean buyProxy(CNames cNames, Double priceLimit, Double volume) {
        System.out.println("BuyProxy  , " + "priceLimit : " + priceLimit + " cNames : " + cNames + " , code : " + cNames.getCode() + " , volume : " + volume);
        return true;
    }

    boolean sellProxy(CNames cNames, Double priceLimit, Double volume) {
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

    void startAlg1(CNames cNames, Double priceLimit, Double volume, Double deltaPrice) {

        boolean buyCheck = false;
        boolean sellCheck = false;
        CRecord rec = null;

        while (!buyCheck) {
//            MInfo mi = getLastMI(cNames);
            rec = this.reader.getNext();
            if (rec != null && rec.getLow() < priceLimit) {
                buyCheck = buy(cNames, rec.getLow(), volume);
                if (!buyCheck) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        System.out.println("Buy Sleep Exception");
                    }
                }
            }
        }

        while (!sellCheck) {
//            MInfo mi = getLastMI(cNames);
            rec = this.reader.getNext();
            if (rec != null && rec.getHigh() > (priceLimit + deltaPrice)) {
                sellCheck = sell(cNames, rec.getHigh(), volume);
                if (!sellCheck) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        System.out.println("Sell Sleep Exception");
                    }
                }
            }
        }
    }
}



