package hsim.checkpoint.core.component.validationRule.replace;

import hsim.checkpoint.core.component.validationRule.type.BaseValidationCheck;
import hsim.checkpoint.core.domain.ValidationData;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * The type Replace base 64.
 */
@Slf4j
@NoArgsConstructor
public class ReplaceBase64 implements BaseValidationCheck {


    @Override
    public boolean check(Object inputValue, Object standardValue) {
        return true;
    }

    @Override
    public Object replace(Object inputValue, Object standardValue, ValidationData param) {
        if (inputValue != null && inputValue instanceof String) {
            try {
                return new String(Base64.getDecoder().decode((String) inputValue), "UTF-8");
            } catch (UnsupportedEncodingException | IllegalArgumentException e) {
                log.info("base64 decode fail ( " + param.getName() + " ) :" + inputValue);
                return inputValue;
            }
        }
        return inputValue;
    }

    @Override
    public void exception(ValidationData param, Object inputValue, Object standardValue) {
    }

}
