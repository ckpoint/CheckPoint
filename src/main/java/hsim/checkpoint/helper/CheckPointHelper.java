package hsim.checkpoint.helper;


import com.fasterxml.jackson.databind.ObjectMapper;
import hsim.checkpoint.config.ValidationConfig;
import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.component.validationRule.callback.ValidationInvalidCallback;
import hsim.checkpoint.core.component.validationRule.rule.AssistType;
import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.component.validationRule.type.BaseValidationCheck;
import hsim.checkpoint.core.component.validationRule.type.BasicCheckRule;
import hsim.checkpoint.core.component.validationRule.type.StandardValueType;
import hsim.checkpoint.core.msg.MsgChecker;
import hsim.checkpoint.core.repository.ValidationDataRepository;
import hsim.checkpoint.core.store.ValidationRuleStore;
import hsim.checkpoint.core.store.ValidationStore;
import hsim.checkpoint.interceptor.ValidationResolver;

/**
 * The type Check point helper.
 */
public class CheckPointHelper {

    private ValidationResolver validationResolver = ComponentMap.get(ValidationResolver.class);
    private ValidationRuleStore validationRuleStore = ComponentMap.get(ValidationRuleStore.class);
    private ValidationDataRepository validationDataRepository = ComponentMap.get(ValidationDataRepository.class);
    private ValidationStore validationStore = ComponentMap.get(ValidationStore.class);
    private ValidationConfig validationConfig = ComponentMap.get(ValidationConfig.class);
    private MsgChecker msgChecker = ComponentMap.get(MsgChecker.class);

    /**
     * Replace the mapper to be used for message parsing.
     *
     * @param objectMapper jackson objectmapper
     * @return CheckPointHelper check point helper
     */
    public CheckPointHelper replaceObjectMapper(ObjectMapper objectMapper) {
        this.validationResolver.replaceObjectMapper(objectMapper);
        return this;
    }

    /**
     * Replace the callback to be used basic exception.
     *
     * @param checkRule basic rule type ex,, BasicCheckRule.Mandatory
     * @param cb        callback class with implement ValidationInvalidCallback
     * @return CheckPointHeler check point helper
     */
    public CheckPointHelper replaceExceptionCallback(BasicCheckRule checkRule, ValidationInvalidCallback cb) {
        this.msgChecker.replaceCallback(checkRule, cb);
        return this;
    }

    /**
     * Add the fresh user rule
     *
     * @param ruleName          use rule name - must uniqueue
     * @param standardValueType rule check standardvalue type
     * @param validationCheck   rule check class with extends BaseValidationCheck and overide replace or check method and exception method
     * @param assistType        input field type
     * @return CheckPointHelper check point helper
     */
    public CheckPointHelper addValidationRule(String ruleName, StandardValueType standardValueType, BaseValidationCheck validationCheck, AssistType assistType) {
        ValidationRule rule = new ValidationRule(ruleName, standardValueType, validationCheck);
        if (assistType == null) {
            assistType = AssistType.all();
        }
        rule.setAssistType(assistType);
        this.validationRuleStore.addRule(rule);
        return this;
    }

    /**
     * Flush check point helper.
     *
     * @return the check point helper
     */
    public CheckPointHelper flush() {
        this.validationDataRepository.datasRuleSync();
        this.validationDataRepository.flush();
        this.validationStore.refresh();
        return this;
    }

    /**
     * Gets config.
     *
     * @return the config
     */
    public ValidationConfig getConfig() {
        return this.validationConfig;
    }

}
