package hsim.checkpoint.type;

import hsim.checkpoint.core.domain.ReqUrl;
import lombok.Getter;

/**
 * The enum Param type.
 */
public enum ParamType {
    /**
     * Body param type.
     */
    BODY("@ValidationBody"), /**
     * Query param param type.
     */
    QUERY_PARAM("@ValidationParam");

    @Getter
    private String annotationName;

    ParamType(String aName) {
        this.annotationName = aName;
    }

    /**
     * Gets unique key.
     *
     * @param reqUrl the req url
     * @return the unique key
     */
    public String getUniqueKey(ReqUrl reqUrl) {
        return this.getUniqueKey(reqUrl.getUniqueKey());
    }

    /**
     * Gets unique key.
     *
     * @param id the id
     * @return the unique key
     */
    public String getUniqueKey(String id) {
        return this.name() + ":" + id;
    }
}
