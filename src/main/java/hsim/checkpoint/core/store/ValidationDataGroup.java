package hsim.checkpoint.core.store;

import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.type.ParamType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Validation data group.
 */
@Getter
public class ValidationDataGroup {
    private ParamType paramType;
    private List<ValidationRule> rules = new ArrayList<>();

    /**
     * Instantiates a new Validation data group.
     *
     * @param paramType       the param type
     * @param validationDatas the validation datas
     */
    public ValidationDataGroup(ParamType paramType, List<ValidationData> validationDatas) {
        this.initDataGroup(paramType, validationDatas);
    }


    /**
     * Add rule.
     *
     * @param vd the vd
     */
    public void addRule(ValidationData vd) {
        this.rules.addAll(vd.getValidationRules().stream().filter(vr -> vr.isUse()).collect(Collectors.toList()));
    }

    /**
     * Init data group.
     *
     * @param paramType the param type
     * @param lists     the lists
     */
    public void initDataGroup(ParamType paramType, List<ValidationData> lists) {
        this.paramType = paramType;

        lists.forEach(vd -> {
            this.addRule(vd);
        });

    }
}
