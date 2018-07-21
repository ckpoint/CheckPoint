package hsim.checkpoint.setting.service;

import hsim.checkpoint.util.excel.PoiWorkBook;

/**
 * The interface Msg excel service.
 */
public interface MsgExcelService {
    /**
     * Gets all excels.
     *
     * @return the all excels
     */
    PoiWorkBook getAllExcels();

    /**
     * Gets excel.
     *
     * @param method the method
     * @param url    the url
     * @return the excel
     */
    PoiWorkBook getExcel(String method, String url);
}
