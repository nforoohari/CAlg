package mainPackage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelFolderReader {

    private final Currency currency;
    private final Interval interval;
    private final List<Path> sortedFiles;
    private int currentFileIndex = 0;
    private ExcelFileReader currentReader;
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");

    public ExcelFolderReader(Currency currency, Interval interval, String folderPath) throws IOException {

        this.currency = currency;
        this.interval=  interval;

        // گرفتن همه فایل‌های excel
//        List<Path> files = Files.list(Paths.get(folderPath))
        List<Path> files = Files.list(Path.of(folderPath))
                .filter(p -> p.toString().endsWith(".xlsx"))
                .filter(p -> p.toString().contains(this.currency.getName()))
                .filter(p -> p.toString().contains(this.interval.getName()))
                .toList();

        // مرتب‌سازی بر اساس نام فایل (که تاریخ است)
        this.sortedFiles = files.stream()
                .sorted(Comparator.comparing(this::extractDateTime))
                .collect(Collectors.toList());

        openNextFile();
    }

    private LocalDateTime extractDateTime(Path path) {
        String fileName = path.getFileName().toString()
                .replace(".xlsx", "")
                .replace(this.currency.getName(), "")
                .replace(this.interval.getName(), "")
                .trim();
        return LocalDateTime.parse(fileName, FORMATTER);
    }

    private void openNextFile() throws IOException {
        if (currentFileIndex < sortedFiles.size()) {
            if (currentReader != null) {
                currentReader.close();
                System.out.println("\n********************************************** Next File ***************************************************\n");
            }
            currentReader = new ExcelFileReader(sortedFiles.get(currentFileIndex));
            currentFileIndex++;
        } else {
            currentReader = null;
        }
    }

    public Record next() throws IOException, ParseException {

        if (currentReader == null) return null;

        Record record = currentReader.next();

        if (record == null) {
            openNextFile();
            return next();
        }
        record.setCurrency(currency);
        return record;
    }
}
