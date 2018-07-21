package hsim.checkpoint.core.msg;

import hsim.checkpoint.config.ValidationConfig;
import hsim.checkpoint.core.annotation.ValidationBody;
import hsim.checkpoint.core.annotation.ValidationParam;
import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.component.DetailParam;
import hsim.checkpoint.core.domain.ReqUrl;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.core.repository.ValidationDataRepository;
import hsim.checkpoint.core.store.ValidationStore;
import hsim.checkpoint.type.ParamType;
import hsim.checkpoint.util.AnnotationScanner;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Method syncor.
 */
@Slf4j
public class MethodSyncor {

    private ValidationDataRepository validationDataRepository = ComponentMap.get(ValidationDataRepository.class);
    private ValidationStore validationStore = ComponentMap.get(ValidationStore.class);
    private AnnotationScanner annotationScanner = ComponentMap.get(AnnotationScanner.class);

    /**
     * Instantiates a new Method syncor.
     */
    public MethodSyncor() {
    }

    /**
     * Update method key async.
     */
    public void updateMethodKeyAsync() {
        new Thread(() -> {
            this.updateMethodKey();
        }).start();
    }

    private void updateMethodKey() {
        Arrays.stream(ParamType.values()).forEach(paramType -> this.syncMethodKey(paramType));

        this.validationDataRepository.flush();
        this.validationStore.refresh();
        log.info("[METHOD_KEY_SYNC] Complete");
    }

    private void syncMethodKey(ParamType paramType) {

        List<DetailParam> params = this.annotationScanner.getParameterWithAnnotation(paramType.equals(ParamType.BODY) ? ValidationBody.class : ValidationParam.class);

        params.forEach(param -> {
            List<ReqUrl> urls = param.getReqUrls();
            urls.forEach(url -> {
                List<ValidationData> datas = this.validationDataRepository.findByParamTypeAndMethodAndUrl(paramType, url.getMethod(), url.getUrl());
                if (!datas.isEmpty()) {
                    this.validationDataRepository.saveAll(datas.stream().map(data -> data.updateKey(param)).collect(Collectors.toList()));
                }
            });
        });
    }

}
