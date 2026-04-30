package dataReader.java;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
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

        return new BFCRecord(
                row.getCell(0).toString(),
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
