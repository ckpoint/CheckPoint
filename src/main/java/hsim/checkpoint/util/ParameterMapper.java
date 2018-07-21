package hsim.checkpoint.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Parameter mapper.
 */
@Slf4j
public class ParameterMapper {

    private static final int QUERY_PARAM_SPLIT_SIZE = 2;

    private static String getSetMethodName(String name) {
        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * Request paramater to object t.
     *
     * @param <T> the type parameter
     * @param req the req
     * @param c   the c
     * @return the t
     */
    public static <T> T requestParamaterToObject(NativeWebRequest req, Class<T> c) {
        return requestParamaterToObject((HttpServletRequest) req.getNativeRequest(), c, "UTF-8");
    }

    /**
     * Request paramater to object t.
     *
     * @param <T>     the type parameter
     * @param request the request
     * @param c       the c
     * @param charset the charset
     * @return the t
     */
    public static <T> T requestParamaterToObject(HttpServletRequest request, Class<T> c, String charset) {

        Map<String, String> map = new HashMap<>();

        String query = request.getQueryString();

        if (query != null) {
            String[] params = query.split("&");

            for (String param : params) {
                if (param == null || param.indexOf('=') < 0) {
                    continue;
                }
                String pv[] = param.split("=");
                if (pv.length != QUERY_PARAM_SPLIT_SIZE) {
                    continue;
                }

                String value = pv[1];

                try {
                    value = URLDecoder.decode(pv[1], charset);
                } catch (UnsupportedEncodingException e) {
                    log.info("unsupport encoding : " + pv[1]);
                    value = pv[1];
                }

                map.put(pv[0], value);
            }
        }

        return c.cast(mapToObject(map, c));

    }

    /**
     * Find method method.
     *
     * @param c          the c
     * @param methodName the method name
     * @return the method
     */
    public static Method findMethod(Class<?> c, String methodName) {
        for (Method m : c.getMethods()) {
            if (!m.getName().equalsIgnoreCase(methodName)) {
                continue;
            }
            if (m.getParameterCount() != 1) {
                continue;
            }
            return m;
        }

        return null;
    }

    /**
     * Cast value object.
     *
     * @param m        the m
     * @param castType the cast type
     * @param value    the value
     * @return the object
     */
    @SuppressWarnings("PMD.LooseCoupling")
    public static Object castValue(Method m, Class<?> castType, String value) {

        try {
            if (castType.isEnum()) {
                Method valueOf;
                try {
                    valueOf = castType.getMethod("valueOf", String.class);
                    return valueOf.invoke(null, value);
                } catch (Exception e) {
                    log.info("enum value of excute error : (" + castType.getName() + ") | " + value);
                    return null;
                }
            } else if (castType == Integer.class || castType == int.class) {
                return Integer.parseInt(value);
            } else if (castType == Long.class || castType == long.class) {
                return Long.parseLong(value);
            } else if (castType == Double.class || castType == double.class) {
                return Double.parseDouble(value);
            } else if (castType == Boolean.class || castType == boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (castType == Float.class || castType == float.class) {
                return Float.parseFloat(value);
            } else if (castType == String.class) {
                return value;
            } else if (castType == ArrayList.class || castType == List.class) {
                ParameterizedType paramType = (ParameterizedType) m.getGenericParameterTypes()[0];
                Class<?> paramClass = (Class<?>) paramType.getActualTypeArguments()[0];
                List<Object> castList = new ArrayList<>();
                String[] values = value.split(",");
                for (String v : values) {
                    castList.add(castValue(m, paramClass, v));
                }

                return castList;
            } else {
                log.info("invalid castType : " + castType);
                return null;
            }
        } catch (NumberFormatException e) {
            log.info("value : " + value + " is invalid value, setter parameter type is  : " + castType);
            return null;
        }
    }


    /**
     * Map to object object.
     *
     * @param map the map
     * @param c   the c
     * @return the object
     */
    public static Object mapToObject(Map<String, String> map, Class<?> c) {

        Method m = null;
        Object obj = null;

        try {
            obj = c.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.info("instance create fail : " + c.getName());
            return null;
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {

            if (entry.getValue() == null || entry.getValue().trim().isEmpty()) {
                continue;
            }

            if (entry.getValue().equalsIgnoreCase("undefined")) {
                continue;
            }

            try {
                m = findMethod(c, getSetMethodName(entry.getKey()));
                if (m == null) {
                    log.info("not found mapping method : " + entry.getKey());
                    continue;
                }
                try {
                    m.invoke(obj, castValue(m, m.getParameterTypes()[0], entry.getValue()));
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    log.info("method invoke error : " + m.getName());
                }
            } catch (SecurityException e) {
                log.info("security exception : " + e.getMessage());
            }
        }

        return obj;
    }
}
