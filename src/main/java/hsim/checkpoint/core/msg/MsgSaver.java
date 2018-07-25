package hsim.checkpoint.core.msg;

import hsim.checkpoint.config.ValidationConfig;
import hsim.checkpoint.core.annotation.ValidationBody;
import hsim.checkpoint.core.annotation.ValidationParam;
import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.component.DetailParam;
import hsim.checkpoint.core.domain.BasicCheckInfo;
import hsim.checkpoint.core.domain.ReqUrl;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.core.repository.ValidationDataRepository;
import hsim.checkpoint.core.store.ValidationStore;
import hsim.checkpoint.type.ParamType;
import hsim.checkpoint.util.AnnotationScanner;
import hsim.checkpoint.util.excel.TypeCheckUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * The type Msg saver.
 */
@Slf4j
public class MsgSaver {

    private ValidationDataRepository validationDataRepository = ComponentMap.get(ValidationDataRepository.class);
    private ValidationStore validationStore = ComponentMap.get(ValidationStore.class);
    private ValidationConfig validationConfig = ComponentMap.get(ValidationConfig.class);
    private AnnotationScanner annotationScanner = ComponentMap.get(AnnotationScanner.class);

    /**
     * Instantiates a new Msg saver.
     */
    public MsgSaver() {
    }


    /**
     * Annotation scan.
     *
     * @param maxDeepLeel the max deep leel
     */
    public void annotationScan(final int maxDeepLeel) {

        Arrays.stream(ParamType.values()).forEach(paramType -> this.initDetailParams(paramType, maxDeepLeel));

        this.validationDataRepository.flush();
        this.validationStore.refresh();
        log.info("[ANNOTATION_SCAN] Complete");
    }

    private void initDetailParams(ParamType paramType, final int maxDeepLevel) {

        List<DetailParam> params = this.annotationScanner.getParameterWithAnnotation(paramType.equals(ParamType.BODY) ? ValidationBody.class : ValidationParam.class);

        params.forEach(param -> {
            List<ReqUrl> urls = param.getReqUrls();
            urls.forEach(url -> {
                this.saveParameter(param, paramType, url, null, param.getParameterType(), 0, maxDeepLevel);
            });
        });
    }

    /**
     * Url check and save.
     *
     * @param basicCheckInfo the basic check info
     * @param paramType      the param type
     * @param reqUrl         the req url
     * @param type           the type
     */
    public void urlCheckAndSave(BasicCheckInfo basicCheckInfo, ParamType paramType, ReqUrl reqUrl, Class<?> type) {
        if (!this.validationConfig.isFreshUrlSave() || !basicCheckInfo.isUrlMapping()) {
            return;
        }

        List<ValidationData> datas = this.validationDataRepository.findByParamTypeAndMethodAndUrl(paramType, reqUrl.getMethod(), reqUrl.getUrl());
        if (datas.isEmpty()) {
            this.saveParameter(basicCheckInfo.getDetailParam(), paramType, reqUrl, null, type, 0, this.validationConfig.getMaxDeepLevel());
            this.validationDataRepository.flush();
            this.validationStore.refresh();
        }
    }

    private ValidationData saveParam(DetailParam detailParam, ParamType paramType, ReqUrl reqUrl, ValidationData parent, Field field, int deepLevel) {
        ValidationData param = this.validationDataRepository.findByParamTypeAndMethodAndUrlAndNameAndParentId(paramType, reqUrl.getMethod(), reqUrl.getUrl(), field.getName(), parent == null ? null : parent.getId());
        if (param == null) {
            param = new ValidationData(detailParam, paramType, reqUrl, parent, field, deepLevel);
        } else {
            param.updateKey(detailParam);
        }

        return this.validationDataRepository.save(param);
    }

    private void saveParameter(DetailParam detailParam, ParamType paramType, ReqUrl reqUrl, ValidationData parent, Class<?> type, int deepLevel, final int maxDeepLevel) {

        if (type != null && type.getSuperclass() != null && !type.getSuperclass().equals(Object.class)) {
            this.saveParameter(detailParam, paramType, reqUrl, parent, type.getSuperclass(), deepLevel, maxDeepLevel);
        }

        if (deepLevel > maxDeepLevel) {
            log.debug(detailParam.getParameterType().getName() + "deep level " + deepLevel + " param : " + type.getName());
            return;
        }

        for (Field field : type.getDeclaredFields()) {

            ValidationData param = this.saveParam(detailParam, paramType, reqUrl, parent, field, deepLevel);

            if (param.isObj()) {
                this.saveParameter(detailParam, paramType, reqUrl, param, param.getTypeClass(), deepLevel + 1, maxDeepLevel);
            } else if (param.isList() && !param.isListChild() && TypeCheckUtil.isObjClass(param.getInnerClass())) {
                this.saveParameter(detailParam, paramType, reqUrl, param, param.getInnerClass(), deepLevel + 1, maxDeepLevel);
            }
        }
    }


}
