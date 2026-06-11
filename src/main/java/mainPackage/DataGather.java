package mainPackage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DataGather {

    public DataReceiver dr;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DataGather(Currency currency) {

        this.dr = new DataReceiver(currency);
    }

    public void gather(Interval interval, String startTime, String endTime) throws IOException, InterruptedException {

        LocalDateTime startDate = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endTime, formatter);

        long startMs = startDate.toInstant(ZoneOffset.UTC).toEpochMilli();
        long endMs = endDate.toInstant(ZoneOffset.UTC).toEpochMilli();

        long intervalMs = interval.getMillis();
        long maxChunk = intervalMs * 1000;

        long currentStart = startMs;

        while (currentStart < endMs) {

            long currentEnd = Math.min(currentStart + maxChunk, endMs);
            dr.receive(interval, currentStart, currentEnd);
//            currentStart = currentEnd + intervalMs;
            currentStart = currentEnd;
            Thread.sleep(1000); //200ms
        }
    }
}
