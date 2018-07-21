package hsim.checkpoint.core.component.validationRule.rule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hsim.checkpoint.core.component.validationRule.type.BaseValidationCheck;
import hsim.checkpoint.core.component.validationRule.type.BasicCheckRule;
import hsim.checkpoint.core.component.validationRule.type.StandardValueType;
import hsim.checkpoint.core.domain.ValidationData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Validation rule.
 */
@Getter
@Setter
@NoArgsConstructor
public class ValidationRule {
    private int orderIdx;
    private String ruleName;
    private boolean use;
    private boolean parentDependency;
    private String overlapBanRuleName;
    private Object standardValue;
    private StandardValueType standardValueType;

    @JsonIgnore
    private BaseValidationCheck validationCheck;

    private AssistType assistType;

    /**
     * Instantiates a new Validation rule.
     *
     * @param rule              the rule
     * @param standardValueType the standard value type
     * @param checker           the checker
     */
    public ValidationRule(BasicCheckRule rule, StandardValueType standardValueType, BaseValidationCheck checker) {
        this.ruleName = rule.name();
        this.standardValueType = standardValueType;
        this.validationCheck = checker;
    }

    /**
     * Instantiates a new Validation rule.
     *
     * @param rName             the r name
     * @param standardValueType the standard value type
     * @param checker           the checker
     */
    public ValidationRule(String rName, StandardValueType standardValueType, BaseValidationCheck checker) {
        this.ruleName = rName;
        this.standardValueType = standardValueType;
        this.validationCheck = checker;
    }

    /**
     * Update rule basic info.
     *
     * @param rule the rule
     */
    public void updateRuleBasicInfo(ValidationRule rule) {
        this.orderIdx = rule.orderIdx;
        this.parentDependency = rule.parentDependency;
        this.standardValueType = rule.standardValueType;
        this.validationCheck = rule.validationCheck;
        this.overlapBanRuleName = rule.overlapBanRuleName;
        this.assistType = rule.assistType;
    }

    /**
     * Is use boolean.
     *
     * @return the boolean
     */
    public boolean isUse() {
        if (!this.use) {
            return this.use;
        }

        return !(this.standardValueType != null && !this.standardValueType.equals(StandardValueType.NONE) && this.standardValue == null);
    }

    /**
     * Assist type validation rule.
     *
     * @param assistType1 the assist type 1
     * @return the validation rule
     */
    public ValidationRule assistType(AssistType assistType1) {
        this.assistType = assistType1;
        return this;
    }

    /**
     * Overlap ban rule validation rule.
     *
     * @param rule the rule
     * @return the validation rule
     */
    public ValidationRule overlapBanRule(BasicCheckRule rule) {
        this.overlapBanRuleName = rule.name();
        return this;
    }

    /**
     * Overlap ban rule validation rule.
     *
     * @param ruleName the rule name
     * @return the validation rule
     */
    public ValidationRule overlapBanRule(String ruleName) {
        this.overlapBanRuleName = ruleName;
        return this;
    }


    /**
     * Overlap ban rule validation rule.
     *
     * @param banRule the ban rule
     * @return the validation rule
     */
    public ValidationRule overlapBanRule(ValidationRule banRule) {
        if (banRule != null) {
            this.overlapBanRuleName = banRule.getRuleName();
        }
        return this;
    }

    /**
     * Parent dependency validation rule.
     *
     * @return the validation rule
     */
    public ValidationRule parentDependency() {
        this.parentDependency = true;
        return this;
    }


    /**
     * Is used my rule boolean.
     *
     * @param item the item
     * @return the boolean
     */
    public boolean isUsedMyRule(ValidationData item) {
        List<ValidationRule> usedRules = item.getValidationRules();
        if (usedRules == null || usedRules.isEmpty()) {
            return false;
        }
        return usedRules.stream().filter(ur -> ur.getRuleName().equals(this.ruleName) && ur.isUse()).findAny().orElse(null) != null;
    }

    /**
     * Filter list.
     *
     * @param allList the all list
     * @return the list
     */
    public List<ValidationData> filter(List<ValidationData> allList) {
        return allList.stream().filter(vd -> this.isUsedMyRule(vd)).collect(Collectors.toList());
    }

}
