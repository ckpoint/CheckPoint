package hsim.checkpoint.core.component.validationRule.replace;

import hsim.checkpoint.core.component.validationRule.type.BaseValidationCheck;
import hsim.checkpoint.core.domain.ValidationData;
import lombok.NoArgsConstructor;

/**
 * The type Replace trim.
 */
@NoArgsConstructor
public class ReplaceTrim implements BaseValidationCheck {


    @Override
    public boolean check(Object inputValue, Object standardValue) {
        return true;
    }

    @Override
    public Object replace(Object inputValue, Object standardValue, ValidationData param) {
        if (inputValue != null && inputValue instanceof String) {
            String str = (String) inputValue;
            return str.trim();
        }
        return inputValue;
    }

    @Override
    public void exception(ValidationData param, Object inputValue, Object standardValue) {

    }

}
