package hsim.checkpoint.util.excel;

import lombok.Getter;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 * The type Poi cell style.
 */
@Getter
public class PoiCellStyle {

    private static final short DATE_FORMAT_VALUE = 0xe;
    private HSSFCellStyle numberCs;
    private HSSFCellStyle stringCs;
    private HSSFCellStyle dateCs;

    /**
     * Instantiates a new Poi cell style.
     *
     * @param workBook the work book
     */
    public PoiCellStyle(PoiWorkBook workBook) {

        this.stringCs = this.getDefaultExcelCellStyle(workBook);

        this.numberCs = this.getDefaultExcelCellStyle(workBook);
        this.numberCs.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        this.numberCs.setAlignment(HorizontalAlignment.RIGHT);

        this.dateCs = this.getDefaultExcelCellStyle(workBook);
        this.dateCs.setDataFormat(DATE_FORMAT_VALUE);
    }

    private HSSFCellStyle getDefaultExcelCellStyle(PoiWorkBook daouWorkBook) {

        HSSFWorkbook wb = daouWorkBook.getWorkBook();

        HSSFCellStyle cs = wb.createCellStyle();
        cs.setAlignment(HorizontalAlignment.CENTER);
        cs.setVerticalAlignment(VerticalAlignment.CENTER);
        cs.setBorderTop(BorderStyle.THIN);
        cs.setBorderRight(BorderStyle.THIN);
        cs.setBorderLeft(BorderStyle.THIN);
        cs.setBorderBottom(BorderStyle.THIN);

        return cs;
    }

}
