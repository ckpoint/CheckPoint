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
public class PatternRuleTest {

    private RuleTestUtil ruleTestUtil = new RuleTestUtil();
    private CommonReqModel obj = new CommonReqModel();
    private ValidationData data = ruleTestUtil.getDefaultValidationData();
    private BasicCheckRule checkType = BasicCheckRule.Pattern;

    public PatternRuleTest(){
        this.data.setName("email");

        ValidationRule rule = data.getValidationRules().stream().filter(r -> r.getRuleName().equals(checkType.name())).findAny().get();
        rule.setUse(true);
        rule.setStandardValue("(\\w+\\.)*\\w+@(\\w+\\.)+[A-Za-z]+");
    }

    @Test
    public void test_fail_1() {
        obj.setEmail("hsim@checkpoint.com.");
        ruleTestUtil.checkRule(data, obj, checkType, obj.getSize(), false);
    }

    @Test
    public void test_fail_2() {
        obj.setEmail("taeon@checkpoint.");
        ruleTestUtil.checkRule(data, obj, checkType, obj.getSize(), false);
    }

    @Test
    public void test_success_1() {
        obj.setEmail("hsim@checkpoint.com");
        ruleTestUtil.checkRule(data, obj, checkType, obj.getSize(), true);
    }

    @Test
    public void test_success_2() {
        obj.setEmail("taeon@checkpoint.com");
        ruleTestUtil.checkRule(data, obj, checkType, obj.getSize(), true);
    }

    @Test
    public void test_callback_change() {
        CheckPointHelper helper = new CheckPointHelper();
        helper.replaceExceptionCallback(this.checkType, new PatternCallback());

        obj.setEmail("taeon");
        ruleTestUtil.checkRule(data, obj, checkType, obj.getSize(), false, HttpStatus.NOT_ACCEPTABLE);
    }

    public static class PatternCallback implements ValidationInvalidCallback {
        @Override
        public void exception(ValidationData param, Object inputValue, Object standardValue) {
            throw  new ValidationLibException(param.getName() +" test exception", HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
