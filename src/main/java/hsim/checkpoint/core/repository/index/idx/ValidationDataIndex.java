package hsim.checkpoint.core.repository.index.idx;

import hsim.checkpoint.core.domain.ValidationData;

import java.util.List;
import java.util.Map;

/**
 * The interface Validation data index.
 */
public interface ValidationDataIndex {

    /**
     * Refresh.
     */
    void refresh();

    /**
     * Get list.
     *
     * @param key the key
     * @return the list
     */
    List<ValidationData> get(String key);

    /**
     * Gets key.
     *
     * @param data the data
     * @return the key
     */
    String getKey(ValidationData data);

    /**
     * Gets map.
     *
     * @return the map
     */
    Map<String, List<ValidationData>> getMap();
}
