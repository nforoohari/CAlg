package algs;

import java.util.Date;

public class RangeAlg extends Thread {

    private final C c;
    private Double limitedPrice;
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
    private Double feeAmount;
    private CExcelReader reader;


    public RangeAlg(C c, Double limitedPrice, Double volume, Double deltaPercent, Double ascendingPercent, Double feePercent) {
        this.c = c;
        this.limitedPrice = limitedPrice;
        this.baseVolume = volume;
        this.volume = 0.0;
        this.deltaPercent = deltaPercent;
        this.deltaPrice = deltaPercent * limitedPrice / 100;
        this.ascendingPercent = ascendingPercent;
        this.ascendingPrice = ascendingPercent * limitedPrice / 100;
        this.fee = feePercent / 100;
        this.running = true;
        this.broughtInAmount = 0.0;
        this.soldAmount = 0.0;
        this.feeAmount = 0.0;

        try {
            this.reader = new CExcelReader(this.c, "C:\\Users\\NoteBook\\Desktop\\Mine\\Code\\CAlg\\src\\main\\resources\\btc_usdt_dummy_year.xlsx");
            System.out.println("The file was read.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void stopThread() {
        running = false;
    }

    boolean buy(C c, Double price, Double volume) {
        return buyProxy(c, price, volume);
    }

    boolean sell(C c, Double price, Double volume) {
        return sellProxy(c, price, volume);
    }

    boolean buyProxy(C c, Double price, Double volume) {
        System.out.println("Buy  " + c + " , " + "price : " + price + " , volume : " + volume);
        return true;
    }

    boolean sellProxy(C c, Double price, Double volume) {
        System.out.println("Sell " + c + " , " + "price : " + price + " , volume : " + volume);
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
        System.out.println("broughtInAmount : " + (((double) Math.round(broughtInAmount * 100)) / 100));
        System.out.println("soldAmount : " + (((double) Math.round(soldAmount * 100)) / 100));
        System.out.println("volume : " + (((double) Math.round(volume * 100)) / 100));
        System.out.println("payedFeeAmount : " + (((double) Math.round(feeAmount * 100)) / 100));
        System.out.println("((soldAmount / broughtInAmount) - 1) * 100  : " + ((double) Math.round(((soldAmount / broughtInAmount) - 1) * 10000)) / 100);
        System.out.println("((volume / baseVolume) - 1) * 100  : " + ((double) Math.round(((volume / baseVolume) - 1) * 10000)) / 100);


    }

    void startAlg() {

        boolean buyCheck = false;
        boolean sellCheck = false;
        CRecord rec = null;

        while (!buyCheck) {
            rec = reader.getNext();
            if (rec != null && rec.getLow() < limitedPrice) {
                volume = soldAmount == 0.0 ? baseVolume : soldAmount / (rec.getLow() * (1 + fee));
                broughtInAmount = soldAmount == 0.0 ? volume * (rec.getLow() * (1 + fee)) : broughtInAmount;
                buyCheck = buy(rec.getC(), rec.getLow(), volume);
                feeAmount += volume * (rec.getLow() * fee);
                soldAmount = 0.0;
            } else if (rec == null) {
                running = false;
                sellCheck = true;
                break;
            }
        }

        while (!sellCheck) {
            rec = reader.getNext();
            if (rec != null && rec.getHigh() > (limitedPrice + deltaPrice)) {
                sellCheck = sell(rec.getC(), rec.getHigh(), volume);
                soldAmount = volume * rec.getHigh() * (1 - fee);
                feeAmount += volume * (rec.getHigh() * fee);
                volume = 0.0;
            } else if (rec == null) {
                running = false;
                break;
            }
        }
        limitedPrice += ascendingPrice;
        deltaPrice = deltaPercent * limitedPrice / 100;
        ascendingPrice = ascendingPercent * limitedPrice / 100;

    }
}



