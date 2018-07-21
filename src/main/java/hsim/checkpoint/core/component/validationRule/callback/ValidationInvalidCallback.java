package hsim.checkpoint.core.component.validationRule.callback;

import hsim.checkpoint.core.domain.ValidationData;

/**
 * The interface Validation invalid callback.
 */
public interface ValidationInvalidCallback {

    /**
     * Exception.
     *
     * @param param         the param
     * @param inputValue    the input value
     * @param standardValue the standard value
     */
    void exception(ValidationData param, Object inputValue, Object standardValue);
}
