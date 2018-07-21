package hsim.checkpoint.config;

import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

/**
 * The type Validation config.
 */
@ToString
@Getter
public class ValidationConfig {

    @Value("${validation.fresh.url.save:false}")
    private boolean freshUrlSave;

    @Value("${validation.msg.check.body.logging:true}")
    private boolean bodyLogging;

    @Value("${validation.setting.password:taeon}")
    private String authToken;

    @Value("${validation.save.max.deeplevel:5}")
    private int maxDeepLevel;


}
