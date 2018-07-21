package hsim.checkpoint.core.component.validationRule.type;

import lombok.Getter;

/**
 * The enum Basic check rule.
 */
@Getter
public enum BasicCheckRule {
    /**
     * Mandatory basic check rule.
     */
    Mandatory, /**
     * Black list basic check rule.
     */
    BlackList, /**
     * White list basic check rule.
     */
    WhiteList, /**
     * Min size basic check rule.
     */
    MinSize, /**
     * Max size basic check rule.
     */
    MaxSize, /**
     * Default value basic check rule.
     */
    DefaultValue, /**
     * Base 64 basic check rule.
     */
    Base64, /**
     * Trim basic check rule.
     */
    Trim, /**
     * Pattern basic check rule.
     */
    Pattern
}
