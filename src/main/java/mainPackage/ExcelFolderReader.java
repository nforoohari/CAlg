package mainPackage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelFolderReader {

    private final Crypto crypto;
    private final List<Path> sortedFiles;
    private int currentFileIndex = 0;
    private ExcelFileReader currentReader;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");

    public ExcelFolderReader(Crypto crypto, String folderPath) throws IOException {
        this.crypto = crypto;

        // گرفتن همه فایل‌های excel
        List<Path> files = Files.list(Paths.get(folderPath))
                .filter(p -> p.toString().endsWith(".xlsx"))
                .collect(Collectors.toList());

        // مرتب‌سازی بر اساس نام فایل (که تاریخ است)
        this.sortedFiles = files.stream()
                .sorted(Comparator.comparing(this::extractDateTime))
                .collect(Collectors.toList());

        openNextFile();
    }

    private LocalDateTime extractDateTime(Path path) {
        String fileName = path.getFileName().toString().replace(".xlsx", "");
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

    public CryptoRecord next() throws IOException, ParseException {

        if (currentReader == null) return null;

        CryptoRecord record = currentReader.next();

        if (record == null) {
            openNextFile();
            return next();
        }
        record.setCrypto(crypto);
        return record;
    }
}
