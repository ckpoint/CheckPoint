/**
 *
 */
package hsim.checkpoint.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Create by hsim on 2018. 1. 25.
 */
@Getter
public class ValidationLibException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private HttpStatus statusCode;

    /**
     * Instantiates a new Validation lib exception.
     *
     * @param statusCode the status code
     */
    public ValidationLibException(HttpStatus statusCode) {
        super();
        this.statusCode = statusCode;
    }

    /**
     * Instantiates a new Validation lib exception.
     *
     * @param message the message
     */
    public ValidationLibException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Validation lib exception.
     *
     * @param throwable the throwable
     */
    public ValidationLibException(Throwable throwable) {
        super(throwable);
    }

    /**
     * Instantiates a new Validation lib exception.
     *
     * @param message   the message
     * @param throwable the throwable
     */
    public ValidationLibException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Instantiates a new Validation lib exception.
     *
     * @param statusCode the status code
     * @param throwable  the throwable
     */
    public ValidationLibException(HttpStatus statusCode, Throwable throwable) {
        super(throwable);
        this.statusCode = statusCode;
    }

    /**
     * Instantiates a new Validation lib exception.
     *
     * @param message    the message
     * @param statusCode the status code
     */
    public ValidationLibException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Instantiates a new Validation lib exception.
     *
     * @param e the e
     */
    public ValidationLibException(HttpClientErrorException e) {
        super(e.getMessage());
        this.statusCode = e.getStatusCode();
    }

    /**
     * Instantiates a new Validation lib exception.
     *
     * @param message    the message
     * @param statusCode the status code
     * @param throwable  the throwable
     */
    public ValidationLibException(String message, HttpStatus statusCode, Throwable throwable) {
        super(message, throwable);
        this.statusCode = statusCode;
    }
}
