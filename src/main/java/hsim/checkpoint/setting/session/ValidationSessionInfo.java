package hsim.checkpoint.setting.session;

import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * The type Validation session info.
 */
@Getter
public class ValidationSessionInfo {

    private String token;
    private String ipAddress;
    private Date createDate;

    /**
     * Instantiates a new Validation session info.
     *
     * @param req the req
     * @param t   the t
     */
    public ValidationSessionInfo(HttpServletRequest req, String t) {
        this.token = t;
        this.createDate = new Date();
        this.ipAddress = req.getRemoteAddr();
    }

}
