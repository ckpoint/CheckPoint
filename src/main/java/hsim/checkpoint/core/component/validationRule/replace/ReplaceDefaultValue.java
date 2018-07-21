package hsim.checkpoint.core.component.validationRule.replace;

import hsim.checkpoint.core.component.validationRule.type.BaseValidationCheck;
import hsim.checkpoint.core.domain.ValidationData;
import lombok.NoArgsConstructor;

/**
 * The type Replace default value.
 */
@NoArgsConstructor
public class ReplaceDefaultValue implements BaseValidationCheck {


    private boolean isEmptyString(Object value) {
        return (value instanceof String && ((String) value).isEmpty());
    }

    @Override
    public boolean check(Object inputValue, Object standardValue) {
        return true;
    }

    @Override
    public Object replace(Object value, Object standardValue, ValidationData param) {
        if (value != null && !this.isEmptyString(value)) {
            return null;
        }

        return standardValue;
    }

    @Override
    public void exception(ValidationData param, Object inputValue, Object standardValue) {

    }

}
