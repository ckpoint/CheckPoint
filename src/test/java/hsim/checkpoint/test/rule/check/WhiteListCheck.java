package hsim.checkpoint.test.rule.check;

import hsim.checkpoint.core.component.validationRule.callback.ValidationInvalidCallback;
import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.component.validationRule.type.BasicCheckRule;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.exception.ValidationLibException;
import hsim.checkpoint.helper.CheckPointHelper;
import hsim.checkpoint.test.rule.RuleTestUtil;
import hsim.model.CommonReqModel;
import org.apache.poi.util.ArrayUtil;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.internal.util.collections.ListUtil;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.stream.Collectors;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WhiteListCheck {

    private RuleTestUtil ruleTestUtil = new RuleTestUtil();
    private CommonReqModel obj = new CommonReqModel();
    private ValidationData data = ruleTestUtil.getDefaultValidationData();
    private BasicCheckRule checkType = BasicCheckRule.WhiteList;

    public WhiteListCheck() {
        this.data.setName("name");

        ValidationRule rule = data.getValidationRules().stream().filter(r -> r.getRuleName().equals(checkType.name())).findAny().get();
        rule.setUse(true);
        rule.setStandardValue(Arrays.asList(new String[]{"hsim", "taeon", "ayoung", "miho"}));
    }

    @Test
    public void test_fail_1() {
        obj.setName("hsim.");
        ruleTestUtil.checkRule(data, obj, checkType, obj.getName(), false);
    }

    @Test
    public void test_fail_2() {
        obj.setName(".taeon");
        ruleTestUtil.checkRule(data, obj, checkType, obj.getName(), false);
    }

    @Test
    public void test_success_1() {
        obj.setName("hsim");
        ruleTestUtil.checkRule(data, obj, checkType, obj.getName(), true);
    }

    @Test
    public void test_success_2() {
        obj.setName("ayoung");
        ruleTestUtil.checkRule(data, obj, checkType, obj.getName(), true);
    }

    @Test
    public void test_callback_change() {
        CheckPointHelper helper = new CheckPointHelper();
        helper.replaceExceptionCallback(this.checkType, new WhiteListCallback());

        obj.setName(".ayoung");
        ruleTestUtil.checkRule(data, obj, checkType, obj.getName(), false, HttpStatus.NOT_ACCEPTABLE);
    }

    public static class WhiteListCallback implements ValidationInvalidCallback {
        @Override
        public void exception(ValidationData param, Object inputValue, Object standardValue) {
            throw new ValidationLibException(param.getName() + " test exception", HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
