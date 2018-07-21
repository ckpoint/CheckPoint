package hsim.checkpoint.setting.session;

import hsim.checkpoint.config.ValidationConfig;
import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.exception.ValidationLibException;
import org.springframework.http.HttpStatus;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * The type Validation session component.
 */
public class ValidationSessionComponent {

    private static final String SESSION_KEY_MAP = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int SESSION_KEY_LENGTH = 50;
    private static final long VALID_SESSION_MSECOND = 1000 * 60 * 60 * 24;
    private ValidationConfig validationConfig = ComponentMap.get(ValidationConfig.class);
    private Map<String, ValidationSessionInfo> sessionMap = new HashMap<>();

    private boolean validationSessionCheck(String token) {
        ValidationSessionInfo info = this.sessionMap.get(token);
        if (info == null) {
            return false;
        }

        long validationTime = info.getCreateDate().getTime() + VALID_SESSION_MSECOND;
        if (validationTime < System.currentTimeMillis()) {
            return false;
        }
        return true;
    }

    private boolean validaitonSessionCheckAndRemake(String token) {
        if (this.validationSessionCheck(token)) {
            this.sessionMap.remove(token);
            return true;
        }
        return false;
    }

    /**
     * Session check.
     *
     * @param req the req
     */
    public void sessionCheck(HttpServletRequest req) {
        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && this.validationSessionCheck(authHeader)) {
            return;
        }

        if (req.getCookies() != null) {
            Cookie cookie = Arrays.stream(req.getCookies()).filter(ck -> ck.getName().equals("AuthToken")).findFirst().orElse(null);
            if (cookie != null && this.validationSessionCheck(cookie.getValue())) {
                return;
            }
        }

        if ( req.getParameter("token") != null){
            String token = req.getParameter("token");
            if(this.validationSessionCheck(token)){
                return;
            }
        }

        throw new ValidationLibException("UnAuthorization", HttpStatus.UNAUTHORIZED);
    }

    /**
     * Check auth validation session info.
     *
     * @param info the info
     * @param req  the req
     * @param res  the res
     * @return the validation session info
     */
    public ValidationSessionInfo checkAuth(ValidationLoginInfo info, HttpServletRequest req, HttpServletResponse res) {

        if (info.getToken() == null || info.getToken().isEmpty()) {
            throw new ValidationLibException("UnAuthorization", HttpStatus.UNAUTHORIZED);
        }
        if (this.validationConfig.getAuthToken() == null) {
            throw new ValidationLibException("Sever authkey is not helper ", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (validaitonSessionCheckAndRemake(info.getToken())) {
            return this.createSession(req, res);
        }

        if (!this.validationConfig.getAuthToken().equals(info.getToken())) {
            throw new ValidationLibException("UnAuthorization", HttpStatus.UNAUTHORIZED);
        }
        req.getSession();

        return this.createSession(req, res);
    }

    private String getSessionKey() {
        Random random = new Random(System.currentTimeMillis());
        StringBuffer key = new StringBuffer();
        for (int i = 0; i < SESSION_KEY_LENGTH; i++) {
            int idx = random.nextInt(SESSION_KEY_MAP.length());
            key.append(SESSION_KEY_MAP.substring(idx, idx + 1));
        }
        return key.toString();
    }

    /**
     * Create session validation session info.
     *
     * @param req the req
     * @param res the res
     * @return the validation session info
     */
    public ValidationSessionInfo createSession(HttpServletRequest req, HttpServletResponse res) {
        String key = this.getSessionKey();
        ValidationSessionInfo sessionInfo = new ValidationSessionInfo(req, key);
        this.sessionMap.put(key, sessionInfo);
        res.addCookie(new Cookie("AuthToken", key));
        return sessionInfo;
    }

    /**
     * Gets session.
     *
     * @param req the req
     * @return the session
     */
    public Object getSession(HttpServletRequest req) {
        if (req.getCookies() == null) {
            return null;
        }

        Cookie cookie = Arrays.stream(req.getCookies()).filter(c -> c.getName().equalsIgnoreCase("ValidationSession")).findFirst().orElse(null);

        if (cookie == null) {
            return null;
        }
        return this.sessionMap.get(cookie.getValue());
    }
}
