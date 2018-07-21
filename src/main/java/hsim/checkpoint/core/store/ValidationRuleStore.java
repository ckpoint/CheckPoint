package hsim.checkpoint.core.store;

import hsim.checkpoint.core.component.validationRule.check.*;
import hsim.checkpoint.core.component.validationRule.replace.ReplaceBase64;
import hsim.checkpoint.core.component.validationRule.replace.ReplaceDefaultValue;
import hsim.checkpoint.core.component.validationRule.replace.ReplaceTrim;
import hsim.checkpoint.core.component.validationRule.rule.AssistType;
import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.component.validationRule.type.BaseValidationCheck;
import hsim.checkpoint.core.component.validationRule.type.BasicCheckRule;
import hsim.checkpoint.core.component.validationRule.type.StandardValueType;
import hsim.checkpoint.exception.ValidationLibException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Validation rule store.
 */
public class ValidationRuleStore {

    @Getter
    private List<ValidationRule> rules;
    private Map<String, BaseValidationCheck> checkHashMap;

    /**
     * Instantiates a new Validation rule store.
     */
    public ValidationRuleStore() {
        super();

        this.rules = new ArrayList<>();
        this.checkHashMap = new HashMap<>();

        this.addRule(new ValidationRule(BasicCheckRule.Mandatory, StandardValueType.NONE, new MandatoryCheck()).parentDependency().overlapBanRule(BasicCheckRule.DefaultValue).assistType(AssistType.all()));
        this.addRule(new ValidationRule(BasicCheckRule.BlackList, StandardValueType.LIST, new BlackListCheck()).overlapBanRule(BasicCheckRule.WhiteList).assistType(new AssistType().string().enumType()));
        this.addRule(new ValidationRule(BasicCheckRule.WhiteList, StandardValueType.LIST, new WhiteListCheck()).overlapBanRule(BasicCheckRule.BlackList).assistType(new AssistType().string().enumType()));

        this.addRule(new ValidationRule(BasicCheckRule.MinSize, StandardValueType.NUMBER, new MinSizeCheck()).assistType(new AssistType().string().list().number()));
        this.addRule(new ValidationRule(BasicCheckRule.MaxSize, StandardValueType.NUMBER, new MaxSizeCheck()).assistType(new AssistType().string().list().number()));

        this.addRule(new ValidationRule(BasicCheckRule.DefaultValue, StandardValueType.STRING, new ReplaceDefaultValue()).overlapBanRule(BasicCheckRule.Mandatory).assistType(new AssistType().number().string().enumType().nullable()));
        this.addRule(new ValidationRule(BasicCheckRule.Base64, StandardValueType.NONE, new ReplaceBase64()).assistType(new AssistType().string()));
        this.addRule(new ValidationRule(BasicCheckRule.Trim, StandardValueType.NONE, new ReplaceTrim()).assistType(new AssistType().string()));
        this.addRule(new ValidationRule(BasicCheckRule.Pattern, StandardValueType.STRING, new PatternCheck()).assistType(new AssistType().string()));
    }

    /**
     * Gets validation checker.
     *
     * @param rule the rule
     * @return the validation checker
     */
    public BaseValidationCheck getValidationChecker(ValidationRule rule) {
        ValidationRule existRule = this.rules.stream().filter(r -> r.getRuleName().equals(rule.getRuleName())).findFirst().orElse(null);
        if (existRule == null) {
            throw new ValidationLibException("rulename : " + rule.getRuleName() + "checker is notfound  ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return existRule.getValidationCheck();
    }

    /**
     * Add rule validation rule store.
     *
     * @param rules the rules
     * @return the validation rule store
     */
    public ValidationRuleStore addRule(ValidationRule... rules) {
        for (ValidationRule rule : rules) {
            this.addRule(rule);
        }
        return this;
    }

    /**
     * Add rule validation rule store.
     *
     * @param rule the rule
     * @return the validation rule store
     */
    public synchronized ValidationRuleStore addRule(ValidationRule rule) {
        rule.setOrderIdx(this.rules.size());
        this.rules.add(rule);
        this.checkHashMap.put(rule.getRuleName(), rule.getValidationCheck());
        return this;
    }

    /**
     * Gets checker.
     *
     * @param rule the rule
     * @return the checker
     */
    public BaseValidationCheck getChecker(ValidationRule rule) {
        return this.checkHashMap.get(rule.getRuleName());
    }


}
