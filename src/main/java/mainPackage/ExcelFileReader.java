package mainPackage;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.TimeZone;

public class ExcelFileReader implements AutoCloseable {

    private final Workbook workbook;
    private final Iterator<Row> rowIterator;
    private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ExcelFileReader(Path path) throws IOException {
        this.workbook = new XSSFWorkbook(new FileInputStream(path.toFile()));
        Sheet sheet = workbook.getSheetAt(0);
        this.rowIterator = sheet.iterator();
        df.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));

        // skip header
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }
    }

    public CryptoRecord next() throws ParseException {
        if (!rowIterator.hasNext()) return null;

        Row row = rowIterator.next();

        return new CryptoRecord(
                df.parse(row.getCell(0).getStringCellValue()),
                row.getCell(1).getNumericCellValue(),
                row.getCell(2).getNumericCellValue(),
                row.getCell(3).getNumericCellValue(),
                row.getCell(4).getNumericCellValue(),
                row.getCell(5).getNumericCellValue()
        );
    }

    @Override
    public void close() throws IOException {
        workbook.close();
    }
}
