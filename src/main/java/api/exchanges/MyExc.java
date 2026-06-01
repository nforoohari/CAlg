package api.exchanges;

import api.daos.Record;
import api.daos.RecordDao;
import api.enums.Currency;
import api.enums.Interval;
import api.orders.OrderRequest;
import api.orders.OrderState;
import api.orders.OrderTransaction;

import java.util.Date;
import java.util.List;

public class MyExc implements IExc {

    private Interval interval;
    private Currency currency;
    private String startTime;
    private String endTime;
    private List<Record> lcr;
    private final int lcrSize;

    private int cnt;


    public MyExc(Interval interval, Currency currency, String startTime, String endTime) throws Exception {
        this.interval = interval;
        this.currency = currency;
        this.startTime = startTime;
        this.endTime = endTime;

        this.lcr = RecordDao.load(interval.getTableName(), currency, startTime, endTime);
        this.lcrSize = lcr.size();
        this.cnt = 0;

    }

    @Override
    public OrderRequest buy(Currency currency, Double volume, Double price) throws Exception {
        System.out.println("Buy : " + currency.getName() + ", Volume : " + volume + ", Price : " + price);
        OrderRequest co = new OrderRequest();
        OrderState os = new OrderState(currency, "BUY", volume, price, new Date());
        OrderDAO.insertOrderStatus(os);
        co.setOrderStatus(os);
        return co;
    }

    @Override
    public OrderRequest sell(Currency currency, Double volume, Double price) throws Exception {
        System.out.println("Sell : " + currency.getName() + ", Volume : " + volume + ", Price : " + price);
        OrderRequest co = new OrderRequest();
        OrderState os = new OrderState(currency, "SELL", volume, price, new Date());
        OrderDAO.insertOrderStatus(os);
        co.setOrderStatus(os);
        return co;
    }

    @Override
    public Record fetchTradeData(Currency currency, String interval) throws Exception {

        Record record;

        if (this.currency != currency || !this.interval.getName().equals(interval)) return null;

        if (cnt < lcrSize) {
            record = lcr.get(cnt);
            offlineCheckOrderStatus(record);
            cnt++;

        }else {
            record = null;
        }
        return record;
    }

    @Override
    public OrderRequest checkOrderStatus(Currency currency, long orderId) throws Exception {
        OrderState os = OrderDAO.getOrderById(orderId);
        List<OrderTransaction> lod = OrderDAO.getOrderDetails(orderId);
        OrderRequest co = new OrderRequest();
        co.setOrderStatus(os);
        co.setTransactions(lod);
        return co;
    }

    private void offlineCheckOrderStatus(Record cr) throws Exception {

        List<OrderState> los;
        List<OrderTransaction> lod;

        los = OrderDAO.getOrdersByCompletion(cr.getCrypto(), false);

        for (OrderState os : los) {

            if ("BUY".equals(os.getSide()) && os.getPrice() > cr.getLow()) {

                lod = OrderDAO.getOrderDetails(os.getId());
                double bv = 0.0;
                double lv = 0.0;
                double ov = os.getVolume();
                double cv = cr.getVolume();
                boolean comp = false;

                for (OrderTransaction od : lod) {
                    bv += od.getVolume();
                }

                lv = Math.min((ov - bv), cv);
                comp = (ov - bv) <= cv;

                OrderDAO.insertOrderDetail(new OrderTransaction(os.getId(), lv, (cr.getLow() + cr.getClose()) / 2, cr.getDate()));

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

                for (OrderTransaction od : lod) {
                    sv += od.getVolume();
                }

                lv = Math.min((ov - sv), cv);
                comp = (ov - sv) <= cv;

                OrderDAO.insertOrderDetail(new OrderTransaction(os.getId(), lv, (cr.getHigh() + cr.getClose()) / 2, new Date()));

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

    public Currency getCrypto() {
        return currency;
    }

    public void setCrypto(Currency currency) {
        this.currency = currency;
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

    @Override
    public void submit(OrderRequest orderRequest) throws Exception {

    }

    @Override
    public void checkStatus(OrderRequest orderRequest) throws Exception {

    }

    @Override
    public void cancel(OrderRequest orderRequest) throws Exception {

    }

    @Override
    public void submitByConfirmation(OrderRequest orderRequest) throws Exception {

    }

    @Override
    public Record fetchExcData(Currency currency, String interval) throws Exception {
        return null;
    }
}
