package hsim.checkpoint.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import hsim.checkpoint.config.ValidationConfig;
import hsim.checkpoint.core.annotation.ValidationBody;
import hsim.checkpoint.core.annotation.ValidationParam;
import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.domain.BasicCheckInfo;
import hsim.checkpoint.core.msg.MsgChecker;
import hsim.checkpoint.core.msg.MsgSaver;
import hsim.checkpoint.type.ParamType;
import hsim.checkpoint.util.ParameterMapper;
import hsim.checkpoint.util.ValidationObjUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * The type Validation resolver.
 */
@Slf4j
public class ValidationResolver implements HandlerMethodArgumentResolver {

    private MsgSaver msgSaver = ComponentMap.get(MsgSaver.class);
    private ObjectMapper objectMapper;

    @Getter
    private MsgChecker msgChecker = ComponentMap.get(MsgChecker.class);

    private ValidationConfig validationConfig = ComponentMap.get(ValidationConfig.class);

    /**
     * Instantiates a new Validation resolver.
     */
    public ValidationResolver() {
        super();
        this.objectMapper = ValidationObjUtil.getDefaultObjectMapper();
    }

    /**
     * Replace object mapper.
     *
     * @param objectMapper the object mapper
     */
    public void replaceObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(ValidationBody.class) != null
                || parameter.getParameterAnnotation(ValidationParam.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Object reqBody = null;
        BasicCheckInfo basicCheckInfo = new BasicCheckInfo((HttpServletRequest) webRequest.getNativeRequest(), parameter, this.validationConfig.isBodyLogging());

        this.msgSaver.urlCheckAndSave(basicCheckInfo, basicCheckInfo.getParamType(), basicCheckInfo.getReqUrl(), parameter.getParameterType());

        if (basicCheckInfo.getParamType().equals(ParamType.BODY) && basicCheckInfo.isListBody()) {
            ParameterizedType paramType = (ParameterizedType) parameter.getGenericParameterType();
            Class<?> paramClass = (Class<?>) paramType.getActualTypeArguments()[0];
            return this.objectMapper.readValue(basicCheckInfo.getBody(), this.objectMapper.getTypeFactory().constructCollectionType(List.class, paramClass));
            //TOO
        } else if (basicCheckInfo.getParamType().equals(ParamType.BODY)) {
            reqBody = this.objectMapper.readValue(basicCheckInfo.getBody(), parameter.getParameterType());
        } else if (basicCheckInfo.getParamType().equals(ParamType.QUERY_PARAM)) {
            reqBody = ParameterMapper.requestParamaterToObject(basicCheckInfo.getReq(), parameter.getParameterType(), parameter.getParameterAnnotation(ValidationParam.class).charset());
        }

        this.msgChecker.checkRequest(basicCheckInfo, reqBody);

        return reqBody;
    }
}