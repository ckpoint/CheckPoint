package hsim.checkpoint.core.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

/**
 * The type Req url.
 */
@Setter
@Getter
@NoArgsConstructor
public class ReqUrl {
    private String url;
    private String method;
    private boolean urlMapping;

    /**
     * Instantiates a new Req url.
     *
     * @param method the method
     * @param url    the url
     */
    public ReqUrl(String method, String url) {
        while(url.endsWith("/")){
            url = url.substring(0, url.length() -1);
        }
        this.method = method;
        this.url = url;
    }

    /**
     * Instantiates a new Req url.
     *
     * @param validationData the validation data
     */
    public ReqUrl(ValidationData validationData) {
        this.method = validationData.getMethod();
        this.url = validationData.getUrl();
        this.urlMapping = validationData.isUrlMapping();
    }

    /**
     * Instantiates a new Req url.
     *
     * @param req the req
     */
    public ReqUrl(HttpServletRequest req) {
        this.method = req.getMethod();
        this.url = req.getRequestURI();
    }

    /**
     * Gets unique key.
     *
     * @return the unique key
     */
    public String getUniqueKey() {
        return method + ":" + url;
    }

    /**
     * Gets sheet name.
     *
     * @param idx the idx
     * @return the sheet name
     */
    public String getSheetName(int idx) {
        String name = method + "|" + url.replace("/", "|");
        if (name.length() > 30) {
            return name.substring(0, 29) + idx;
        }
        return name;
    }

}
