package Algs;

import java.util.Date;

public class RangeAlg extends Thread {

    private final CNames cNames;
    private Double priceLimit;
    private final Double baseVolume;
    private Double volume;
    private final Double deltaPercent;
    private Double deltaPrice;
    private final Double ascendingPercent;
    private Double ascendingPrice;
    private final Double fee;
    private volatile boolean running;
    private Double broughtInAmount;
    private Double soldAmount;
    private Double payedFeeAmount;
    private CExcelReader reader;


    public RangeAlg(CNames cNames, Double priceLimit, Double volume, Double deltaPercent, Double ascendingPercent, Double feePercent) {
        this.cNames = cNames;
        this.priceLimit = priceLimit;
        this.baseVolume = volume;
        this.volume = volume;
        this.deltaPercent = deltaPercent;
        this.deltaPrice = deltaPercent * priceLimit / 100;
        this.ascendingPercent = ascendingPercent;
        this.ascendingPrice = ascendingPercent * priceLimit / 100;
        this.fee = feePercent / 100;
        this.running = true;
        this.broughtInAmount = 0.0;
        this.soldAmount = 0.0;
        this.payedFeeAmount = 0.0;

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
        System.out.println("broughtInAmount : " +   (((double) Math.round(broughtInAmount * 10000)) / 100));
        System.out.println("soldAmount : " + (((double) Math.round(soldAmount * 10000)) / 100));
        System.out.println("volume : " + (((double) Math.round(volume * 10000)) / 100));
        System.out.println("payedFeeAmount : " + (((double) Math.round(payedFeeAmount * 10000)) / 100));
        System.out.println("((soldAmount / broughtInAmount) - 1) * 100  : " + ((double) Math.round(((soldAmount / broughtInAmount) - 1) * 10000)) / 100);
        System.out.println("((volume / baseVolume) - 1) * 100  : " + ((double) Math.round(((volume / baseVolume) - 1) * 10000)) / 100);


    }

    void startAlg() {

        boolean buyCheck = false;
        boolean sellCheck = false;
        CRecord rec = null;

        while (!buyCheck) {
            rec = reader.getNext();
            if (rec != null && rec.getLow() < priceLimit) {
                volume = soldAmount == 0.0 ? volume : soldAmount / (rec.getLow() * (1 + fee));
                broughtInAmount = soldAmount == 0.0 ? volume * (rec.getLow() * (1 + fee)) : broughtInAmount;
                buyCheck = buy(rec.getcNames(), rec.getLow(), volume);
                payedFeeAmount += volume * (rec.getLow() * fee);
                soldAmount = 0.0;

//                if (!buyCheck) {
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                        System.out.println("Buy Sleep Exception");
//                    }
//                }
            } else if (rec == null) {
                running = false;
                sellCheck = true;
                break;
            }
        }

        while (!sellCheck) {
            rec = reader.getNext();
            if (rec != null && rec.getHigh() > (priceLimit + deltaPrice)) {
                sellCheck = sell(rec.getcNames(), rec.getHigh(), volume);
                soldAmount = volume * rec.getHigh() * (1 - fee);
                payedFeeAmount += volume * (rec.getHigh() * fee);
                volume = 0.0;
//                if (!sellCheck) {
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                        System.out.println("Sell Sleep Exception");
//                    }
//                }
            } else if (rec == null) {
                running = false;
                break;
            }
        }
        priceLimit += ascendingPrice;
        deltaPrice = deltaPercent * priceLimit / 100;
        ascendingPrice = ascendingPercent * priceLimit / 100;

    }
}



