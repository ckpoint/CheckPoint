package hsim.checkpoint.util;

import hsim.checkpoint.core.component.DetailParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Annotation scanner.
 */
@Slf4j
public class AnnotationScanner {

    private List<Class> controllers = null;

    private boolean isControllerClass(Class type) {
        return type.getAnnotation(RestController.class) != null || type.getAnnotation(Controller.class) != null;
    }

    private Class getController(Object bean) {

        if (this.isControllerClass(bean.getClass())) {
            return bean.getClass();
        } else if (bean.getClass().getSuperclass() != null && !bean.getClass().getSuperclass().equals(Object.class) && this.isControllerClass(bean.getClass().getSuperclass())) {
            return bean.getClass().getSuperclass();
        }

        return null;
    }

    public void initBeans(Object[] beans) {
        this.controllers = Arrays.stream(beans).filter(bean -> this.getController(bean) != null).map(bean -> this.getController(bean)).collect(Collectors.toList());
    }

    /**
     * Gets parameter from method with annotation.
     *
     * @param parentClass     the parent class
     * @param method          the method
     * @param annotationClass the annotation class
     * @return the parameter from method with annotation
     */
    public List<DetailParam> getParameterFromMethodWithAnnotation(Class<?> parentClass, Method method, Class<?> annotationClass) {
        List<DetailParam> params = new ArrayList<>();
        if (method.getParameterCount() < 1) {
            return params;
        }

        for (Parameter param : method.getParameters()) {

            Annotation[] annotations = param.getAnnotations();

            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(annotationClass)) {
                    params.add(new DetailParam(param.getType(), method, parentClass));
                    break;
                }
            }
        }
        return params;
    }

    /**
     * Gets parameter from class with annotation.
     *
     * @param baseClass       the base class
     * @param annotationClass the annotation class
     * @return the parameter from class with annotation
     */
    public List<DetailParam> getParameterFromClassWithAnnotation(Class<?> baseClass, Class<?> annotationClass) {
        List<DetailParam> params = new ArrayList<>();
        Arrays.stream(baseClass.getDeclaredMethods()).forEach(method -> params.addAll(this.getParameterFromMethodWithAnnotation(baseClass, method, annotationClass)));
        return params;
    }

    /**
     * Gets parameter with annotation.
     *
     * @param annotation the annotation
     * @return the parameter with annotation
     */
    public List<DetailParam> getParameterWithAnnotation(Class<?> annotation) {
        List<DetailParam> params = new ArrayList<>();
        this.controllers.stream().forEach(cla -> params.addAll(this.getParameterFromClassWithAnnotation(cla, annotation)));
        return params;

    }
}
