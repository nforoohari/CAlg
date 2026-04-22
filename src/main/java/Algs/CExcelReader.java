package Algs;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class CExcelReader {

    private final Workbook workbook;
    private final Sheet sheet;
    private int currentRow = 1;   // رد شدن از ردیف header

    public CExcelReader(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        this.workbook = new XSSFWorkbook(fis);
        this.sheet = workbook.getSheetAt(0);
    }

    public CRecord getNext() {
        if (currentRow > sheet.getLastRowNum()) {
            return null;    // تمام شد
        }

        Row row = sheet.getRow(currentRow++);
        if (row == null) {
            return getNext(); // ردیف خالی → رد شو
        }

        String date     = row.getCell(0).getStringCellValue();
        double open     = row.getCell(1).getNumericCellValue();
        double high     = row.getCell(2).getNumericCellValue();
        double low      = row.getCell(3).getNumericCellValue();
        double close    = row.getCell(4).getNumericCellValue();
        double volume   = row.getCell(5).getNumericCellValue();

        return new CRecord(date, open, high, low, close, volume);
    }

    public void close() throws IOException {
        workbook.close();
    }
}
