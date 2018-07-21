package hsim.checkpoint.core.component.validationRule.check;

import hsim.checkpoint.core.component.validationRule.type.BaseValidationCheck;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.exception.ValidationLibException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * The type Black list check.
 */
@NoArgsConstructor
public class BlackListCheck implements BaseValidationCheck {

    @Override
    public boolean check(Object value, Object standardValue) {
        if (value == null) {
            return true;
        }
        List<String> blackList = (List<String>) standardValue;
        return !blackList.contains(String.valueOf(value).trim());
    }

    @Override
    public Object replace(Object value, Object standardValue, ValidationData param) {
        return null;
    }

    @Override
    public void exception(ValidationData param, Object inputValue, Object standardValue) {
        throw new ValidationLibException(inputValue + " is bad", HttpStatus.NOT_ACCEPTABLE);
    }
}
