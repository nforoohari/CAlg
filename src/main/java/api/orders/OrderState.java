package api.orders;

import api.enums.Side;
import api.enums.Status;

import java.util.Date;

public class OrderState {

    private long id;
    private long requestId;
    private long orderId;
    private Date orderDate;
    private double volume;
    private double balance;
    private double payedFee;
    private Status status;
    private Date statusDate;

    public OrderState() {
    }

    public OrderState(Side side, double capital) {

        this.orderId = 0;
        this.orderDate = new Date();
        this.volume = side == Side.BUY ? 0 : capital;
        this.balance = side == Side.BUY ? capital : 0;
        this.payedFee = 0;
        this.status = Status.In_Progress;
        this.statusDate = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getPayedFee() {
        return payedFee;
    }

    public void setPayedFee(double payedFee) {
        this.payedFee = payedFee;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }
}