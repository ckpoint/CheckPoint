package hsim.checkpoint.type;

import hsim.checkpoint.core.annotation.ValidationBody;
import hsim.checkpoint.core.annotation.ValidationParam;
import hsim.checkpoint.core.annotation.ValidationResponse;
import hsim.checkpoint.core.domain.ReqUrl;
import lombok.Getter;

/**
 * The enum Param type.
 */
@Getter
public enum ParamType {
    /**
     * Body param type.
     */
    BODY("@ValidationBody", ValidationBody.class), /**
     * Query param param type.
     */
    QUERY_PARAM("@ValidationParam", ValidationParam.class),
    RESPONSE_BODY("@ValidationResponse", ValidationResponse.class);

    private String annotationName;
    private Class annotationClass;

    ParamType(String aName, Class aClass) {
        this.annotationName = aName;
        this.annotationClass = aClass;
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
