package hsim.checkpoint.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import hsim.checkpoint.exception.ValidationLibException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * The type Validation file util.
 */
@Slf4j
public class ValidationFileUtil {


    private static BufferedReader getBufferReader(final File file, final Charset charset) throws IOException {
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            throw new IOException("file status not normal : " + file.getName());
        }
        return Files.newBufferedReader(file.toPath(), charset);
    }

    /**
     * Read file to string string.
     *
     * @param file    the file
     * @param charset the charset
     * @return the string
     * @throws IOException the io exception
     */
    public static String readFileToString(File file, final Charset charset) throws IOException {
        String line = null;
        BufferedReader reader = getBufferReader(file, charset);
        StringBuffer strBuffer = new StringBuffer();

        while ((line = reader.readLine()) != null) {
            strBuffer.append(line);
        }
        return strBuffer.toString();
    }


    private static OutputStream getOutputStream(final File file) throws IOException {
        if(!file.getParentFile().exists()) {
            Files.createDirectory(file.getParentFile().toPath());
        }
        return Files.newOutputStream(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }

    /**
     * Write string to file.
     *
     * @param file    the file
     * @param content the content
     * @throws IOException the io exception
     */
    public static void writeStringToFile(File file, String content) throws IOException {
        OutputStream outputStream = getOutputStream(file);
        outputStream.write(content.getBytes());
    }

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
