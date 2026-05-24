package api;

import java.util.Date;

public class OrderStatus {

    private long id;
    private Crypto crypto;
    private String side;
    private double volume;
    private double price;
    private Date orderedDate;
    private boolean completed;
    private Date completedDate;

    public OrderStatus() {
    }

    public OrderStatus(Crypto crypto, String side, double volume, double price, Date orderedDate) {
        this.crypto = crypto;
        this.side = side;
        this.volume = volume;
        this.price = price;
        this.orderedDate = orderedDate;
        this.completed = false;
        this.completedDate=null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Crypto getCrypto() {
        return crypto;
    }

    public void setCrypto(Crypto crypto) {
        this.crypto = crypto;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
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

    public Date getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(Date orderedDate) {
        this.orderedDate = orderedDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
}