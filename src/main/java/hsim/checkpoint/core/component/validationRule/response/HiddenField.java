package hsim.checkpoint.core.component.validationRule.response;

import hsim.checkpoint.core.component.validationRule.type.BaseValidationCheck;
import hsim.checkpoint.core.domain.ValidationData;
import lombok.NoArgsConstructor;

import java.util.Random;

/**
 * The type Replace trim.
 */
@NoArgsConstructor
public class HiddenField implements BaseValidationCheck {

    @Override
    public boolean check(Object inputValue, Object standardValue) {
        return true;
    }

    @Override
    public Object replace(Object value, Object standardValue, ValidationData param) {
        if(param.getTypeClass().isPrimitive() && param.getTypeClass().equals(boolean.class)){
            return false;
        }
        else if ( param.getTypeClass().isPrimitive()) {
            return new Random(System.currentTimeMillis() + param.hashCode()).nextInt(); }
        else {
            return null;
        }
    }

    @Override
    public void exception(ValidationData param, Object inputValue, Object standardValue) {

    }

}
