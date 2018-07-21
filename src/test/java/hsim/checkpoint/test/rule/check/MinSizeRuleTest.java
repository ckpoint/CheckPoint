package hsim.checkpoint.test.rule.check;

import hsim.checkpoint.core.component.validationRule.callback.ValidationInvalidCallback;
import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.component.validationRule.type.BasicCheckRule;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.exception.ValidationLibException;
import hsim.checkpoint.helper.CheckPointHelper;
import hsim.checkpoint.test.rule.RuleTestUtil;
import hsim.model.CommonReqModel;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MinSizeRuleTest {

    private RuleTestUtil ruleTestUtil = new RuleTestUtil();
    private CommonReqModel obj = new CommonReqModel();
    private ValidationData data = ruleTestUtil.getDefaultValidationData();
    private BasicCheckRule checkType = BasicCheckRule.MinSize;

    public MinSizeRuleTest(){
        this.data.setName("size");

        ValidationRule rule = data.getValidationRules().stream().filter(r -> r.getRuleName().equals(checkType.name())).findAny().get();
        rule.setUse(true);
        rule.setStandardValue(9.9);
    }

    @Test
    public void test_fail_1() {
        obj.setSize(0);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getSize(), false);
    }

    @Test
    public void test_fail_2() {
        obj.setSize(9.8);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getSize(), false);
    }

    @Test
    public void test_success_1() {
        obj.setSize(10.0);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getSize(), true);
    }

    @Test
    public void test_success_2() {
        obj.setSize(100);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getSize(), true);
    }

    @Test
    public void test_callback_change() {
        CheckPointHelper helper = new CheckPointHelper();
        helper.replaceExceptionCallback(this.checkType, new MinSizeCallback() );

        obj.setSize(-100);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getSize(), false, HttpStatus.NOT_ACCEPTABLE);
    }

    public static class MinSizeCallback implements ValidationInvalidCallback {
        @Override
        public void exception(ValidationData param, Object inputValue, Object standardValue) {
            throw  new ValidationLibException(param.getName() +" test exception", HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
