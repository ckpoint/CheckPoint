package hsim.checkpoint.setting.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.domain.ReqUrl;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.core.repository.ValidationDataRepository;
import hsim.checkpoint.core.store.ValidationStore;
import hsim.checkpoint.type.ParamType;
import hsim.checkpoint.util.ValidationObjUtil;
import hsim.checkpoint.util.ValidationReqUrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Msg setting service.
 */
@Slf4j
public class MsgSettingServiceImpl implements MsgSettingService {

    private ValidationDataRepository validationDataRepository = ComponentMap.get(ValidationDataRepository.class);
    private ValidationStore validationStore = ComponentMap.get(ValidationStore.class);

    public List<ReqUrl> getAllUrlList() {
        return this.validationDataRepository.findAllUrl();
    }

    @Override
    public List<ValidationData> getValidationData(String method, String url) {
        return this.validationDataRepository.findByMethodAndUrl(method, url);
    }

    @Override
    public List<ValidationData> getAllValidationData() {
        return this.validationDataRepository.findAll();
    }

    @Override
    public List<ValidationData> getValidationData(ParamType paramType, String method, String url) {
        return this.validationDataRepository.findByParamTypeAndMethodAndUrl(paramType, method, url);
    }

    @Override
    public void updateValidationData(List<ValidationData> models) {

        List<ValidationData> origins = this.validationDataRepository.findByIds(models.stream().map(model -> model.getId()).collect(Collectors.toList()));

        this.validationDataRepository.saveAll(
                origins.stream().map(origin -> origin.updateUserData(
                        models.stream().filter(model -> model.getId().equals(origin.getId())).findFirst().orElse(null))
                ).collect(Collectors.toList()));

        this.validationDataRepository.saveAll(models.stream().filter(model -> model.getId() == null).collect(Collectors.toList()));

        this.validationDataRepository.flush();
        this.validationStore.refresh();
    }

    /**
     * Update from file.
     *
     * @param file the file
     */
    public void updateFromFile(MultipartFile file) {
        ObjectMapper objectMapper = ValidationObjUtil.getDefaultObjectMapper();

        try {
            String jsonStr = new String(file.getBytes(), "UTF-8");
            List<ValidationData> list = objectMapper.readValue(jsonStr, objectMapper.getTypeFactory().constructCollectionType(List.class, ValidationData.class));
            List<ReqUrl> reqUrls = ValidationReqUrlUtil.getUrlListFromValidationDatas(list);
            reqUrls.forEach(reqUrl -> {
                this.deleteValidationData(reqUrl);
            });
            Map<Long, Long> idsMap = new HashMap<>();
            List<ValidationData> saveList = new ArrayList<>();

            list.forEach(data -> {
                long oldId = data.getId();
                data.setId(null);
                data = this.validationDataRepository.save(data);
                idsMap.put(oldId, data.getId());
                saveList.add(data);
            });

            saveList.forEach(data -> {
                if (data.getParentId() != null) {
                    data.setParentId(idsMap.get(data.getParentId()));
                    this.validationDataRepository.save(data);
                }
            });

        } catch (IOException e) {
            log.info("file io exception : " + e.getMessage());
        }
    }

    /**
     * Update from files.
     *
     * @param files the files
     */
    public void updateFromFiles(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            this.updateFromFile(file);
        }
    }

    @Override
    public void updateValidationData(MultipartHttpServletRequest req) {
        req.getMultiFileMap().entrySet().forEach(entry -> {
            List<MultipartFile> files = entry.getValue();
            this.updateFromFiles(files);
        });

        this.validationDataRepository.flush();
        this.validationStore.refresh();
    }

    @Override
    public void deleteValidationData(List<ValidationData> models) {
        if (models == null || models.isEmpty()) {
            return;
        }

        this.validationDataRepository.deleteAll(models);

        this.validationDataRepository.flush();
        this.validationStore.refresh();
    }

    @Override
    public void deleteAll() {
        this.validationDataRepository.deleteAll(this.validationDataRepository.findAll());
        this.validationDataRepository.flush();
        this.validationStore.refresh();
    }

    @Override
    public void deleteValidationData(ReqUrl url) {
        this.validationDataRepository.deleteAll(this.validationDataRepository.findByMethodAndUrl(url.getMethod(), url.getUrl()));

        this.validationDataRepository.flush();
        this.validationStore.refresh();

    }


}
