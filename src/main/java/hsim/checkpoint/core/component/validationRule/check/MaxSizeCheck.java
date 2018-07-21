package hsim.checkpoint.core.component.validationRule.check;

import hsim.checkpoint.core.component.validationRule.type.BaseValidationCheck;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.exception.ValidationLibException;
import hsim.checkpoint.util.ValidationObjUtil;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * The type Max size check.
 */
@NoArgsConstructor
public class MaxSizeCheck implements BaseValidationCheck {


    @Override
    public boolean check(Object value, Object standardValue) {

        Double v = ValidationObjUtil.getObjectSize(value);
        if (v == null) {
            return true;
        }

        Double doubleSValue = Double.valueOf(String.valueOf(standardValue));

        if (doubleSValue < v) {
            return false;
        }

        return true;
    }

    @Override
    public Object replace(Object value, Object standardValue, ValidationData param) {
        return null;
    }

    @Override
    public void exception(ValidationData param, Object inputValue, Object standardValue) {
        throw new ValidationLibException("Parameter : " + param.getName() + " value is too big(maximum:" + standardValue + ")", HttpStatus.BAD_REQUEST);
    }

}
