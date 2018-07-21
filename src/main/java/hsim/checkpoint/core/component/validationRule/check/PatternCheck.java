package hsim.checkpoint.core.component.validationRule.check;

import hsim.checkpoint.core.component.validationRule.type.BaseValidationCheck;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.exception.ValidationLibException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Pattern check.
 */
@NoArgsConstructor
public class PatternCheck implements BaseValidationCheck {


    @Override
    public boolean check(Object value, Object standradValue) {

        if (value instanceof String) {
            Pattern pattern = Pattern.compile(String.valueOf(standradValue));
            Matcher matcher = pattern.matcher(String.valueOf(value));
            if (!matcher.matches()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object replace(Object value, Object standardValue, ValidationData param) {
        return null;
    }

    @Override
    public void exception(ValidationData param, Object inputValue, Object standardValue) {
        throw new ValidationLibException("invalid pattern value : " + param.getName() + " - " + inputValue, HttpStatus.BAD_REQUEST);
    }

}
