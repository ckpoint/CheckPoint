package hsim.checkpoint.test.rule.check;

import hsim.checkpoint.core.component.validationRule.callback.ValidationInvalidCallback;
import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.component.validationRule.type.BasicCheckRule;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.exception.ValidationLibException;
import hsim.checkpoint.helper.CheckPointHelper;
import hsim.checkpoint.model.user.UserModel;
import hsim.checkpoint.model.user.type.Membership;
import hsim.checkpoint.test.rule.RuleTestUtil;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WhiteListCheck {

    private RuleTestUtil ruleTestUtil = new RuleTestUtil();
    private UserModel obj = new UserModel();
    private ValidationData data = ruleTestUtil.getDefaultValidationData();
    private BasicCheckRule checkType = BasicCheckRule.WhiteList;

    public WhiteListCheck() {
        this.data.setName("membership");

        ValidationRule rule = data.getValidationRules().stream().filter(r -> r.getRuleName().equals(checkType.name())).findAny().get();
        rule.setUse(true);
        rule.setStandardValue(Arrays.asList(new String[]{"GOLD", "SILVER", "BRONZE"}));
    }

    @Test
    public void test_fail_1() {
        obj.setMembership(Membership.BLACK);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getMembership(), false);
    }

    @Test
    public void test_fail_2() {
        obj.setMembership(Membership.DORMANCY);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getMembership(), false);
    }

    @Test
    public void test_success_1() {
        obj.setMembership(Membership.GOLD);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getMembership(), true);
    }

    @Test
    public void test_success_2() {
        obj.setMembership(Membership.SILVER);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getMembership(), true);
    }

    @Test
    public void test_success_3() {
        obj.setMembership(Membership.BRONZE);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getMembership(), true);
    }

    @Test
    public void test_callback_change() {
        CheckPointHelper helper = new CheckPointHelper();
        helper.replaceExceptionCallback(this.checkType, new WhiteListCallback());

        obj.setMembership(Membership.BLACK);
        ruleTestUtil.checkRule(data, obj, checkType, obj.getMembership(), false, HttpStatus.NOT_ACCEPTABLE);
    }

    public static class WhiteListCallback implements ValidationInvalidCallback {
        @Override
        public void exception(ValidationData param, Object inputValue, Object standardValue) {
            throw new ValidationLibException(param.getName() + " test exception", HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
