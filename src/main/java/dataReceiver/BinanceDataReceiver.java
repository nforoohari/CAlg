package dataReceiver;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;

import java.io.FileOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class BinanceDataReceiver {

    public static void main(String[] args) throws Exception {

        // --- تنظیم بازه زمانی دو سال اخیر ---
        LocalDateTime endDate = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime startDate = endDate.minus(730, ChronoUnit.DAYS);

        long startMs = startDate.toInstant(ZoneOffset.UTC).toEpochMilli();
        long endMs   = endDate.toInstant(ZoneOffset.UTC).toEpochMilli();

        // --- آدرس API ---
        String url = "https://api.binance.com/api/v3/klines";

        String fullUrl = url +
                "?symbol=ETHUSDT" +
                "&interval=1s" +
                "&startTime=" + startMs +
                "&endTime=" + endMs +
                "&limit=1000";

        // --- ارسال درخواست HTTP ---
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("HTTP Error: " + response.statusCode());
        }

        // --- دریافت JSON ---
        JSONArray json = new JSONArray(response.body());

        // --- ایجاد فایل Excel ---
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ETHUSDT");

        // Header
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("date");
        header.createCell(1).setCellValue("open");
        header.createCell(2).setCellValue("high");
        header.createCell(3).setCellValue("low");
        header.createCell(4).setCellValue("close");
        header.createCell(5).setCellValue("volume");

        // Data
        int rowIndex = 1;

        for (int i = 0; i < json.length(); i++) {

            JSONArray rowJson = json.getJSONArray(i);

            long openTimeMs   = rowJson.getLong(0);
            double open       = rowJson.getDouble(1);
            double high       = rowJson.getDouble(2);
            double low        = rowJson.getDouble(3);
            double close      = rowJson.getDouble(4);
            double volume     = rowJson.getDouble(5);

            LocalDateTime date =
                    Instant.ofEpochMilli(openTimeMs)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDateTime();

            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(date.toString());
            row.createCell(1).setCellValue(open);
            row.createCell(2).setCellValue(high);
            row.createCell(3).setCellValue(low);
            row.createCell(4).setCellValue(close);
            row.createCell(5).setCellValue(volume);
        }

        // تنظیم عرض ستون‌ها
        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }

        // خروجی Excel
        String output = "eth_usdt_last_2years_seconds_utc.xlsx";

        try (FileOutputStream out = new FileOutputStream(output)) {
            workbook.write(out);
        }

        workbook.close();

        System.out.println("فایل با موفقیت ساخته شد: " + output);
    }
}