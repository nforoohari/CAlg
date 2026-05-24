package api;

import java.util.Date;

public class Djo {

    private long id;
    private long orderStatusId;
    private double volume;
    private double price;
    private Date detailDate;

    public Djo() {

    }
    public Djo(long orderStatusId, double volume, double price, Date detailDate) {
        this.orderStatusId = orderStatusId;
        this.volume = volume;
        this.price = price;
        this.detailDate = detailDate;
    }

    public long getId() {
        return id;
    }

    public long getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(long orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDetailDate() {
        return detailDate;
    }

    public void setDetailDate(Date detailDate) {
        this.detailDate = detailDate;
    }
}