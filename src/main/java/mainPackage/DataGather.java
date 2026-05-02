package mainPackage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DataGather {

    public DataReceiver dr;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DataGather(Crypto crypto) {

        this.dr = new DataReceiver(crypto);
    }

    public void gather(String interval, String startTime, String endTime) throws IOException, InterruptedException {

        LocalDateTime startDate = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endTime, formatter);

        long startMs = startDate.toInstant(ZoneOffset.UTC).toEpochMilli();
        long endMs = endDate.toInstant(ZoneOffset.UTC).toEpochMilli();

        long intervalMs = intervalToMillis(interval);
        long maxChunk = intervalMs * 1000;

        long currentStart = startMs;

        while (currentStart < endMs) {

            long currentEnd = Math.min(currentStart + maxChunk, endMs);
            dr.receive(interval, currentStart, currentEnd);
            currentStart = currentEnd + intervalMs;
            Thread.sleep(1000); //200ms
        }
    }

    private static long intervalToMillis(String interval) {
        if (interval.endsWith("s"))
            return Long.parseLong(interval.replace("s", "")) * 1000;
        if (interval.endsWith("m"))
            return Long.parseLong(interval.replace("m", "")) * 60_000;
        if (interval.endsWith("h"))
            return Long.parseLong(interval.replace("h", "")) * 3_600_000;
        if (interval.endsWith("d"))
            return Long.parseLong(interval.replace("d", "")) * 86_400_000;

        throw new IllegalArgumentException("Invalid interval");
    }

    public static void main(String[] args) {

        System.out.println(intervalToMillis("1s"));
        System.out.println(intervalToMillis("1m"));
        System.out.println(intervalToMillis("1h"));
        System.out.println(intervalToMillis("1d"));

    }

}
