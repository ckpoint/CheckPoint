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

    @Value("${ckpoint.save.url:true}")
    private boolean freshUrlSave;

    @Value("${ckpoint.max.deeplevel:5}")
    private int maxDeepLevel;

    @Value("${ckpoint.body.logging:true}")
    private boolean bodyLogging;

    @Value("${ckpoint.password:taeon}")
    private String authToken;

    @Value("${ckpoint.path.repository:/checkpoint/validation.json}")
    private String repositoryPath = "/checkpoint/validation.json";


}
