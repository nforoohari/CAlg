package api;

import java.util.Date;
import java.util.List;

public class Nem implements Ngi {

    private Lp lp;
    private Oh oh;
    private String startTime;
    private String endTime;
    private List<Rxc> lcr;
    private final int lcrSize;

    private int cnt;


    public Nem(Lp lp, Oh oh, String startTime, String endTime) throws Exception {
        this.lp = lp;
        this.oh = oh;
        this.startTime = startTime;
        this.endTime = endTime;

        this.lcr = Lwd.load(lp.getTableName(), oh, startTime, endTime);
        this.lcrSize = lcr.size();
        this.cnt = 0;

    }

    @Override
    public Ofc buy(Oh oh, Double volume, Double price) throws Exception {
        System.out.println("Buy : " + oh.getName() + ", Volume : " + volume + ", Price : " + price);
        Ofc co = new Ofc();
        Spo os = new Spo(oh, "BUY", volume, price, new Date());
        Dio.insertOrderStatus(os);
        co.setOrderStatus(os);
        return co;
    }

    @Override
    public Ofc sell(Oh oh, Double volume, Double price) throws Exception {
        System.out.println("Sell : " + oh.getName() + ", Volume : " + volume + ", Price : " + price);
        Ofc co = new Ofc();
        Spo os = new Spo(oh, "SELL", volume, price, new Date());
        Dio.insertOrderStatus(os);
        co.setOrderStatus(os);
        return co;
    }

    @Override
    public Rxc getMarketInfo(Oh oh, String interval) throws Exception {

        Rxc rxc;

        if (this.oh != oh || !this.lp.getName().equals(interval)) return null;

        if (cnt < lcrSize) {
            rxc = lcr.get(cnt);
            offlineCheckOrderStatus(rxc);
            cnt++;

        }else {
            rxc = null;
        }
        return rxc;
    }

    @Override
    public Ofc checkOrderStatus(Oh oh, long orderId) throws Exception {
        Spo os = Dio.getOrderById(orderId);
        List<Djo> lod = Dio.getOrderDetails(orderId);
        Ofc co = new Ofc();
        co.setOrderStatus(os);
        co.setDetails(lod);
        return co;
    }

    private void offlineCheckOrderStatus(Rxc cr) throws Exception {

        List<Spo> los;
        List<Djo> lod;

        los = Dio.getOrdersByCompletion(cr.getCrypto(), false);

        for (Spo os : los) {

            if ("BUY".equals(os.getSide()) && os.getPrice() > cr.getLow()) {

                lod = Dio.getOrderDetails(os.getId());
                double bv = 0.0;
                double lv = 0.0;
                double ov = os.getVolume();
                double cv = cr.getVolume();
                boolean comp = false;

                for (Djo od : lod) {
                    bv += od.getVolume();
                }

                lv = Math.min((ov - bv), cv);
                comp = (ov - bv) <= cv;

                Dio.insertOrderDetail(new Djo(os.getId(), lv, (cr.getLow() + cr.getClose()) / 2, cr.getDate()));

                if (comp) {
                    Dio.completeOrder(os.getId(),cr.getDate());
                }

            } else if ("SELL".equals(os.getSide()) && os.getPrice() < cr.getHigh()) {

                lod = Dio.getOrderDetails(os.getId());
                double sv = 0.0;
                double lv = 0.0;
                double ov = os.getVolume();
                double cv = cr.getVolume();
                boolean comp = false;

                for (Djo od : lod) {
                    sv += od.getVolume();
                }

                lv = Math.min((ov - sv), cv);
                comp = (ov - sv) <= cv;

                Dio.insertOrderDetail(new Djo(os.getId(), lv, (cr.getHigh() + cr.getClose()) / 2, new Date()));

                if (comp) {
                    Dio.completeOrder(os.getId(),cr.getDate());
                }
            }
        }

    }

    public Lp getInterval() {
        return lp;
    }

    public void setInterval(Lp lp) {
        this.lp = lp;
    }

    public Oh getCrypto() {
        return oh;
    }

    public void setCrypto(Oh oh) {
        this.oh = oh;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}
