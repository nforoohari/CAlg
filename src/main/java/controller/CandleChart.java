package controller;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultHighLowDataset;

import javax.swing.*;
import java.util.Date;
import java.util.List;

public class CandleChart {

    public static void show(List<Candle> candles){

        int n = candles.size();

        Date[] date = new Date[n];
        double[] high = new double[n];
        double[] low = new double[n];
        double[] open = new double[n];
        double[] close = new double[n];
        double[] volume = new double[n];

        for(int i=0;i<n;i++){

            Candle c = candles.get(i);

            date[i] = new Date(c.time);
            high[i] = c.high;
            low[i] = c.low;
            open[i] = c.open;
            close[i] = c.close;
            volume[i] = c.volume;
        }

        DefaultHighLowDataset dataset =
                new DefaultHighLowDataset(
                        "ETHUSDT",
                        date,
                        high,
                        low,
                        open,
                        close,
                        volume
                );

        JFreeChart chart =
                ChartFactory.createCandlestickChart(
                        "ETH/USDT",
                        "Time",
                        "Price",
                        dataset,
                        false
                );

        JFrame frame = new JFrame("Candlestick");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.setSize(1000,600);
        frame.setVisible(true);
    }
}
