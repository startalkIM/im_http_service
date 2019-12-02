package com.qunar.qchat.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.qunar.qchat.constants.BaseCode;
import com.qunar.qchat.dao.model.QtalkConfigModel;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.model.request.QtalkConfigRequest;
import com.qunar.qchat.service.LdapAdService;
import com.qunar.qchat.service.QtalkConfigService;
import com.qunar.qchat.utils.JacksonUtils;
import com.qunar.qchat.utils.JsonResultUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/newapi/nck/ldap/")
@RestController
public class QtalkConfigController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QtalkConfigController.class);

    @Autowired
    QtalkConfigService qtalkConfigService;
    @Autowired
    LdapAdService ldapAdService;


    @RequestMapping(value = "/saveLdapConfig.qunar", method = RequestMethod.POST)
    public JsonResult<?> saveLdapConfig(@RequestBody QtalkConfigRequest configRequest) {

        try {
            LOGGER.info("saveLdapConfig begin param:{}", JacksonUtils.obj2String(configRequest));
            if (configRequest == null || StringUtils.isNotEmpty(configRequest.check())) {
                return JsonResultUtils.fail(BaseCode.BADREQUEST.getCode(), BaseCode.BADREQUEST.getMsg());
            }
            String json = JacksonUtils.obj2String(configRequest);
            Map<String, String> stringMap = JacksonUtils.string2Obj(json, new TypeReference<Map<String, String>>() {
            });

            ldapAdService.setQtalkConfig(stringMap);
            JsonResult<?> jsonResult = ldapAdService.synchronizeAdUsers(configRequest.isNeedDeleteData(), false, true);
            LOGGER.info("saveLdapConfig synchronizeAdUsers result:{}", jsonResult.getErrmsg());
            if (jsonResult.isRet()) {
                return qtalkConfigService.insertConfig(stringMap);
            } else {
                return jsonResult;
            }
        } catch (Exception e) {
            LOGGER.error("saveLdapConfig error", e);
            return JsonResultUtils.fail(500, "server端错误");
        }

    }

    @RequestMapping(value = "/initUser.qunar", method = RequestMethod.POST)
    public Object synchronizeAdUser(@RequestBody(required = false) String json) {
        LOGGER.info("synchronizeAdUser user begin");
        boolean needDeleteData = false;
        if (StringUtils.isNotEmpty(json) && json.contains("needDeleteData")) {
            JSONObject jsonObject = JSONObject.parseObject(json);
            needDeleteData = jsonObject.getBoolean("needDeleteData");
        }
        return ldapAdService.synchronizeAdUsers(needDeleteData, true, false);
    }

    @RequestMapping(value = "/selectConfig.qunar")
    public JsonResult<?> selectLdapConfig() {
        LOGGER.info("selectLdapConfig begin");
        return qtalkConfigService.selectConfig();
    }

    @RequestMapping(value = "/updateConfig.qunar", method = RequestMethod.POST)
    public JsonResult<?> updateLdapConfig(@RequestBody String json) {
        try {
            LOGGER.info("selectLdapConfig begin");
            List<QtalkConfigModel> configModels = JSONArray.parseArray(json, QtalkConfigModel.class);
            if (CollectionUtils.isEmpty(configModels)) {
                return JsonResultUtils.fail(BaseCode.BADREQUEST.getCode(), BaseCode.BADREQUEST.getMsg());
            }

            return qtalkConfigService.insertOrUpdateConfig(configModels);
        } catch (Exception e) {
            LOGGER.error("updateLdapConfig error", e);
            return JsonResultUtils.fail(BaseCode.ERROR.getCode(), BaseCode.ERROR.getMsg());
        }
    }
}
