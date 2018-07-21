package hsim.checkpoint.core.component.validationRule.type;

import hsim.checkpoint.core.domain.ValidationData;

/**
 * The type Base validation check.
 */
public interface BaseValidationCheck {

    /**
     * Check boolean.
     *
     * @param inputValue    the input value
     * @param standardValue the standard value
     * @return the boolean
     */
    boolean check(Object inputValue, Object standardValue);

    /**
     * Replace object.
     *
     * @param value         the value
     * @param standardValue the standard value
     * @param param         the param
     * @return the object
     */
    Object replace(Object value, Object standardValue, ValidationData param);

    /**
     * Exception.
     *
     * @param param         the param
     * @param inputValue    the input value
     * @param standardValue the standard value
     */
    void exception(ValidationData param, Object inputValue, Object standardValue);
}
