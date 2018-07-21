package hsim.checkpoint.util.excel;

import hsim.checkpoint.exception.ValidationLibException;
import hsim.checkpoint.util.ValidationFileUtil;
import lombok.Getter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type Poi work book.
 */
@Getter
public class PoiWorkBook {

    private HSSFWorkbook workBook;

    /**
     * Instantiates a new Poi work book.
     */
    public PoiWorkBook() {
        this.checkDependency();
        this.workBook = new HSSFWorkbook();
    }

    private void checkDependency() {
        try {
           Class.forName("org.apache.poi.hssf.usermodel.HSSFWorkbook");
        } catch (ClassNotFoundException e) {
            throw new ValidationLibException("Not found apache POI library, must import poi to your maven or gradle dependency", HttpStatus.FAILED_DEPENDENCY);
        }
    }


    /**
     * Create sheet poi work sheet.
     *
     * @return the poi work sheet
     */
    public PoiWorkSheet createSheet() {
        return new PoiWorkSheet(this, null);
    }

    /**
     * Create sheet poi work sheet.
     *
     * @param sheetName the sheet name
     * @return the poi work sheet
     */
    public PoiWorkSheet createSheet(String sheetName) {
        return new PoiWorkSheet(this, sheetName);
    }

    /**
     * Write file.
     *
     * @param fn  the fn
     * @param res the res
     */
    public void writeFile(String fn, HttpServletResponse res) {

        ValidationFileUtil.initFileSendHeader(res, ValidationFileUtil.getEncodingFileName(fn + ".xls"), null);

        try {
            this.workBook.write(res.getOutputStream());
        } catch (IOException e) {
            throw new ValidationLibException("workbook write error  : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }


}
