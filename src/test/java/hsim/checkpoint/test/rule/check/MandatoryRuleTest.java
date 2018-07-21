package hsim.checkpoint.test.rule.check;

import hsim.checkpoint.core.component.validationRule.callback.ValidationInvalidCallback;
import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.component.validationRule.type.BasicCheckRule;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.exception.ValidationLibException;
import hsim.checkpoint.helper.CheckPointHelper;
import hsim.checkpoint.test.rule.RuleTestUtil;
import hsim.model.CommonReqModel;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MandatoryRuleTest {

    private RuleTestUtil ruleTestUtil = new RuleTestUtil();
    private CommonReqModel obj = new CommonReqModel();
    private ValidationData data = ruleTestUtil.getDefaultValidationData();
    private BasicCheckRule checkType = BasicCheckRule.Mandatory;

    public MandatoryRuleTest() {
        this.data.setName("loginId");

        ValidationRule rule = data.getValidationRules().stream().filter(r -> r.getRuleName().equals(checkType.name())).findAny().get();
        rule.setUse(true);
    }

    @Test
    public void test_fail_1() {
        obj.setLoginId(null);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getLoginId(), false);
    }

    @Test
    public void test_fail_2() {
        obj.setLoginId("");
        ruleTestUtil.checkRule(data, obj, checkType, obj.getLoginId(), false);
    }

    @Test
    public void test_success_1() {
        obj.setLoginId("hsim");
        ruleTestUtil.checkRule(data, obj, checkType, obj.getLoginId(), true);
    }

    @Test
    public void test_success_2() {
        obj.setLoginId("lhs1553");
        ruleTestUtil.checkRule(data, obj, checkType, obj.getLoginId(), true);
    }

    @Test
    public void test_callback_change() {
        CheckPointHelper helper = new CheckPointHelper();
        helper.replaceExceptionCallback(this.checkType, new MandatoryCallback());

        obj.setLoginId(null);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getSize(), false, HttpStatus.NO_CONTENT);
    }

    public static class MandatoryCallback implements ValidationInvalidCallback {
        @Override
        public void exception(ValidationData param, Object inputValue, Object standardValue) {
            throw new ValidationLibException(param.getName() + " test exception", HttpStatus.NO_CONTENT);
        }
    }
}
