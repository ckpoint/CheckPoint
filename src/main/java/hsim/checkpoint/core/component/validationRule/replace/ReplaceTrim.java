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
    public Object replace(Object value, Object standardValue, ValidationData param) {
        if (value != null && value instanceof String) {
            String str = (String) value;
            return str.trim();
        }
        return null;
    }

    @Override
    public void exception(ValidationData param, Object inputValue, Object standardValue) {

    }

}
