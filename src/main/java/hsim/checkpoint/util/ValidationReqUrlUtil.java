package hsim.checkpoint.util;

import hsim.checkpoint.core.domain.ReqUrl;
import hsim.checkpoint.core.domain.ValidationData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Validation req url util.
 */
public class ValidationReqUrlUtil {

    /**
     * Gets url list from validation datas.
     *
     * @param datas the datas
     * @return the url list from validation datas
     */
    public static List<ReqUrl> getUrlListFromValidationDatas(List<ValidationData> datas) {
        Map<String, ReqUrl> urlMap = new HashMap<>();
        datas.stream().forEach(data -> {
            ReqUrl url = new ReqUrl(data);
            urlMap.put(url.getUniqueKey(), url);
        });

        return urlMap.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
    }
}
