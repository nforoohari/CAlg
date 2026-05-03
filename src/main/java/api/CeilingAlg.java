package api;

public class CeilingAlg implements AlgInterface {
    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void setParameters() {

    }

    @Override
    public void initialize() {

    }
}

//package dataAnalyzer.dummy;
//
//        import java.util.Date;
//
//public class CeilingAlg extends Thread {
//
//    private final C c;
//    private Double limitedPrice;
//    private Double stopLoss;
//    private final Double baseVolume;
//    private Double volume;
//    private final Double delta;
//    private Double deltaPrice;
//    private final Double ascending;
//    private Double ascendingPrice;
//    private final Double fee;
//    private volatile boolean running;
//    private Double broughtInAmount;
//    private Double soldAmount;
//    private Double feeAmount;
//    private boolean firstTime;
//    private CExcelReader reader;
//
//    public CeilingAlg(C c, Double limitedPrice, Double stopLoss, Double volume, Double deltaPercent, Double ascendingPercent, Double feePercent) {
//        this.c = c;
//        this.limitedPrice = limitedPrice;
//        this.stopLoss = stopLoss;
//        this.baseVolume = volume;
//        this.volume = volume;
//        this.delta = deltaPercent / 100;
//        this.deltaPrice = this.delta * limitedPrice;
//        this.ascending = ascendingPercent / 100;
//        this.ascendingPrice = this.ascending * limitedPrice;
//        this.fee = feePercent / 100;
//        this.running = true;
//        this.broughtInAmount = 0.0;
//        this.soldAmount = 0.0;
//        this.feeAmount = 0.0;
//        this.firstTime = true;
//        try {
//            this.reader = new CExcelReader(this.c, "C:\\Users\\NoteBook\\Desktop\\Mine\\Code\\CAlg\\src\\main\\resources\\dummy\\btc_usdt_dummy_year.xlsx");
//            System.out.println("The file was read.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void stopThread() {
//        running = false;
//    }
//
//    boolean buy(C c, Double price, Double volume) {
//        return buyProxy(c, price, volume);
//    }
//
//    boolean sell(C c, Double price, Double volume) {
//        return sellProxy(c, price, volume);
//    }
//
//    boolean buyProxy(C c, Double price, Double volume) {
//        System.out.println("Buy  " + c + " , " + "price : " + price + " , volume : " + volume);
//        return true;
//    }
//
//    boolean sellProxy(C c, Double price, Double volume) {
//        System.out.println("Sell " + c + " , " + "price : " + price + " , volume : " + volume);
//        return true;
//    }
//
//    @Override
//    public void run() {
//
//        System.out.println("Thread started at : " + new Date());
//
//        while (running) {
//            System.out.println("limitedPrice : " + limitedPrice);
//            System.out.println("stopLoss : " + stopLoss);
//            System.out.println("deltaPrice : " + deltaPrice);
//            System.out.println("limitedPrice - deltaPrice : " + (limitedPrice - deltaPrice));
//            System.out.println("ascendingPrice : " + ascendingPrice);
//            startAlg();
//        }
//
//        System.out.println("Thread stopped safely at : " + new Date());
//        System.out.println("broughtInAmount : " + (((double) Math.round(broughtInAmount * 100)) / 100));
//        System.out.println("soldAmount : " + (((double) Math.round(soldAmount * 100)) / 100));
//        System.out.println("volume : " + volume);
//        System.out.println("payedFeeAmount : " + (((double) Math.round(feeAmount * 100)) / 100));
//        System.out.println("((soldAmount / broughtInAmount) - 1) * 100  : " + ((double) Math.round(((soldAmount / broughtInAmount) - 1) * 10000)) / 100);
//        System.out.println("((volume / baseVolume) - 1) * 100  : " + ((double) Math.round(((volume / baseVolume) - 1) * 10000)) / 100);
//
//    }
//
//    void startAlg() {
//
//        boolean buyCheck = false;
//        boolean sellCheck = false;
//        CRecord rec = null;
//
//        while (!sellCheck) {
//            rec = reader.getNext();
//            if (rec != null) {
//                if (rec.getHigh() > limitedPrice) {
//
//                    broughtInAmount = firstTime ? baseVolume * rec.getHigh() : broughtInAmount;
//                    sellCheck = sell(rec.getC(), rec.getHigh(), volume);
//                    soldAmount = volume * rec.getHigh() * (1 - fee);
//                    feeAmount += volume * (rec.getHigh() * fee);
//                    volume = 0.0;
//                    firstTime = false;
//
//                }
//            } else {
//
//                running = false;
//                buyCheck = true;
//                break;
//
//            }
//        }
//
//        while (!buyCheck) {
//            rec = reader.getNext();
//            if (rec != null) {
//                if (rec.getLow() < (limitedPrice - deltaPrice)) {
//
//                    volume = soldAmount / (rec.getLow() * (1 + fee));
//                    buyCheck = buy(rec.getC(), rec.getLow(), volume);
//                    feeAmount += volume * rec.getLow() * fee;
//                    soldAmount = 0.0;
//
//                    deltaPrice = (deltaPrice + ascendingPrice) > (2 * fee * limitedPrice) ? (deltaPrice + ascendingPrice) : deltaPrice;
//
//                } else if (rec.getClose() > stopLoss) {
//
//                    volume = soldAmount / (rec.getClose() * (1 + fee));
//                    buyCheck = buy(rec.getC(), rec.getClose(), volume);
//                    feeAmount += volume * (rec.getClose() * fee);
//                    soldAmount = 0.0;
//
//                    running = false;
//
//                }
//            } else {
//
//                running = false;
//                break;
//
//            }
//        }
//    }
//}



