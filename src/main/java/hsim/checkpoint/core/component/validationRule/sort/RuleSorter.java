package hsim.checkpoint.core.component.validationRule.sort;

import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;

import java.util.Comparator;

/**
 * The type Rule sorter.
 */
public class RuleSorter implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        ValidationRule rule1 = (ValidationRule) o1;
        ValidationRule rule2 = (ValidationRule) o2;

        int v1 = rule1.getOrderIdx();
        int v2 = rule2.getOrderIdx();

        if (v1 < v2) {
            return -1;
        } else if (v1 == v2) {
            return 0;
        } else {
            return 1;
        }
    }
}
