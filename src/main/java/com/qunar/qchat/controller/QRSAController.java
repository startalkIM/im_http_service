package com.qunar.qchat.controller;

import com.google.common.collect.Maps;
import com.qunar.qchat.constants.Config;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.utils.JsonResultUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("/newapi/nck/rsa/")
@RestController
public class QRSAController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QRSAController.class);

    @RequestMapping(value = "/get_public_key.do")
    @ResponseBody
    public JsonResult<?> getPublicKey(
            @RequestParam(value = "p", required = false, defaultValue = "default") String p,
            HttpServletRequest request) {
        Map<String, String> data = Maps.newHashMap();

        data.put("rsa_pub_key_fullkey", Config.getProperty("rsa_pub_key_fullkey"));
        data.put("rsa_pub_key_shortkey",  Config.getProperty("rsa_pub_key_shortkey"));

        data.put("pub_key_fullkey",  Config.getProperty("pub_key_fullkey"));
        data.put("pub_key_shortkey",  Config.getProperty("pub_key_shortkey"));

        return JsonResultUtils.success(data);

    }
}
