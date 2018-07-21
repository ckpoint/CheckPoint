package hsim.checkpoint.setting.controller;

import hsim.checkpoint.config.ValidationConfig;
import hsim.checkpoint.config.ValidationIntercepterConfig;
import hsim.checkpoint.core.component.ComponentMap;
import hsim.checkpoint.core.domain.ReqUrl;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.core.msg.MsgSaver;
import hsim.checkpoint.exception.resolver.ValidationExceptionResolver;
import hsim.checkpoint.init.InitCheckPoint;
import hsim.checkpoint.setting.service.MsgExcelService;
import hsim.checkpoint.setting.service.MsgExcelServiceImpl;
import hsim.checkpoint.setting.service.MsgSettingService;
import hsim.checkpoint.setting.service.MsgSettingServiceImpl;
import hsim.checkpoint.setting.session.ValidationLoginInfo;
import hsim.checkpoint.setting.session.ValidationSessionComponent;
import hsim.checkpoint.setting.session.ValidationSessionInfo;
import hsim.checkpoint.util.ParameterMapper;
import hsim.checkpoint.util.ValidationFileUtil;
import hsim.checkpoint.util.excel.PoiWorkBook;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.List;

/**
 * The type Msg setting controller.
 */
@CrossOrigin(origins = "*")
public class MsgSettingController {

    private MsgSettingService msgSettingService = ComponentMap.get(MsgSettingServiceImpl.class);
    private MsgExcelService msgExcelService = ComponentMap.get(MsgExcelServiceImpl.class);
    private ValidationSessionComponent validationSessionComponent = ComponentMap.get(ValidationSessionComponent.class);
    private MsgSaver msgSaver = ComponentMap.get(MsgSaver.class);


    /**
     * Login validation session info.
     *
     * @param info the info
     * @param req  the req
     * @param res  the res
     * @return the validation session info
     */
    @PostMapping("/setting/auth")
    public ValidationSessionInfo login(@RequestBody ValidationLoginInfo info, HttpServletRequest req, HttpServletResponse res) {
        return this.validationSessionComponent.checkAuth(info, req, res);
    }

    /**
     * Annotation scan.
     *
     * @param maxdeeplevel the maxdeeplevel
     * @param req          the req
     * @param res          the res
     */
    @GetMapping("/setting/scan/{maxdeeplevel}")
    public void annotationScan(@PathVariable int maxdeeplevel, HttpServletRequest req, HttpServletResponse res) {
        this.validationSessionComponent.sessionCheck(req);
        this.msgSaver.annotationScan(maxdeeplevel);
    }
    /**
     * Download api json all.
     *
     * @param req the req
     * @param res the res
     */
    @GetMapping("/setting/download/api/json/all")
    public void downloadApiJsonAll(HttpServletRequest req, HttpServletResponse res) {
        this.validationSessionComponent.sessionCheck(req);
        List<ValidationData> list = this.msgSettingService.getAllValidationData();
        ValidationFileUtil.sendFileToHttpServiceResponse("validation.json", list, res);
    }

    /**
     * Download api json.
     *
     * @param req    the req
     * @param method the method
     * @param url    the url
     * @param res    the res
     */
    @GetMapping("/setting/download/api/json")
    public void downloadApiJson(HttpServletRequest req, @RequestParam("method") String method, @RequestParam("url") String url, HttpServletResponse res) {
        this.validationSessionComponent.sessionCheck(req);
        url = new String(Base64.getDecoder().decode(url));
        List<ValidationData> list = this.msgSettingService.getValidationData(method, url);
        ValidationFileUtil.sendFileToHttpServiceResponse(method + url.replaceAll("/", "-") + ".json", list, res);
    }

    /**
     * Download api all.
     *
     * @param req the req
     * @param res the res
     */
    @GetMapping("/setting/download/api/excel/all")
    public void downloadApiAll(HttpServletRequest req, HttpServletResponse res) {
        this.validationSessionComponent.sessionCheck(req);
        PoiWorkBook workBook = this.msgExcelService.getAllExcels();
        workBook.writeFile("ValidationApis_" + System.currentTimeMillis(), res);
    }

    /**
     * Download api.
     *
     * @param req    the req
     * @param method the method
     * @param url    the url
     * @param res    the res
     */
    @GetMapping("/setting/download/api/excel")
    public void downloadApi(HttpServletRequest req, @RequestParam("method") String method, @RequestParam("url") String url, HttpServletResponse res) {
        this.validationSessionComponent.sessionCheck(req);
        url = new String(Base64.getDecoder().decode(url));
        PoiWorkBook workBook = this.msgExcelService.getExcel(method, url);
        workBook.writeFile("ValidationApis_" + System.currentTimeMillis(), res);
    }

    /**
     * Upload setting.
     *
     * @param req the req
     */
    @PostMapping("/setting/upload/json")
    public void uploadSetting(HttpServletRequest req) {
        this.validationSessionComponent.sessionCheck(req);
        this.msgSettingService.updateValidationData((MultipartHttpServletRequest) req);
    }


    /**
     * Req url all list list.
     *
     * @param req the req
     * @return the list
     */
    @GetMapping("/setting/url/list/all")
    public List<ReqUrl> reqUrlAllList(HttpServletRequest req) {
        this.validationSessionComponent.sessionCheck(req);
        return this.msgSettingService.getAllUrlList();
    }

    /**
     * Gets validation data lists.
     *
     * @param req the req
     * @return the validation data lists
     */
    @GetMapping("/setting/param/from/url")
    public List<ValidationData> getValidationDataLists(HttpServletRequest req) {
        this.validationSessionComponent.sessionCheck(req);
        ValidationData data = ParameterMapper.requestParamaterToObject(req, ValidationData.class, "UTF-8");
        return this.msgSettingService.getValidationData(data.getParamType(), data.getMethod(), data.getUrl());
    }

    /**
     * Update validation data lists.
     *
     * @param req   the req
     * @param datas the datas
     */
    @PostMapping("/setting/update/param/from/url")
    public void updateValidationDataLists(HttpServletRequest req, @RequestBody List<ValidationData> datas) {
        this.validationSessionComponent.sessionCheck(req);
        this.msgSettingService.updateValidationData(datas);
    }

    /**
     * Delete validation data lists.
     *
     * @param req   the req
     * @param datas the datas
     */
    @DeleteMapping("/setting/delete/param/from/url")
    public void deleteValidationDataLists(HttpServletRequest req, @RequestBody List<ValidationData> datas) {
        this.validationSessionComponent.sessionCheck(req);
        this.msgSettingService.deleteValidationData(datas);
    }

    /**
     * Delete validation url.
     *
     * @param req    the req
     * @param reqUrl the req url
     */
    @DeleteMapping("/setting/delete/url")
    public void deleteValidationUrl(HttpServletRequest req, @RequestBody ReqUrl reqUrl) {
        this.validationSessionComponent.sessionCheck(req);
        this.msgSettingService.deleteValidationData(reqUrl);
    }

    /**
     * Delete validation url.
     *
     * @param req    the req
     */
    @DeleteMapping("/setting/delete/all")
    public void cleanValidationDatas(HttpServletRequest req) {
        this.validationSessionComponent.sessionCheck(req);
        this.msgSettingService.deleteAll();
    }


    /**
     * Validation exception resolver validation exception resolver.
     *
     * @return the validation exception resolver
     */
    @Bean
    public ValidationExceptionResolver validationExceptionResolver() {
        return ComponentMap.get(ValidationExceptionResolver.class);
    }

    /**
     * Intercepter config validation intercepter config.
     *
     * @return the validation intercepter config
     */
    @Bean
    public ValidationIntercepterConfig intercepterConfig() {
        return ComponentMap.get(ValidationIntercepterConfig.class);
    }

    /**
     * Validation config validation config.
     *
     * @return the validation config
     */
    @Bean
    public ValidationConfig validationConfig() {
        return ComponentMap.get(ValidationConfig.class);
    }

    /**
     * Init check point init check point.
     *
     * @return the init check point
     */
    @Bean
    public InitCheckPoint initCheckPoint() {
        return ComponentMap.get(InitCheckPoint.class);
    }

}
