package hsim.checkpoint.test.rule.replace;

import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.component.validationRule.type.BasicCheckRule;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.model.user.UserModel;
import hsim.checkpoint.test.rule.RuleTestUtil;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultValueRuleTest {

    private RuleTestUtil ruleTestUtil = new RuleTestUtil();
    private UserModel obj = new UserModel();
    private ValidationData data = ruleTestUtil.getDefaultValidationData();
    private BasicCheckRule checkType = BasicCheckRule.DefaultValue;

    public DefaultValueRuleTest() {
        this.data.setName("nickName");
        this.data.setTypeClass(String.class);

        ValidationRule rule = data.getValidationRules().stream().filter(r -> r.getRuleName().equals(checkType.name())).findAny().get();

        rule.setUse(true);
        rule.setStandardValue("guest");
    }

    @Test
    public void test_fail_1() {
        obj.setNickName("");
        ruleTestUtil.getMsgChecker().checkDataInnerRules(this.data, this.obj);
        Assert.assertEquals(obj.getNickName(), "guest");
    }

    @Test
    public void test_fail_2() {
        obj.setNickName(null);
        ruleTestUtil.getMsgChecker().checkDataInnerRules(this.data, this.obj);
        Assert.assertEquals(obj.getNickName(), "guest");
    }

    @Test
    public void test_success_1() {
        obj.setNickName("hsim");
        ruleTestUtil.getMsgChecker().checkDataInnerRules(this.data, this.obj);
        Assert.assertEquals(obj.getNickName(), "hsim");
    }

    @Test
    public void test_success_2() {
        obj.setNickName("taeon");
        ruleTestUtil.getMsgChecker().checkDataInnerRules(this.data, this.obj);
        Assert.assertEquals(obj.getNickName(), "taeon");
    }

}
