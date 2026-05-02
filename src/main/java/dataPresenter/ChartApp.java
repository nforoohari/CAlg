package dataPresenter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class ChartApp {

    public static void main(String[] args) throws Exception {

        LocalDateTime start =
                LocalDateTime.of(2025,1,1,0,0);

        LocalDateTime end =
                LocalDateTime.of(2025,1,2,0,0);

        long startMs = start.toInstant(ZoneOffset.UTC).toEpochMilli();
        long endMs   = end.toInstant(ZoneOffset.UTC).toEpochMilli();

        List<Candle> candles =
                CandleRepository.load(startMs,endMs);

        CandleChart.show(candles);
    }
}
