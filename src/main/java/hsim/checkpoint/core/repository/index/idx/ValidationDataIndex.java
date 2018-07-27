package hsim.checkpoint.core.repository.index.idx;

import hsim.checkpoint.core.domain.ValidationData;

import java.util.List;
import java.util.Map;

public interface ValidationDataIndex {

    void refresh();

    List<ValidationData> get(String key);

    String getKey(ValidationData data);

    Map<String, List<ValidationData>> getMap();
}
