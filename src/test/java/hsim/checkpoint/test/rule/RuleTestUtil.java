package hsim.checkpoint.test.rule;

import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.component.validationRule.type.BasicCheckRule;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.core.msg.MsgChecker;
import hsim.checkpoint.core.store.ValidationRuleStore;
import hsim.checkpoint.exception.ValidationLibException;
import lombok.Getter;
import org.junit.Assert;
import org.springframework.http.HttpStatus;

public class RuleTestUtil {

    @Getter
    private MsgChecker msgChecker = ComponentMap.get(MsgChecker.class);

    public ValidationData getDefaultValidationData() {
        ValidationData data = new ValidationData();
        data.setValidationRules(ComponentMap.get(ValidationRuleStore.class).getRules());
        data.getValidationRules().stream().forEach(r -> r.setUse(false));
        return data;
    }

    public void checkRule(ValidationData data, Object obj, BasicCheckRule checkRule, Object inputValue, boolean success) {
       this.checkRule(data, obj, checkRule, inputValue, success, null);
    }

    public void checkRule(ValidationData data, Object obj, BasicCheckRule checkRule, Object inputValue, boolean success, HttpStatus failStatus) {
        try {
            msgChecker.checkDataInnerRules(data, obj);
        } catch (ValidationLibException e) {
            if (success) {
                Assert.fail(checkRule.name() + " FAIL -  " + e.getMessage());
            }
            if(failStatus != null){
                Assert.assertEquals(e.getStatusCode(), failStatus);
            }
            return;
        }
        if (!success) {
            ValidationRule rule = data.getValidationRules().stream().filter(r -> r.getRuleName().equals(checkRule.name())).findAny().get();
            Assert.fail(rule.getRuleName() + " is passed -  standardValue : " + rule.getStandardValue() + " input value : " + inputValue);
        }
    }
}
