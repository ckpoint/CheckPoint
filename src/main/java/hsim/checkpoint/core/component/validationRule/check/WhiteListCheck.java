package hsim.checkpoint.core.component.validationRule.check;

import hsim.checkpoint.core.component.validationRule.type.BaseValidationCheck;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.exception.ValidationLibException;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * The type White list check.
 */
@NoArgsConstructor
public class WhiteListCheck implements BaseValidationCheck {


    @Override
    public boolean check(Object value, Object standardValue) {
        List<String> whiteList = (List<String>) standardValue;

        return whiteList.contains(String.valueOf(value).trim());
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
