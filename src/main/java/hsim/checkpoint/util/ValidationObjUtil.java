package hsim.checkpoint.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hsim.checkpoint.exception.ValidationLibException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Validation obj util.
 */
@Slf4j
public class ValidationObjUtil {

    private static final Class[] EMPTY_CLASS = new Class[0];
    private static final String GET_METHOD_PREFIX = "get";
    private static final String SET_METHOD_PREFIX = "set";
    private static final String IS_METHOD_PREFIX = "is";

    /**
     * Gets default object mapper.
     *
     * @return the default object mapper
     */
    public static ObjectMapper getDefaultObjectMapper() {

        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper;
    }


    private static boolean isBlockFieldMethod(Method m, String... blockFields) {

        if (blockFields == null) {
            return false;
        }

        String methodField = getFieldNameFromMethod(m);

        for (String field : blockFields) {
            if (field.equals(methodField)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Is number obj boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public static boolean isNumberObj(Object type) {
        try {
            Double.valueOf(type + "");
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Is number type boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public static boolean isNumberType(Class<?> type) {
        return isIntType(type) || isDoubleType(type);
    }

    /**
     * Is double type boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public static boolean isDoubleType(Class<?> type) {
        return (type.isPrimitive() && type == float.class) || (type.isPrimitive() && type == double.class)
                || (!type.isPrimitive() && type == Float.class) || (!type.isPrimitive() && type == Double.class);
    }


    /**
     * Is int type boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public static boolean isIntType(Class<?> type) {
        return (type.isPrimitive() && type == int.class) || (type.isPrimitive() && type == long.class)
                || (!type.isPrimitive() && type == Integer.class) || (!type.isPrimitive() && type == Long.class);
    }

    private static Class<?> getSubType(Class<?> mainType) {
        if (mainType.isPrimitive() && mainType == int.class) {
            return Integer.class;
        } else if (mainType.isPrimitive() && mainType == long.class) {
            return Long.class;
        } else if (mainType.isPrimitive() && mainType == float.class) {
            return Float.class;
        } else if (mainType.isPrimitive() && mainType == double.class) {
            return Float.class;
        } else if (!mainType.isPrimitive() && mainType == Integer.class) {
            return int.class;
        } else if (!mainType.isPrimitive() && mainType == Long.class) {
            return long.class;
        } else if (!mainType.isPrimitive() && mainType == Float.class) {
            return float.class;
        } else if (!mainType.isPrimitive() && mainType == Double.class) {
            return double.class;
        }

        return null;
    }

    /**
     * Gets setter method not check param type.
     *
     * @param cType     the c type
     * @param fieldName the field name
     * @return the setter method not check param type
     */
    public static Method getSetterMethodNotCheckParamType(Class<?> cType, String fieldName) {
        String methodName = getMethodName(fieldName, SET_METHOD_PREFIX);
        Method[] methods = cType.getMethods();
        for (Method m : methods) {
            if (m.getName().equals(methodName) && m.getParameterCount() == 1) {
                return m;
            }
        }
        return null;
    }

    /**
     * Gets setter method.
     *
     * @param cType     the c type
     * @param fieldName the field name
     * @param paramType the param type
     * @return the setter method
     */
    public static Method getSetterMethod(Class<?> cType, String fieldName, Class<?> paramType) {

        Class<?> subType = getSubType(paramType);
        String methodName = getMethodName(fieldName, SET_METHOD_PREFIX);

        try {
            return cType.getMethod(methodName, paramType);
        } catch (NoSuchMethodException e) {
            try {
                return cType.getMethod(methodName, subType);
            } catch (NoSuchMethodException e1) {
                //log.info("setter method not found : " + fieldName);
                return null;
            }
        }
    }

    /**
     * Gets method name.
     *
     * @param name   the name
     * @param prefix the prefix
     * @return the method name
     */
    public static String getMethodName(String name, String prefix) {
        return prefix + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * Gets field name from method.
     *
     * @param m the m
     * @return the field name from method
     */
    public static String getFieldNameFromMethod(Method m) {
        String methodName = m.getName();

        if (methodName.startsWith(GET_METHOD_PREFIX)) {
            methodName = methodName.replaceFirst(GET_METHOD_PREFIX, "");
        } else if (methodName.startsWith(IS_METHOD_PREFIX)) {
            methodName = methodName.replaceFirst(IS_METHOD_PREFIX, "");
        } else if (methodName.startsWith(SET_METHOD_PREFIX)) {
            methodName = methodName.replaceFirst(SET_METHOD_PREFIX, "");
        }

        return methodName.substring(0, 1).toLowerCase() + methodName.substring(1);

    }

    /**
     * Gets getter method.
     *
     * @param c     the c
     * @param field the field
     * @return the getter method
     */
    public static Method getGetterMethod(Class<?> c, String field) {

        try {
            return c.getMethod(getMethodName(field, GET_METHOD_PREFIX), EMPTY_CLASS);
        } catch (NoSuchMethodException e) {
            try {
                return c.getMethod(getMethodName(field, IS_METHOD_PREFIX), EMPTY_CLASS);
            } catch (NoSuchMethodException e1) {
                //log.info("getter method not found : " + field);
                return null;
            }
        }
    }

    /**
     * Gets value.
     *
     * @param obj   the obj
     * @param field the field
     * @return the value
     */
    public static Object getValue(Object obj, String field) {
        Method getter = getGetterMethod(obj.getClass(), field);

        try {
            return getter.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    /**
     * Copy from to.
     *
     * @param from       the from
     * @param to         the to
     * @param fieldNames the field names
     */
    public static void copyFromTo(Object from, Object to, String... fieldNames) {
        for (String field : fieldNames) {
            copyFromTo(from, to, field);
        }
    }

    /**
     * Copy from to.
     *
     * @param from      the from
     * @param to        the to
     * @param fieldName the field name
     */
    public static void copyFromTo(Object from, Object to, String fieldName) {

        if (from == null || to == null) {
            log.info("object deep copy : from or to is null ");
            return;
        }

        try {
            Method getter = getGetterMethod(from.getClass(), fieldName);

            if (getter == null) {
                //log.info("getter method not found : " + fieldName);
                return;
            }

            Method setter = getSetterMethod(to.getClass(), fieldName, getter.getReturnType());
            if (setter == null) {
                //log.info("setter method not found : " + fieldName);
                return;
            }

            setter.invoke(to, getter.invoke(from, EMPTY_CLASS));

        } catch (IllegalAccessException | InvocationTargetException e) {
            log.info("set method invoke error : " + fieldName);
        }
    }


    private static Object getNewInstance(Class<?> c) {
        try {
            return c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.info("new instance create fail : " + e.getMessage());
            throw new ValidationLibException("new instnace create fail : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }


    /**
     * Get declared fields string [ ].
     *
     * @param baseClass the base class
     * @return the string [ ]
     */
    public static List<String> getDeclaredFields(Class<?> baseClass) {

        Method[] methods = baseClass.getMethods();

        List<String> fields = new ArrayList<>();

        for (Method fm : methods) {
            if ((!fm.getName().startsWith(GET_METHOD_PREFIX) && !fm.getName().startsWith(IS_METHOD_PREFIX)) || fm.getParameterCount() != 0) {
                continue;
            }

            String field = getFieldNameFromMethod(fm);
            fields.add(field);
        }
        return fields;
    }


    /**
     * Obj deep copy object.
     *
     * @param from       the from
     * @param to         the to
     * @param copyFields the copy fields
     * @return the object
     */
    public static Object objDeepCopy(Object from, Object to, String... copyFields) {
        if (from == null || to == null) {
            log.error("object deep copy from or to is null ");
            return to;
        }

        for (String field : copyFields) {
            copyFromTo(from, to, field);
        }

        return to;
    }

    /**
     * Object deep copy with white list t.
     *
     * @param <T>        the type parameter
     * @param from       the from
     * @param to         the to
     * @param copyFields the copy fields
     * @return the t
     */
    public static <T> T objectDeepCopyWithWhiteList(Object from, Object to, String... copyFields) {
        return (T) to.getClass().cast(objDeepCopy(from, to, copyFields));
    }

    /**
     * Is getter method boolean.
     *
     * @param m the m
     * @return the boolean
     */
    public static boolean isGetterMethod(Method m) {

        String methodName = m.getName();

        if (methodName.equals("getClass")) {
            return false;
        }

        if (!methodName.startsWith(GET_METHOD_PREFIX) && !methodName.startsWith(IS_METHOD_PREFIX)) {
            return false;
        }
        if (m.getParameterCount() > 0) {
            return false;
        }

        return true;
    }

    /**
     * Object deep copy with black list t.
     *
     * @param <T>         the type parameter
     * @param from        the from
     * @param to          the to
     * @param blockFields the block fields
     * @return the t
     */
    public static <T> T objectDeepCopyWithBlackList(Object from, Object to, String... blockFields) {

        if (to == null) {
            to = getNewInstance(from.getClass());
        }

        List<String> whiteList = new ArrayList<>();

        Method[] methods = from.getClass().getMethods();

        for (Method m : methods) {

            if (!isGetterMethod(m)) {
                continue;
            }

            if (isBlockFieldMethod(m, blockFields)) {
                continue;
            }

            whiteList.add(getFieldNameFromMethod(m));

        }

        return objectDeepCopyWithWhiteList(from, to, whiteList.toArray(new String[whiteList.size()]));

    }

    /**
     * Object deep copy with black list t.
     *
     * @param <T>         the type parameter
     * @param from        the from
     * @param to          the to
     * @param baseClass   the base class
     * @param blockFields the block fields
     * @return the t
     */
    public static <T> T objectDeepCopyWithBlackList(Object from, Object to, Class<?> baseClass, String... blockFields) {

        List<String> bFields = getDeclaredFields(baseClass);

        for (String blockField : blockFields) {
            bFields.add(blockField);
        }

        return objectDeepCopyWithBlackList(from, to, bFields.toArray(new String[bFields.size()]));
    }

    /**
     * Object deep copy with black list t.
     *
     * @param <T>         the type parameter
     * @param from        the from
     * @param toClass     the to class
     * @param blockFields the block fields
     * @return the t
     */
    public static <T> T objectDeepCopyWithBlackList(Object from, Class<T> toClass, String... blockFields) {
        Object to = getNewInstance(toClass);
        return objectDeepCopyWithBlackList(from, to, blockFields);
    }

    /**
     * Object deep copy with black list t.
     *
     * @param <T>         the type parameter
     * @param from        the from
     * @param toClass     the to class
     * @param baseClass   the base class
     * @param blockFields the block fields
     * @return the t
     */
    public static <T> T objectDeepCopyWithBlackList(Object from, Class<T> toClass, Class<?> baseClass, String... blockFields) {
        Object to = getNewInstance(toClass);
        return objectDeepCopyWithBlackList(from, to, baseClass, blockFields);
    }

    /**
     * Gets object size.
     *
     * @param value the value
     * @return the object size
     */
    public static Double getObjectSize(Object value) {

        double v = 1;

        if (value instanceof String) {
            v = ((String) value).length();
            if (v < 1) {
                return null;
            }
        } else if (value instanceof List) {
            v = ((List) value).size();
        } else if (ValidationObjUtil.isNumberObj(value)) {
            v = Double.valueOf(value + "");
        }

        return v;
    }

    /**
     * Gets list inner class from generic type.
     *
     * @param genericType the generic type
     * @return the list inner class from generic type
     */
    public static Class<?> getListInnerClassFromGenericType(Type genericType) {
        ParameterizedType innerClass = (ParameterizedType) genericType;
        return (Class<?>) innerClass.getActualTypeArguments()[0];
    }

}

