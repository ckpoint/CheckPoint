/**
 *
 */
package hsim.checkpoint.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Create by hsim on 2017. 12. 11.
 */
@Slf4j
public class ValidationHttpUtil {

    /**
     * Read body string.
     *
     * @param request the request
     * @return the string
     */
    public static String readBody(HttpServletRequest request) {

        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String buffer;
            while ((buffer = input.readLine()) != null) {
                if (builder.length() > 0) {
                    builder.append("\n");
                }
                builder.append(buffer);
            }
            return builder.toString();
        } catch (IOException e) {
            log.info("http io exception : " + e.getMessage());
        }
        return null;
    }


}
