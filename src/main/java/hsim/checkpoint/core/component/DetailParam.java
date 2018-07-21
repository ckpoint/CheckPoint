package hsim.checkpoint.core.component;

import hsim.checkpoint.core.annotation.ValidationUrlMapping;
import hsim.checkpoint.core.domain.ReqUrl;
import hsim.checkpoint.util.AnnotationUtil;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Detail param.
 */
@Getter
@ToString
public class DetailParam {

    private Class<?> parameterType;
    private Method parentMethod;
    private Class<?> parentClass;

    /**
     * Instantiates a new Detail param.
     *
     * @param paramType   the param class
     * @param method      the method
     * @param parentClass the parent class
     */
    public DetailParam(Class<?> paramType, Method method, Class<?> parentClass) {
        this.parameterType = paramType;
        this.parentMethod = method;
        this.parentClass = parentClass;
    }

    /**
     * Is url mapping boolean.
     *
     * @return the boolean
     */
    public boolean isUrlMapping() {
        if (parentMethod == null) {
            return false;
        }
        return this.parentMethod.getAnnotation(ValidationUrlMapping.class) != null;
    }

    private String getRequestMappingUrl(String[] value) {
        if (value == null) {
            return "";
        }

        String url = "";
        for (String s : value) {
            url += s;
        }
        return url;
    }

    private String getClassMappingUrl(Class<?> parentClass) {
        String url = "";

        RequestMapping requestMapping = (RequestMapping) AnnotationUtil.getAnnotation(parentClass.getAnnotations(), RequestMapping.class);
        if (requestMapping != null) {
            url += this.getRequestMappingUrl(requestMapping.value());
        }
        return url;
    }

    /**
     * Gets req urls.
     *
     * @return the req urls
     */
    public List<ReqUrl> getReqUrls() {

        String url = this.getClassMappingUrl(this.getParentClass());
        RequestAnnotation requestAnnotation = new RequestAnnotation(this.getParentMethod());

        url += this.getRequestMappingUrl(requestAnnotation.getValue());

        List<ReqUrl> list = new ArrayList<>();

        for (RequestMethod requestMethod : requestAnnotation.getMethod()) {
            list.add(new ReqUrl(requestMethod.name(), url));
        }
        return list;
    }

    /**
     * Gets method key.
     *
     * @return the method key
     */
    public String getMethodKey() {
        return String.valueOf(this.parentMethod.hashCode());
    }
}
