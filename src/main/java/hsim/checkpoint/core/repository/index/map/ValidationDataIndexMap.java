package hsim.checkpoint.core.repository.index.map;

import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.domain.ReqUrl;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.core.repository.index.idx.IdIndex;
import hsim.checkpoint.core.repository.index.idx.MethodAndUrlIndex;
import hsim.checkpoint.core.repository.index.idx.ValidationDataIndex;
import hsim.checkpoint.core.repository.index.util.ValidationIndexUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Validation data index map.
 */
public class ValidationDataIndexMap {

    private ValidationDataIndex methodAndUrlIndex = ComponentMap.get(MethodAndUrlIndex.class);
    private ValidationDataIndex idIndex = ComponentMap.get(IdIndex.class);

    private ValidationDataIndex[] idxs = {this.methodAndUrlIndex, this.idIndex};

    /**
     * Gets url list.
     *
     * @return the url list
     */
    public List<ReqUrl> getUrlList() {
        List<ReqUrl> reqUrls = new ArrayList<>();
        for (String key : this.methodAndUrlIndex.getMap().keySet()) {
            List<ValidationData> datas = this.methodAndUrlIndex.getMap().get(key);
            if (!datas.isEmpty()) {
                reqUrls.add(new ReqUrl(datas.get(0).getMethod(), datas.get(0).getUrl()));
            }
        }
        return reqUrls;
    }

    /**
     * Find by id validation data.
     *
     * @param id the id
     * @return the validation data
     */
    public ValidationData findById(Long id) {
        List<ValidationData> datas = this.idIndex.get(ValidationIndexUtil.makeKey(String.valueOf(id)));
        if (datas.isEmpty()) {
            return null;
        }
        return datas.get(0);
    }

    /**
     * Find by method and url list.
     *
     * @param method the method
     * @param url    the url
     * @return the list
     */
    public List<ValidationData> findByMethodAndUrl(String method, String url) {
        return this.methodAndUrlIndex.get(ValidationIndexUtil.makeKey(method, url));
    }

    /**
     * Add index.
     *
     * @param data the data
     */
    public void addIndex(ValidationData data) {
        for (ValidationDataIndex idx : this.idxs) {
            ValidationIndexUtil.addIndexData(data, idx);
        }
    }

    /**
     * Remove index.
     *
     * @param data the data
     */
    public void removeIndex(ValidationData data) {
        for (ValidationDataIndex idx : this.idxs) {
            ValidationIndexUtil.removeIndexData(data, idx);
        }
    }

    /**
     * Refresh.
     */
    public void refresh() {
        for (ValidationDataIndex idx : this.idxs) {
            idx.refresh();
        }
    }

}
