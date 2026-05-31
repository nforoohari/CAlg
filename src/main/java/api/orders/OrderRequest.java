package api.orders;

import api.enums.Currency;
import api.enums.Exchange;
import api.enums.Side;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderRequest {

    private long id;
    private Exchange exchange;
    private Currency currency;
    private Side side;
    private double capital;
    private double price;
    private double fee;
    private Date date;

    private OrderState state;
    private List<OrderTransaction> transactions;

    public OrderRequest() {
        this.transactions = new ArrayList<>();
        this.date = new Date();
    }

    public OrderRequest(Currency currency, Side side, double capital, double price) {
        this.transactions = new ArrayList<>();
        this.date = new Date();
        this.currency = currency;
        this.side = side;
        this.capital = capital;
        this.price = price;
    }

    public OrderState getOrderStatus() {
        return this.state;
    }

    public void setOrderStatus(OrderState orderState) {
        this.state = orderState;
    }

    public List<OrderTransaction> getTransactions() {
        return this.transactions;
    }

    public void setTransactions(List<OrderTransaction> transactions) {
        this.transactions = transactions;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public double getCapital() {
        return capital;
    }

    public void setCapital(double capital) {
        this.capital = capital;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
