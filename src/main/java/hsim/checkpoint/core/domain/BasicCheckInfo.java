package hsim.checkpoint.core.domain;

import hsim.checkpoint.core.annotation.ValidationBody;
import hsim.checkpoint.core.component.DetailParam;
import hsim.checkpoint.type.ParamType;
import hsim.checkpoint.util.ValidationHttpUtil;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;

/**
 * The type Basic check info.
 */
@ToString
@Getter
@Slf4j
public class BasicCheckInfo {

    private ParamType paramType;
    private ReqUrl reqUrl;
    private DetailParam detailParam;
    private HttpServletRequest req;
    private MethodParameter parameter;
    private String body;

    public BasicCheckInfo(MethodParameter param) {
        this.parameter = param;
        this.paramType = ParamType.RESPONSE_BODY;
        this.detailParam = new DetailParam(parameter.getParameterType(), parameter.getMethod(), null);
    }

    /**
     * Instantiates a new Basic check info.
     *
     * @param httpReq the http req
     * @param param   the param
     * @param log     the log
     */
    public BasicCheckInfo(HttpServletRequest httpReq, MethodParameter param, boolean log) {
        this.req = httpReq;
        this.parameter = param;

        this.paramType = this.parameter.getParameterAnnotation(ValidationBody.class) != null ? ParamType.BODY : ParamType.QUERY_PARAM;

        this.detailParam = new DetailParam(parameter.getParameterType(), parameter.getMethod(), null);
        this.reqUrl = new ReqUrl(this.req);

        if (this.paramType.equals(ParamType.BODY)) {
            this.body = ValidationHttpUtil.readBody(this.req);

            if (log) {
                this.logging();
            }
        }
    }

    /**
     * Is url mapping boolean.
     *
     * @return the boolean
     */
    public boolean isUrlMapping() {
        if (this.detailParam == null) {
            return false;
        }
        return this.detailParam.isUrlMapping();
    }

    /**
     * Is list body boolean.
     *
     * @return the boolean
     */
    public boolean isListBody() {
        return (this.parameter.getParameterType().equals(java.util.List.class));
    }

    /**
     * Logging.
     */
    public void logging() {
        if (this.body == null) {
            return;
        }

        log.info("[" + this.reqUrl.getMethod() + "]::[" + this.reqUrl.getUrl() + "] ::");
        log.info(this.body);
    }

    /**
     * Gets unique key.
     *
     * @return the unique key
     */
    public String getUniqueKey() {
        return this.isUrlMapping() ? this.reqUrl.getUniqueKey() : this.detailParam.getMethodKey();
    }

}
