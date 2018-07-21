package hsim.checkpoint.core.annotation;

import java.lang.annotation.*;


/**
 * The interface Validation body.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidationBody {

    /**
     * Required boolean.
     *
     * @return the boolean
     */
    boolean required() default true;
}