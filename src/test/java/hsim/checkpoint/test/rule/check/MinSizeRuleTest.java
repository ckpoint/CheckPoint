package hsim.checkpoint.test.rule.check;

import hsim.checkpoint.core.component.validationRule.callback.ValidationInvalidCallback;
import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.component.validationRule.type.BasicCheckRule;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.exception.ValidationLibException;
import hsim.checkpoint.helper.CheckPointHelper;
import hsim.checkpoint.model.product.ProductModel;
import hsim.checkpoint.test.rule.RuleTestUtil;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MinSizeRuleTest {

    private RuleTestUtil ruleTestUtil = new RuleTestUtil();
    private ProductModel obj = new ProductModel();
    private ValidationData data = ruleTestUtil.getDefaultValidationData();
    private BasicCheckRule checkType = BasicCheckRule.MinSize;

    public MinSizeRuleTest() {
        this.data.setName("discountPercent");

        ValidationRule rule = data.getValidationRules().stream().filter(r -> r.getRuleName().equals(checkType.name())).findAny().get();
        rule.setUse(true);
        rule.setStandardValue(0);
    }

    @Test
    public void test_fail_1() {
        obj.setDiscountPercent(-1.0);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getDiscountPercent(), false);
    }

    @Test
    public void test_fail_2() {
        obj.setDiscountPercent(-0.1);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getDiscountPercent(), false);
    }

    @Test
    public void test_success_1() {
        obj.setDiscountPercent(10.0);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getDiscountPercent(), true);
    }

    @Test
    public void test_success_2() {
        obj.setDiscountPercent(100.0);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getDiscountPercent(), true);
    }

    @Test
    public void test_callback_change() {
        CheckPointHelper helper = new CheckPointHelper();
        helper.replaceExceptionCallback(this.checkType, new MinSizeCallback());

        obj.setDiscountPercent(-100.0);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getDiscountPercent(), false, HttpStatus.NOT_ACCEPTABLE);
    }

    public static class MinSizeCallback implements ValidationInvalidCallback {
        @Override
        public void exception(ValidationData param, Object inputValue, Object standardValue) {
            throw new ValidationLibException(param.getName() + " order exception", HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
