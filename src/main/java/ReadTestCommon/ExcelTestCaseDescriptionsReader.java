package ReadTestCommon;

import com.google.common.base.Optional;
import static com.google.common.base.Preconditions.*;
import static com.google.common.collect.Maps.newHashMap;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

public class ExcelTestCaseDescriptionsReader {

	private final ClassLoader classLoader;
    private final String fileResourcePath;

    private static final String XLS_FORMAT = "xls";
    private static final String XLSX_FORMAT = "xlsx";
    private static final String SHEET_DESCRIPTIONS = "descriptions";
//    private static final String HEADER_TEST_CASE_NO = "Test Case Numbers";
//    private static final String HEADER_TEST_CASE_SUMMARY = "Test Case Summary";
//    private static final String HEADER_TEST_ENABLED = "Enabled";
//    private static final String HEADER_TAGS = "Tags";

    private ExcelTestCaseDescriptionsReader(final String fileResourcePath) {
        this.classLoader = ExcelTestCaseDescriptionsReader.class.getClassLoader();
        this.fileResourcePath = fileResourcePath;
    }

    private void load() {
        Optional<Workbook> wb = Optional.absent();
        try {
            final InputStream inputStream = classLoader.getResourceAsStream(fileResourcePath);
            wb = Optional.of(createWorkBook(inputStream, fileResourcePath));
            Optional<Sheet> descSheet = findSheet(wb.get(), SHEET_DESCRIPTIONS);
            checkArgument(descSheet.isPresent());

            Map<String, Integer> header2Column = newHashMap();
            // Iterate row by row
            Iterator<Row> rowIterator = descSheet.get().iterator();
            // Read the first header row.


            while (rowIterator.hasNext()) {
                final Row row = rowIterator.next();
                // For each row, iterate through each columns
                final Iterator<Cell> cellIterator = row.cellIterator();
                System.out.println();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    System.out.printf(" %s ", cell.getStringCellValue());
                }
                System.out.println("");
            }

        } catch (Exception e) {
            throw new RuntimeException(String.format("Unable to load test case descriptions Excel file=[%s]. Cause: ",
                    fileResourcePath), e);
        } finally {
            if (wb.isPresent()) { // If workbook is still open close it
            }
        }
    }

    public Map<String, Integer> extractHeader2ColumnMapping(final Iterator<Row> rowIterator ) {
        if (rowIterator.hasNext()) {
            Row firstRow = rowIterator.next();
            final Iterator<Cell> cellIterator = firstRow.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                //
                System.out.printf(" %s ", cell.getStringCellValue());
            }
        }
        return null;
    }

    private Optional<Sheet> findSheet(Workbook workbook, final String sheetName) {
        return Optional.fromNullable(workbook.getSheet(sheetName));
    }

    private Workbook createWorkBook(InputStream inputStream, String fileResourcePath) throws IOException {
        String normalizedResource = fileResourcePath.toLowerCase();
        if (normalizedResource.endsWith(XLS_FORMAT)) {
            return new HSSFWorkbook(inputStream);
        } else if (normalizedResource.endsWith(XLSX_FORMAT)) {
            return new XSSFWorkbook(inputStream);
        } else {
            throw new RuntimeException(String.format("Unsupported Excel file [%s] format", fileResourcePath));
        }

    }

    public static void load(String fileResourcePath) {
        ExcelTestCaseDescriptionsReader reader = new ExcelTestCaseDescriptionsReader(fileResourcePath);
        reader.load();
    }
    
}
