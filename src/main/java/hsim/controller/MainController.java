package hsim.controller;

import hsim.checkpoint.core.annotation.ValidationBody;
import hsim.checkpoint.core.annotation.ValidationParam;
import hsim.checkpoint.core.annotation.ValidationUrlMapping;
import hsim.model.CommonReqModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The type Main controller.
 */
@Slf4j
@RestController
@RequestMapping("/main")
public class MainController {

    /**
     * Main controllerbcdeg common req model.
     *
     * @param paramModel the param model
     * @param reqModel   the req model
     * @param req        the req
     * @param res        the res
     * @return the common req model
     */
    @PostMapping("/urlmap/**")
    @ValidationUrlMapping
    public CommonReqModel mainControllerbcdeg(@ValidationParam CommonReqModel paramModel, @ValidationBody CommonReqModel reqModel, HttpServletRequest req, HttpServletResponse res) {
        log.info(reqModel.toString());
        log.info(paramModel.toString());
        return reqModel;
    }

    /**
     * Main contrbba common req model.
     *
     * @param paramModel the param model
     * @param reqModel   the req model
     * @param req        the req
     * @param res        the res
     * @return the common req model
     */
    @PostMapping("/abcdb")
    public CommonReqModel mainContrbba(@ValidationParam CommonReqModel paramModel, @ValidationBody CommonReqModel reqModel, HttpServletRequest req, HttpServletResponse res) {
        log.info(reqModel.toString());
        log.info(paramModel.toString());
        return reqModel;
    }


    /**
     * Main controllera common req model.
     *
     * @param paramModel the param model
     * @param reqModel   the req model
     * @param req        the req
     * @param res        the res
     * @return the common req model
     */
    @PostMapping("/bcd")
    public CommonReqModel mainControllera(@ValidationParam CommonReqModel paramModel, @ValidationBody CommonReqModel reqModel, HttpServletRequest req, HttpServletResponse res) {
        log.info(reqModel.toString());
        log.info(paramModel.toString());
        return reqModel;
    }
}
