package hsim.checkpoint.rule;

import hsim.checkpoint.core.component.validationRule.type.BaseValidationCheck;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.exception.ValidationLibException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class LoggingRule implements BaseValidationCheck {

    @Override
    public boolean check(Object value, Object standardValue) {
        log.info("input : " + value + ", standard : " + standardValue);
        return true;
    }

    @Override
    public Object replace(Object value, Object standardValue, ValidationData param) {
        return null;
    }

    @Override
    public void exception(ValidationData param, Object inputValue, Object standardValue) {
        throw new ValidationLibException("logging", HttpStatus.BAD_REQUEST);
    }

}