package api;

import java.util.Date;
import java.util.List;

public class MyNet implements INet {

    private Interval interval;
    private Crypto crypto;
    private String startTime;
    private String endTime;
    private List<CryptoRecord> lcr;
    private final int lcrSize;

    private int cnt;


    public MyNet(Interval interval, Crypto crypto, String startTime, String endTime) throws Exception {
        this.interval = interval;
        this.crypto = crypto;
        this.startTime = startTime;
        this.endTime = endTime;

        this.lcr = DBLoader.load(interval.getTableName(), crypto, startTime, endTime);
        this.lcrSize = lcr.size();
        this.cnt = 0;

    }

    @Override
    public CryptoOrder buy(Crypto crypto, Double volume, Double price) throws Exception {
        System.out.println("Buy : " + crypto.getName() + ", Volume : " + volume + ", Price : " + price);
        CryptoOrder co = new CryptoOrder();
        OrderStatus os = new OrderStatus(crypto, "BUY", volume, price, new Date());
        OrderDAO.insertOrderStatus(os);
        co.setOrderStatus(os);
        return co;
    }

    @Override
    public CryptoOrder sell(Crypto crypto, Double volume, Double price) throws Exception {
        System.out.println("Sell : " + crypto.getName() + ", Volume : " + volume + ", Price : " + price);
        CryptoOrder co = new CryptoOrder();
        OrderStatus os = new OrderStatus(crypto, "SELL", volume, price, new Date());
        OrderDAO.insertOrderStatus(os);
        co.setOrderStatus(os);
        return co;
    }

    @Override
    public CryptoRecord getMarketInfo(Crypto crypto, String interval) throws Exception {

        CryptoRecord cryptoRecord;

        if (this.crypto != crypto || !this.interval.getName().equals(interval)) return null;

        if (cnt < lcrSize) {
            cryptoRecord = lcr.get(cnt);
            offlineCheckOrderStatus(cryptoRecord);
            cnt++;

        }else {
            cryptoRecord = null;
        }
        return cryptoRecord;
    }

    @Override
    public CryptoOrder checkOrderStatus(Crypto crypto, long orderId) throws Exception {
        OrderStatus os = OrderDAO.getOrderById(orderId);
        List<OrderDetails> lod = OrderDAO.getOrderDetails(orderId);
        CryptoOrder co = new CryptoOrder();
        co.setOrderStatus(os);
        co.setDetails(lod);
        return co;
    }

    private void offlineCheckOrderStatus(CryptoRecord cr) throws Exception {

        List<OrderStatus> los;
        List<OrderDetails> lod;

        los = OrderDAO.getOrdersByCompletion(cr.getCrypto(), false);

        for (OrderStatus os : los) {

            if ("BUY".equals(os.getSide()) && os.getPrice() > cr.getLow()) {

                lod = OrderDAO.getOrderDetails(os.getId());
                double bv = 0.0;
                double lv = 0.0;
                double ov = os.getVolume();
                double cv = cr.getVolume();
                boolean comp = false;

                for (OrderDetails od : lod) {
                    bv += od.getVolume();
                }

                lv = Math.min((ov - bv), cv);
                comp = (ov - bv) <= cv;

                OrderDAO.insertOrderDetail(new OrderDetails(os.getId(), lv, (cr.getLow() + cr.getClose()) / 2, cr.getDate()));

                if (comp) {
                    OrderDAO.completeOrder(os.getId(),cr.getDate());
                }

            } else if ("SELL".equals(os.getSide()) && os.getPrice() < cr.getHigh()) {

                lod = OrderDAO.getOrderDetails(os.getId());
                double sv = 0.0;
                double lv = 0.0;
                double ov = os.getVolume();
                double cv = cr.getVolume();
                boolean comp = false;

                for (OrderDetails od : lod) {
                    sv += od.getVolume();
                }

                lv = Math.min((ov - sv), cv);
                comp = (ov - sv) <= cv;

                OrderDAO.insertOrderDetail(new OrderDetails(os.getId(), lv, (cr.getHigh() + cr.getClose()) / 2, new Date()));

                if (comp) {
                    OrderDAO.completeOrder(os.getId(),cr.getDate());
                }
            }
        }

    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
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
