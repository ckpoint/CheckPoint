package hsim.checkpoint.core.domain;

import hsim.checkpoint.core.component.DetailParam;
import hsim.checkpoint.core.component.validationRule.rule.ValidationRule;
import hsim.checkpoint.core.component.validationRule.sort.RuleSorter;
import hsim.checkpoint.exception.ValidationLibException;
import hsim.checkpoint.type.ParamType;
import hsim.checkpoint.util.ValidationObjUtil;
import hsim.checkpoint.util.excel.TypeCheckUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Validation data.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Slf4j
public class ValidationData {

    private Long id;
    private String url;
    private String method;

    private ParamType paramType;

    private Long parentId;
    private String name;

    private int deepLevel;

    private String type;
    private Class<?> typeClass;
    private Class<?> innerClass;

    private boolean number;
    private boolean obj;
    private boolean enumType;
    private boolean list;

    private String methodKey;
    private String parameterKey;

    private boolean listChild;

    private boolean urlMapping;

    @Getter(AccessLevel.PRIVATE)
    private ValidationData parent;

    private List<ValidationRule> validationRules = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<ValidationRule> tempRules = new ArrayList<>();


    /**
     * Instantiates a new Validation data.
     *
     * @param detailParam the detail param
     * @param paramType   the param type
     * @param reqUrl      the req url
     * @param parent      the parent
     * @param field       the field
     * @param deepLevel   the deep level
     */
    public ValidationData(DetailParam detailParam, ParamType paramType, ReqUrl reqUrl, ValidationData parent, Field field, int deepLevel) {

        this.method = reqUrl.getMethod();
        this.url = reqUrl.getUrl();
        this.paramType = paramType;
        this.parentId = parent == null ? null : parent.getId();
        this.deepLevel = deepLevel;
        this.urlMapping = detailParam.isUrlMapping();

        this.listChild = (parent  != null && ( parent.isList() || parent.listChild));

        this.updateField(field);
        this.updateKey(detailParam);
    }

    /**
     * Equal url boolean.
     *
     * @param url the url
     * @return the boolean
     */
    public boolean equalUrl(ReqUrl url) {
        return url.getMethod().equalsIgnoreCase(this.method) && url.getUrl().equalsIgnoreCase(this.url);
    }

    private Object castSetValue(ValidationData param, Object value) {
        try {
            Method valueof = param.getTypeClass().getMethod("valueOf", String.class);
            try {
                return valueof.invoke(null, value.toString());
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.info("value of invoke error : " + param.getName());
                return value;
            }
        } catch (NoSuchMethodException e) {
            return value;
        }
    }

    private void setter(ValidationData param, Object bodyObj, Object setValue) {

        Method setter = ValidationObjUtil.getSetterMethodNotCheckParamType(bodyObj.getClass(), param.getName());
        if (setter == null) {
            log.error("setter method is null :" + param.getName());
            return;
        }
        try {
            setter.invoke(bodyObj, this.castSetValue(param, setValue));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ValidationLibException("setter fail: " + param.getName(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Replace value.
     *
     * @param bodyObj      the body obj
     * @param replaceValue the replace value
     */
    public void replaceValue(Object bodyObj, Object replaceValue) {
        Object parentObj = bodyObj;

        if(this.listChild) {
            ValidationData rootListParent = this.findListParent();
            if(this.deepLevel - rootListParent.deepLevel > 1 ){
                parentObj = this.parent.getValue(bodyObj);
            }
        }
        else {
            if (this.parentId != null) {
                parentObj = this.parent.getValue(bodyObj);
            }
        }

        this.setter(this, parentObj, replaceValue);
    }

    private Object getter(ValidationData param, Object bodyObj) {

        Method getter = ValidationObjUtil.getGetterMethod(bodyObj.getClass(), param.getName());

        if (getter == null) {
            log.error("getter method is null :" + param.getName());
            return null;
        }

        try {
            return getter.invoke(bodyObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ValidationLibException("mandatory Param is invalid : " + param.getName(), HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isRootParent(ValidationData parent, ValidationData rootParent) {
        if (parent == null) { return true; }

        if (rootParent == null) { return false;
        }
        return parent.getId().equals(rootParent.getId());
    }

    private Object getParentObj(ValidationData child, Object bodyObj, ValidationData rootParent) {
        if (isRootParent(child.getParent(), rootParent)) {
            return this.getter(child, bodyObj);
        }
        return this.getter(child, this.getParentObj(child.getParent(), bodyObj, rootParent));
    }

    /**
     * Gets value.
     *
     * @param bodyObj the body obj
     * @return the value
     */
    public Object getValue(Object bodyObj) {
        return this.getParentObj(this, bodyObj, this.findListParent());
    }


    /**
     * Update user data validation data.
     *
     * @param data the data
     * @return the validation data
     */
    public ValidationData updateUserData(ValidationData data) {
        if (data == null) {
            return this;
        }
        return ValidationObjUtil.objectDeepCopyWithBlackList(data, this, "name", "parent", "type", "deepLevel");
    }

    /**
     * Update key validation data.
     *
     * @param detailParam the detail param
     * @return the validation data
     */
    public ValidationData updateKey(DetailParam detailParam) {
        if (detailParam == null) {
            return this;
        }
        this.methodKey = detailParam.getMethodKey();
        this.parameterKey = this.paramType.getUniqueKey(this.methodKey);
        this.urlMapping = detailParam.isUrlMapping();

        return this;
    }

    /**
     * Diff key boolean.
     *
     * @param detailParam the detail param
     * @return the boolean
     */
    public boolean diffKey(DetailParam detailParam){
        if (detailParam == null) {
            return false;
        }

        if(!detailParam.getMethodKey().equals(this.methodKey)){
            return true;
        }

        return detailParam.isUrlMapping() != this.isUrlMapping();
    }

    /**
     * Update field.
     *
     * @param field the field
     */
    public void updateField(Field field) {
        this.name = field.getName();
        this.obj = TypeCheckUtil.isObjClass(field);
        this.list = TypeCheckUtil.isListClass(field);
        this.number = TypeCheckUtil.isNumberClass(field);
        this.enumType = field.getType().isEnum();
        this.typeClass = field.getType();
        this.type = field.getType().getSimpleName();

        if (this.list) {
            this.innerClass = ValidationObjUtil.getListInnerClassFromGenericType(field.getGenericType());
            this.type += "<" + this.innerClass.getSimpleName() + ">";
        }
    }

    /**
     * Is query param boolean.
     *
     * @return the boolean
     */
    public boolean isQueryParam() {
        if (this.paramType == null) {
            return false;
        }
        return this.paramType.equals(ParamType.QUERY_PARAM);
    }

    /**
     * Is body boolean.
     *
     * @return the boolean
     */
    public boolean isBody() {
        if (this.paramType == null) {
            return false;
        }
        return this.paramType.equals(ParamType.BODY);
    }

    /**
     * Minimalize validation data.
     *
     * @return the validation data
     */
    public ValidationData minimalize() {
        this.setValidationRules(this.validationRules.stream().filter(vr -> vr.isUse()).collect(Collectors.toList()));
        return this;
    }

    private ValidationRule getExistRule(ValidationRule r) {
        ValidationRule existRule = this.validationRules.stream().filter(rule -> r.getRuleName().equals(rule.getRuleName())).findFirst().orElse(null);
        if (existRule != null) {
            return existRule;
        }
        return this.tempRules.stream().filter(rule -> r.getRuleName().equals(rule.getRuleName())).findFirst().orElse(null);
    }

    /**
     * Rule sync validation data.
     *
     * @param rules the rules
     * @return the validation data
     */
    public ValidationData ruleSync(List<ValidationRule> rules) {
        List<ValidationRule> ruleList = new ArrayList<>();

        rules.forEach(rule -> {
            ValidationRule existRule = this.getExistRule(rule);
            ruleList.add(existRule != null ? existRule : rule);
        });

        this.validationRules.removeAll(ruleList);
        this.tempRules.addAll(this.validationRules);

        this.validationRules = ruleList;
        this.validationRules.sort(new RuleSorter());

        return this;
    }

    /**
     * Find list parent validation data.
     *
     * @return the validation data
     */
    public ValidationData findListParent() {
        if (!this.listChild) {
            return null;
        }

        ValidationData parent = this.parent;
        while (parent != null && !parent.isList()) {
            parent = parent.parent;
        }
        return parent;
    }

}
