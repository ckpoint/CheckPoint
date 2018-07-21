package hsim.checkpoint.core.component;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Component map.
 */
@Slf4j
public class ComponentMap {

    private static Map<Class<?>, Object> map = new HashMap();

    /**
     * Get t.
     *
     * @param <T>   the type parameter
     * @param cType the c type
     * @return the t
     */
    public synchronized static <T> T get(Class<T> cType) {

        if( map.get(cType) != null){
            return cType.cast(map.get(cType));
        }

        try {
            Object obj = cType.newInstance();
            log.debug("[CREATE INSTANCE] : " + cType.getSimpleName());
            map.put(cType, obj);
            return cType.cast(obj);
        } catch (InstantiationException | IllegalAccessException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

}
