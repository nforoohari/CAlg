package mainPackage;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.DefaultHighLowDataset;

import javax.swing.*;
import java.util.Date;
import java.util.List;

public class CandleChart {

    public static void show(List<CryptoRecord> candles) {

        int n = candles.size();

        Date[] date = new Date[n];
        double[] high = new double[n];
        double[] low = new double[n];
        double[] open = new double[n];
        double[] close = new double[n];
        double[] volume = new double[n];

        for (int i = 0; i < n; i++) {

            CryptoRecord c = candles.get(i);

            date[i] = c.getDate();
            high[i] = c.getHigh();
            low[i] = c.getLow();
            open[i] = c.getOpen();
            close[i] = c.getClose();
            volume[i] = c.getVolume();
        }

        String cn = candles.get(0).getCrypto().getName();
//        System.out.println(candles.size());

        DefaultHighLowDataset dataset =
                new DefaultHighLowDataset(
                        cn + "USDT",
                        date,
                        high,
                        low,
                        open,
                        close,
                        volume
                );

        JFreeChart chart =
                ChartFactory.createCandlestickChart(
                        cn + "/USDT",
                        "Time",
                        "Price",
                        dataset,
                        false
                );

        JFrame frame = new JFrame("Candlestick");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }

}
