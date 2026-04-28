package algs;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class BFCFolderSequentialReader {

    private final List<Path> sortedFiles;
    private int currentFileIndex = 0;
    private BFCExcelFileReader currentReader;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");

    public BFCFolderSequentialReader(String folderPath) throws IOException {

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
            currentReader = new BFCExcelFileReader(sortedFiles.get(currentFileIndex));
            currentFileIndex++;
        } else {
            currentReader = null;
        }
    }

    public BFCRecord next() throws IOException {

        if (currentReader == null) return null;

        BFCRecord record = currentReader.next();

        if (record == null) {
            openNextFile();
            return next();
        }

        return record;
    }
}
