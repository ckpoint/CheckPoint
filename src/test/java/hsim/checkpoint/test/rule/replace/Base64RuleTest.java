package hsim.checkpoint.test.rule.replace;

import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.component.validationRule.type.BasicCheckRule;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.test.rule.RuleTestUtil;
import hsim.model.CommonReqModel;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Base64;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Base64RuleTest {

    private RuleTestUtil ruleTestUtil = new RuleTestUtil();
    private CommonReqModel obj = new CommonReqModel();
    private ValidationData data = ruleTestUtil.getDefaultValidationData();
    private BasicCheckRule checkType = BasicCheckRule.Base64;

    public Base64RuleTest() {
        this.data.setName("domain");
        this.data.setTypeClass(String.class);

        ValidationRule rule = data.getValidationRules().stream().filter(r -> r.getRuleName().equals(checkType.name())).findAny().get();
        rule.setUse(true);
    }

    @Test
    public void test_fail_1() {
        String plainText = "hsim@checkpoint.com";
        obj.setDomain(plainText);
        ruleTestUtil.getMsgChecker().checkDataInnerRules(this.data, this.obj);
        Assert.assertEquals(obj.getDomain(), plainText);
    }

    @Test
    public void test_fail_2() {
        String plainText = "hsim@checkpoint.com";
        String base64Text = Base64.getEncoder().encodeToString(plainText.getBytes()) +"##";
        obj.setDomain(base64Text);
        ruleTestUtil.getMsgChecker().checkDataInnerRules(this.data, this.obj);
        Assert.assertEquals(obj.getDomain(), base64Text);
    }

    @Test
    public void test_success_1() {
        String plainText = "hsim@checkpoint.com";
        String base64Text = Base64.getEncoder().encodeToString(plainText.getBytes()) ;
        obj.setDomain(base64Text);
        ruleTestUtil.getMsgChecker().checkDataInnerRules(this.data, this.obj);
        Assert.assertEquals(obj.getDomain(), plainText);
    }

}
