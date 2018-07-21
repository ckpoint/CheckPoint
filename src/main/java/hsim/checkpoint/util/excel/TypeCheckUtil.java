package hsim.checkpoint.util.excel;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Type check util.
 */
@Slf4j
public class TypeCheckUtil {

    private static Class<?>[] PRIMITIVE_CLASS_TYPE = {
            Double.class, Float.class, Long.class, Integer.class, Byte.class, String.class, Short.class, Character.class, List.class,
    };

    private static Class<?>[] NUMBER_CLASS_TYPE = {
            Double.class, Float.class, Long.class, Integer.class, Byte.class, Short.class,
            double.class, float.class, long.class, int.class, byte.class, short.class
    };

    private static String[] BASIC_PACKAGE_PREFIX = {"java", "sun", "org"};

    private static List<Class<?>> PRIMITIVE_CLASS_LIST = Arrays.stream(PRIMITIVE_CLASS_TYPE).collect(Collectors.toList());

    private static List<Class<?>> NUMBER_CLASS_LIST = Arrays.stream(NUMBER_CLASS_TYPE).collect(Collectors.toList());
    private static List<String> BASIC_PACKAGE_PREFIX_LIST = Arrays.stream(BASIC_PACKAGE_PREFIX).collect(Collectors.toList());

    /**
     * Is obj class boolean.
     *
     * @param field the field
     * @return the boolean
     */
    public static boolean isObjClass(Field field) {
        return isObjClass(field.getType());
    }

    /**
     * Is list class boolean.
     *
     * @param field the field
     * @return the boolean
     */
    public static boolean isListClass(Field field) {
        return isListClass(field.getType());
    }

    /**
     * Is number class boolean.
     *
     * @param field the field
     * @return the boolean
     */
    public static boolean isNumberClass(Field field) {
        return isNumberClass(field.getType());
    }

    /**
     * Is not scan class boolean.
     *
     * @param className the class name
     * @return the boolean
     */
    public static boolean isNotScanClass(String className) {
        String block = BASIC_PACKAGE_PREFIX_LIST.stream().filter(prefix -> className.startsWith(prefix)).findAny().orElse(null);
        return block != null;
    }

    /**
     * Is obj class boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public static boolean isObjClass(Class<?> type) {
        if (type.isPrimitive() || type.isEnum() || type.isArray()) {
            return false;
        }

        String block = BASIC_PACKAGE_PREFIX_LIST.stream().filter(prefix -> type.getName().startsWith(prefix)).findAny().orElse(null);
        if (block != null) {
            return false;
        }

        return !PRIMITIVE_CLASS_LIST.contains(type);
    }

    /**
     * Is list class boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public static boolean isListClass(Class<?> type) {
        if (type.isPrimitive() || type.isEnum()) {
            return false;
        }
        return type.equals(List.class);
    }

    /**
     * Is number class boolean.
     *
     * @param type the type
     * @return the boolean
     */
    public static boolean isNumberClass(Class<?> type) {
        return NUMBER_CLASS_LIST.contains(type);
    }


    /**
     * Get default white list string [ ].
     *
     * @param field the field
     * @return the string [ ]
     */
    public static String[] getDefaultWhiteList(Field field) {
        Method values = null;

        if (!field.getType().isEnum()) {
            try {
                values = field.getType().getMethod("values");
            } catch (NoSuchMethodException e) {
                log.info("field : " + field.getName() + " values method not found ");
            }

            try {
                Object[] objs = (Object[]) values.invoke(field.getType());
                return (String[]) Arrays.stream(objs).map(obj -> String.valueOf(obj)).toArray();
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.info("values invoke error : " + e.getMessage());
            }
        }
        return null;
    }
}
