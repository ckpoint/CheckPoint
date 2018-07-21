package hsim.checkpoint.core.msg;

import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.component.validationRule.callback.ValidationInvalidCallback;
import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.component.validationRule.type.BaseValidationCheck;
import hsim.checkpoint.core.component.validationRule.type.BasicCheckRule;
import hsim.checkpoint.core.domain.BasicCheckInfo;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.core.store.ValidationRuleStore;
import hsim.checkpoint.core.store.ValidationStore;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Msg checker.
 */
@Slf4j
public class MsgChecker {

    private ValidationStore validationStore = ComponentMap.get(ValidationStore.class);
    private ValidationRuleStore validationRuleStore = ComponentMap.get(ValidationRuleStore.class);

    private Map<String, ValidationInvalidCallback> callbackMap = new HashMap<>();

    /**
     * Replace callback msg checker.
     *
     * @param type the type
     * @param cb   the cb
     * @return the msg checker
     */
    public MsgChecker replaceCallback(BasicCheckRule type, ValidationInvalidCallback cb) {
        this.callbackMap.put(type.name(), cb);
        return this;
    }


    private void callExcpetion(ValidationData param, ValidationRule rule, Object value, Object standardValue) {
        ValidationInvalidCallback cb = this.callbackMap.get(rule.getRuleName());
        if (cb != null) {
            cb.exception(param, value, standardValue);
        } else {
            this.validationRuleStore.getValidationChecker(rule).exception(param, value, standardValue);
        }
    }

    /**
     * Check point.
     *
     * @param param         the param
     * @param rule          the rule
     * @param bodyObj       the body obj
     * @param standardValue the standard value
     */
    public void checkPoint(ValidationData param, ValidationRule rule, Object bodyObj, Object standardValue) {
        //check
        Object value = param.getValue(bodyObj);
        BaseValidationCheck checker = this.validationRuleStore.getValidationChecker(rule);
        if ((value != null && standardValue != null) || rule.getAssistType().isNullable()) {
            boolean valid = checker.check(value, standardValue);

            if (!valid) {
                this.callExcpetion(param, rule, value, standardValue);
            }

        }
        //replace value
        Object replaceValue = checker.replace(value, standardValue, param);
        if (replaceValue != null && replaceValue != value) {
            param.replaceValue(bodyObj, replaceValue);
        }
    }

    /**
     * Check data inner rules.
     *
     * @param data    the data
     * @param bodyObj the body obj
     */
    public void checkDataInnerRules(ValidationData data, Object bodyObj) {
        data.getValidationRules().stream().filter(vr -> vr.isUse()).forEach(rule -> {
            this.checkPoint(data, rule, bodyObj, rule.getStandardValue());
        });
    }

    /**
     * Check request.
     *
     * @param basicCheckInfo the basic check info
     * @param bodyObj        the body obj
     */
    public void checkRequest(BasicCheckInfo basicCheckInfo, Object bodyObj) {
        String key = basicCheckInfo.getUniqueKey();

        List<ValidationData> checkData = this.validationStore.getValidationDatas(basicCheckInfo.getParamType(), key);

        if (checkData == null || checkData.isEmpty()) {
            return;
        }
        checkData.stream().forEach(data -> {
            this.checkDataInnerRules(data, bodyObj);
        });
    }
}
