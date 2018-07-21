package hsim.checkpoint.test.helper;

import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.component.validationRule.type.BaseValidationCheck;
import hsim.checkpoint.core.component.validationRule.rule.AssistType;
import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.component.validationRule.type.BasicCheckRule;
import hsim.checkpoint.core.component.validationRule.type.StandardValueType;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.core.store.ValidationRuleStore;
import hsim.checkpoint.exception.ValidationLibException;
import hsim.checkpoint.helper.CheckPointHelper;
import hsim.checkpoint.test.rule.check.MandatoryRuleTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;

public class HelperTest {

    @Test
    public void test_addrule() {
        CheckPointHelper checkPointHelper = new CheckPointHelper();
        checkPointHelper.addValidationRule("ruleTest", StandardValueType.NUMBER, new TestRule(), new AssistType().string()).flush();
        checkPointHelper.replaceExceptionCallback(BasicCheckRule.Mandatory, new MandatoryRuleTest.MandatoryCallback());

        ValidationRuleStore ruleStore = ComponentMap.get(ValidationRuleStore.class);
        ValidationRule rule = ruleStore.getRules().stream().filter(r -> r.getRuleName().equals("ruleTest")).findAny().orElse(null);
        Assert.assertNotNull(rule);
    }

    static class TestRule implements BaseValidationCheck {


        @Override
        public boolean check(Object value, Object standardValue) {
            return true;
        }

        @Override
        public Object replace(Object value, Object standardValue, ValidationData param) {
            return null;
        }

        @Override
        public void exception(ValidationData param, Object inputValue, Object standardValue) {
            throw new ValidationLibException("testrule", HttpStatus.BAD_REQUEST);
        }

    }
}

