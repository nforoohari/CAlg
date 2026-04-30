package dataReader.python;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.Iterator;

public class BFCExcelFileReader implements AutoCloseable {

    private final Workbook workbook;
    private final Iterator<Row> rowIterator;

    public BFCExcelFileReader(Path path) throws IOException {
        this.workbook = new XSSFWorkbook(new FileInputStream(path.toFile()));
        Sheet sheet = workbook.getSheetAt(0);
        this.rowIterator = sheet.iterator();

        // skip header
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }
    }

    public BFCRecord next() {
        if (!rowIterator.hasNext()) return null;

        Row row = rowIterator.next();

        Date date = row.getCell(0).getDateCellValue();
        double open = Double.parseDouble(row.getCell(1).getStringCellValue());
        double high = Double.parseDouble(row.getCell(2).getStringCellValue());
        double low = Double.parseDouble(row.getCell(3).getStringCellValue());
        double close = Double.parseDouble(row.getCell(4).getStringCellValue());
        double volume = Double.parseDouble(row.getCell(5).getStringCellValue());
        return new BFCRecord(date, open, high, low, close, volume);

    }

    @Override
    public void close() throws IOException {
        workbook.close();
    }
}
