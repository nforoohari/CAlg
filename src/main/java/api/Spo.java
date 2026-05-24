package api;

import java.util.Date;

public class Spo {

    private long id;
    private Oh oh;
    private String side;
    private double volume;
    private double price;
    private Date orderedDate;
    private boolean completed;
    private Date completedDate;

    public Spo() {
    }

    public Spo(Oh oh, String side, double volume, double price, Date orderedDate) {
        this.oh = oh;
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

    public Oh getCrypto() {
        return oh;
    }

    public void setCrypto(Oh oh) {
        this.oh = oh;
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