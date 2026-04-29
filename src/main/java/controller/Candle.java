package controller;

public class Candle {

    long time;
    double open;
    double high;
    double low;
    double close;
    double volume;

    public Candle(long time,double open,double high,double low,double close,double volume){
        this.time=time;
        this.open=open;
        this.high=high;
        this.low=low;
        this.close=close;
        this.volume=volume;
    }
}
