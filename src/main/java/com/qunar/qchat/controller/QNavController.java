package com.qunar.qchat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.service.IDomainService;
import com.qunar.qchat.utils.JsonResultUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/newapi/nck/")
@RestController
public class QNavController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QNavController.class);
    @Autowired
    private IDomainService domainService;

    @RequestMapping(value = "/qtalk_nav.qunar", method = RequestMethod.GET)
    public Map publicNav(@RequestParam(value = "c", required = false) String domain, @RequestParam(value = "nauth", required = false) String nauth) {
        try {
            String Configfile = "nav.json";
            ClassPathResource classPathResource = new ClassPathResource(Configfile);
            InputStream read = classPathResource.getInputStream();
            String config = new String(ByteStreams.toByteArray(read));
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(config, Map.class);

            Map baseaddessMap = (Map) map.get("baseaddess");
            if (domain != null) {
                baseaddessMap.put("domain", domain);
            }
            map.put("baseaddess", baseaddessMap);

            Map loginMap = (Map) map.get("Login");
            if (nauth != null && nauth.equals("true")) {
                loginMap.put("loginType", "newpassword");
            }

            Map imConfigMap = (Map) map.get("imConfig");
            Boolean isToCDomain = domainService.isToCDomainWithoutNull(domain);
            imConfigMap.put("isToC", isToCDomain);
            map.put("imConfig", imConfigMap);

            return map;
        } catch (Exception e) {
            LOGGER.error("catch error ", e);
            Map map = new HashMap();
            return map;
        }
    }
}
