package com.qunar.qchat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.model.request.CheckConfigRequest;
import com.qunar.qchat.model.result.CheckConfigResult;
import com.qunar.qchat.utils.JsonResultUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RequestMapping("/newapi/config/")
@RestController
public class QCheckConfigController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QBaseController.class);

    @RequestMapping(value = "/check_config.qunar", method = RequestMethod.POST)
    public JsonResult<?> checkConfig(@RequestBody CheckConfigRequest request) {
        try {
            String ver = request.getVer();
            String p = request.getP();
            Integer v = request.getV();
            String cv = request.getCv();
            if(Strings.isNullOrEmpty(cv)||Strings.isNullOrEmpty(ver)||Strings.isNullOrEmpty(p)||v == null){
                return JsonResultUtils.fail(0, " 参数缺失！" );
            }
            String Configfile = p + ver + ".json";
            Configfile = Configfile.toLowerCase();
            System.out.println(Configfile);
            CheckConfigResult result = genQtalkConfig(Configfile);
            return JsonResultUtils.success(result);
        } catch (Exception e) {
            LOGGER.error("catch error ", e);
            return JsonResultUtils.fail(0, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }


    @RequestMapping(value = "/t", method = RequestMethod.POST)
    public JsonResult<?> checkConfigIOS(@RequestBody CheckConfigRequest request) {
        try {
            String ver = request.getVer();
            String p = request.getP();
            Integer v = request.getV();
            String cv = request.getCv();
            if(Strings.isNullOrEmpty(cv)||Strings.isNullOrEmpty(ver)||Strings.isNullOrEmpty(p)||v == null){
                return JsonResultUtils.fail(0, " 参数缺失！" );
            }
            String Configfile = p + ver + ".json";
            Configfile = Configfile.toLowerCase();
            System.out.println(Configfile);
            CheckConfigResult result = genQtalkConfig(Configfile);
            return JsonResultUtils.success(result);
        } catch (Exception e) {
            LOGGER.error("catch error ", e);
            return JsonResultUtils.fail(0, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }

    private CheckConfigResult genQtalkConfig(String configName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(configName);
        InputStream read = classPathResource.getInputStream();
        String config = new String(ByteStreams.toByteArray(read));
        ObjectMapper mapper = new ObjectMapper();
        CheckConfigResult params = mapper.readValue(config, CheckConfigResult.class);
        return params;
    }

}
