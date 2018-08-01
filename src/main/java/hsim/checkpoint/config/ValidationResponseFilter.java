package hsim.checkpoint.config;

import hsim.checkpoint.core.annotation.ValidationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.*;

@ControllerAdvice
@Slf4j
public class ValidationResponseFilter implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return  ((methodParameter.getMethodAnnotation(ValidationResponse.class) != null)
                || (methodParameter.getParameterAnnotation(ValidationResponse.class) != null));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        Map<String, Object> bodyMap = new HashMap<>();
        List<Object> bodyList = new ArrayList<>();
        for ( int i =0 ;i < 2 ;i ++){
            bodyList.add(body);
        }

        bodyMap.put("id", 1);
        bodyMap.put("createdAt", new Date());
        bodyMap.put("body", body);
        bodyMap.put("list", bodyList);
        return bodyMap;
    }
}
