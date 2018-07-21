package hsim.checkpoint.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import hsim.checkpoint.exception.ValidationLibException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;

/**
 * The type Validation file util.
 */
@Slf4j
public class ValidationFileUtil {

    /**
     * Gets encoding file name.
     *
     * @param fn the fn
     * @return the encoding file name
     */
    public static String getEncodingFileName(String fn) {
        try {
            return URLEncoder.encode(fn, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ValidationLibException("unSupported fiel encoding : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Init file send header.
     *
     * @param res         the res
     * @param filename    the filename
     * @param contentType the content type
     */
    public static void initFileSendHeader(HttpServletResponse res, String filename, String contentType) {

        filename = getEncodingFileName(filename);
        if (contentType != null) {
            res.setContentType(contentType);
        } else {
            res.setContentType("applicaiton/download;charset=utf-8");
        }
        res.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\";");
        res.setHeader("Content-Transfer-Encoding", "binary");
        res.setHeader("file-name", filename);

    }

    /**
     * Send file to http service response.
     *
     * @param file        the file
     * @param res         the res
     * @param contentType the content type
     */
    public static void sendFileToHttpServiceResponse(File file, HttpServletResponse res, String contentType) {

        if (file == null || res == null) {
            return;
        }
        String filename = getEncodingFileName(file.getName());
        initFileSendHeader(res, filename, contentType);

        res.setContentLength((int) file.length());
        try {
            res.getOutputStream().write(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new ValidationLibException("file io error :" + e.getMessage());
        }
    }

    /**
     * Send file to http service response.
     *
     * @param fileName the file name
     * @param bodyObj  the body obj
     * @param res      the res
     */
    public static void sendFileToHttpServiceResponse(String fileName, Object bodyObj, HttpServletResponse res) {

        if (fileName == null || bodyObj == null) {
            return;
        }

        String body = null;
        try {
            body = ValidationObjUtil.getDefaultObjectMapper().writeValueAsString(bodyObj);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }

        String filename = getEncodingFileName(fileName);
        initFileSendHeader(res, filename, null);

        byte[] bytes = body.getBytes();
        res.setContentLength(bytes.length);
        try {
            res.getOutputStream().write(bytes);
        } catch (IOException e) {
            throw new ValidationLibException("file io error :" + e.getMessage());
        }
    }
}
