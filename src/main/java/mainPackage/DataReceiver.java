package mainPackage;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DataReceiver {

    private Crypto crypto;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // --- آدرس API ---
    private final String url = "https://api.binance.com/api/v3/klines";

    private final String path = "C:\\Users\\NoteBook\\Desktop\\Mine\\Code\\CAlg\\src\\main\\resources\\main\\";

    public DataReceiver(Crypto crypto) {
        this.crypto = crypto;
    }

    public void receive(String interval, String startTime, String endTime) throws IOException, InterruptedException {

        LocalDateTime startDate = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endTime, formatter);

        long startMs = startDate.toInstant(ZoneOffset.UTC).toEpochMilli();
        long endMs = endDate.toInstant(ZoneOffset.UTC).toEpochMilli();

        String fullUrl = url +
                "?symbol=" + crypto.getName() + "USDT" +
                "&interval=" + interval +
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
        Sheet sheet = workbook.createSheet(crypto.getName() + "USDT");

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

            long openTimeMs = rowJson.getLong(0);
            double open = rowJson.getDouble(1);
            double high = rowJson.getDouble(2);
            double low = rowJson.getDouble(3);
            double close = rowJson.getDouble(4);
            double volume = rowJson.getDouble(5);

            LocalDateTime date =
                    Instant.ofEpochMilli(openTimeMs)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDateTime();

            Row row = sheet.createRow(rowIndex++);

//            row.createCell(0).setCellValue(date.toString());
            row.createCell(0).setCellValue(date.format(formatter));
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
//        String output = "eth_usdt_last_2years_seconds_utc.xlsx";
        String output = crypto.getName() + " " + startTime + " " + interval + ".xlsx";

        try (FileOutputStream out = new FileOutputStream(path + output)) {
            workbook.write(out);
        }

        workbook.close();

        System.out.println("فایل با موفقیت ساخته شد: " + output);

    }

    public static void main(String[] args) throws Exception {

        DataReceiver dr = new DataReceiver(Crypto.Ethereum);
        dr.receive("1d", "2025-01-01 00:00:00", "2025-02-01 00:00:00");
    }
}