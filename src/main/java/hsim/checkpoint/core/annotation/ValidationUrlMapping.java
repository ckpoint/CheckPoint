package hsim.checkpoint.core.annotation;

import java.lang.annotation.*;


/**
 * The interface Validation url mapping.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidationUrlMapping {

    /**
     * Required boolean.
     *
     * @return the boolean
     */
    boolean required() default true;
}