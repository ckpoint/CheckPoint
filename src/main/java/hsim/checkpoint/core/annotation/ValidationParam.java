package hsim.checkpoint.core.annotation;

import java.lang.annotation.*;


/**
 * The interface Validation param.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidationParam {

    /**
     * Required boolean.
     *
     * @return the boolean
     */
    boolean required() default true;

    /**
     * Charset string.
     *
     * @return the string
     */
    String charset() default "UTF-8";
}