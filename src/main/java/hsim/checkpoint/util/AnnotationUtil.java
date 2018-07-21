package hsim.checkpoint.util;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * The type Annotation util.
 */
public class AnnotationUtil {

    /**
     * Gets annotation.
     *
     * @param annotations     the annotations
     * @param annotationClass the annotation class
     * @return the annotation
     */
    public static Annotation getAnnotation(Annotation[] annotations, Class<?> annotationClass) {
        return Arrays.stream(annotations).filter(annotation -> annotation.annotationType().equals(annotationClass)).findFirst().orElse(null);
    }
}
