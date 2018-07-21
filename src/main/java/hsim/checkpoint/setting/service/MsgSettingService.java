package hsim.checkpoint.setting.service;

import hsim.checkpoint.core.domain.ReqUrl;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.type.ParamType;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;


/**
 * The interface Msg setting service.
 */
public interface MsgSettingService {
    /**
     * Gets all url list.
     *
     * @return the all url list
     */
    List<ReqUrl> getAllUrlList();

    /**
     * Gets validation data.
     *
     * @param method the method
     * @param url    the url
     * @return the validation data
     */
    List<ValidationData> getValidationData(String method, String url);

    /**
     * Gets all validation data.
     *
     * @return the all validation data
     */
    List<ValidationData> getAllValidationData();

    /**
     * Gets validation data.
     *
     * @param paramType the param type
     * @param method    the method
     * @param url       the url
     * @return the validation data
     */
    List<ValidationData> getValidationData(ParamType paramType, String method, String url);

    /**
     * Update validation data.
     *
     * @param models the models
     */
    void updateValidationData(List<ValidationData> models);

    /**
     * Update validation data.
     *
     * @param req the req
     */
    void updateValidationData(MultipartHttpServletRequest req);

    /**
     * Delete validation data.
     *
     * @param models the models
     */
    void deleteValidationData(List<ValidationData> models);
    void deleteAll();

    /**
     * Delete validation data.
     *
     * @param url the url
     */
    void deleteValidationData(ReqUrl url);

}
