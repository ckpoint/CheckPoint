package hsim.checkpoint.exception.resolver;

import hsim.checkpoint.exception.ValidationLibException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type Validation exception resolver.
 */
@Slf4j
public class ValidationExceptionResolver extends AbstractHandlerExceptionResolver {

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex instanceof ValidationLibException) {
            return this.sendErrorCode(response, (ValidationLibException) ex);
        }
        return null;
    }

    private ModelAndView sendErrorCode(HttpServletResponse res, ValidationLibException ex) {

        try {
            log.info("ex message : " + ex.getMessage());
            res.setHeader("ERR_MSG", ex.getMessage());
            res.sendError(ex.getStatusCode().value(), ex.getMessage());
        } catch (IOException e) {
            log.error("response error send io exception ");
        }

        return new ModelAndView();
    }
}