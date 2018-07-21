package hsim.checkpoint.core.component.validationRule.rule;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The type Assist type.
 */
@Getter
@NoArgsConstructor
public class AssistType {
    private boolean nullable;
    private boolean string;
    private boolean number;
    private boolean enumType;
    private boolean list;
    private boolean obj;

    /**
     * All assist type.
     *
     * @return the assist type
     */
    public static AssistType all() {
        AssistType assistType = new AssistType();
        assistType.nullable = true;
        assistType.string = true;
        assistType.number = true;
        assistType.enumType = true;
        assistType.list = true;
        assistType.obj = true;
        return assistType;
    }

    /**
     * Nullable assist type.
     *
     * @return the assist type
     */
    public AssistType nullable() {
        this.nullable = true;
        return this;
    }

    /**
     * String assist type.
     *
     * @return the assist type
     */
    public AssistType string() {
        this.string = true;
        return this;
    }

    /**
     * Number assist type.
     *
     * @return the assist type
     */
    public AssistType number() {
        this.number = true;
        return this;
    }

    /**
     * Enum type assist type.
     *
     * @return the assist type
     */
    public AssistType enumType() {
        this.enumType = true;
        return this;
    }

    /**
     * List assist type.
     *
     * @return the assist type
     */
    public AssistType list() {
        this.list = true;
        return this;
    }

    /**
     * Obj assist type.
     *
     * @return the assist type
     */
    public AssistType obj() {
        this.obj = true;
        return this;
    }
}
