package Algs;

import java.util.Date;

public class NormalRangeAlg extends Thread {

    private final CNames cNames;
    private final Double priceLimit;
    private Double volume;
    private final Double deltaPrice;
    private volatile boolean running;
    private Double soldAmount;
    private CExcelReader reader;


    public NormalRangeAlg(CNames cNames, Double priceLimit, Double volume, Double deltaPercent) {
        this.cNames = cNames;
        this.priceLimit = priceLimit;
        this.volume = volume;
        this.deltaPrice = deltaPercent * priceLimit / 100;
        this.running = true;
        this.soldAmount = 0.0;
        try {
            this.reader = new CExcelReader(this.cNames, "C:\\Users\\n_foroohari\\Desktop\\Mine\\Code\\CAlg\\src\\main\\resources\\btc_usdt_dummy_year.xlsx");
            System.out.println("The file was read.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void stopThread() {
        running = false;
    }

    boolean buy(CNames cNames, Double price, Double volume) {
        return buyProxy(cNames, price, volume);
    }

    boolean sell(CNames cNames, Double price, Double volume) {
        return sellProxy(cNames, price, volume);
    }

    boolean buyProxy(CNames cNames, Double price, Double volume) {
        System.out.println("Buy " + cNames + " , " + "price : " + price + " , volume : " + volume);
        return true;
    }

    boolean sellProxy(CNames cNames, Double price, Double volume) {
        System.out.println("Sell " + cNames + " , " + "price : " + price + " , volume : " + volume);
        return true;
    }

    @Override
    public void run() {
        System.out.println("Thread started at : " + new Date());
        while (running) {
//            System.out.println("Thread is running...");
            startAlg();
        }

        System.out.println("Thread stopped safely at : " + new Date());
    }

    void startAlg() {

        boolean buyCheck = false;
        boolean sellCheck = false;
        CRecord rec = null;

        while (!buyCheck) {
            rec = this.reader.getNext();
            if (rec != null && rec.getLow() < priceLimit) {
                volume = soldAmount == 0.0 ? volume : soldAmount / rec.getLow();
                buyCheck = buy(rec.getcNames(), rec.getLow(), volume);
//                if (!buyCheck) {
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                        System.out.println("Buy Sleep Exception");
//                    }
//                }
            } else if (rec == null) {
                this.running = false;
                sellCheck = true;
                break;
            }
        }

        while (!sellCheck) {
            rec = this.reader.getNext();
            if (rec != null && rec.getHigh() > (priceLimit + deltaPrice)) {
                sellCheck = sell(rec.getcNames(), rec.getHigh(), volume);
                soldAmount = volume * rec.getHigh();
//                if (!sellCheck) {
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                        System.out.println("Sell Sleep Exception");
//                    }
//                }
            } else if (rec == null) {
                this.running = false;
                break;
            }
        }
    }
}



