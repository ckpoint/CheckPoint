package hsim.checkpoint.config;

import hsim.checkpoint.core.annotation.ValidationResponse;
import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.domain.BasicCheckInfo;
import hsim.checkpoint.core.msg.MsgChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
@Slf4j
public class ValidationResponseFilter implements ResponseBodyAdvice<Object> {

    private MsgChecker msgChecker = ComponentMap.get(MsgChecker.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return ((methodParameter.getMethodAnnotation(ValidationResponse.class) != null)
                || (methodParameter.getParameterAnnotation(ValidationResponse.class) != null));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        log.info("method parameter key : " + methodParameter.getMethod().hashCode());

        this.msgChecker.checkMessage(new BasicCheckInfo(methodParameter), body);

        return body;
    }
}
