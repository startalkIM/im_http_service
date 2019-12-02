package com.qunar.qchat.service;

import com.qunar.qchat.dao.IQtalkConfigDao;
import com.qunar.qchat.dao.model.QtalkConfigModel;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.utils.JacksonUtils;
import com.qunar.qchat.utils.JsonResultUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QtalkConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger(QtalkConfigService.class);


    @Resource
    private IQtalkConfigDao qtalkConfigDao;

    public JsonResult<?> insertConfig(Map<String, String> stringMap) {
//        List<QtalkConfigModel> configModels = new ArrayList<>();
        stringMap.entrySet().stream().forEach(entry ->
                qtalkConfigDao.insertOrUpdateConfig(new QtalkConfigModel(entry.getKey(), entry.getValue())));

//        int insertResult = qtalkConfigDao.insertConfigs(configModels);
        LOGGER.info("insertConfig result:success");
        return JsonResultUtils.success();

    }

    public JsonResult<?> selectConfig() {
        List<QtalkConfigModel> configMap = qtalkConfigDao.getConfigMap();
        Map<String, String> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(configMap)) {
            configMap.stream().forEach(qtalkConfigModel -> result.put(qtalkConfigModel.getConfigKey(), qtalkConfigModel.getConfigValue()));
        }

        LOGGER.info("selectConfig result:{}", JacksonUtils.obj2String(configMap));
        return JsonResultUtils.success(result);

    }
    public JsonResult<?> insertOrUpdateConfig(List<QtalkConfigModel> list) {
        list.stream().forEach(qtalkConfigModel -> qtalkConfigDao.insertOrUpdateConfig(qtalkConfigModel));
//        int updateConfig = qtalkConfigDao.insertOrUpdateConfig(list);
        //LOGGER.info("insertOrUpdateConfig result:{}", updateConfig);
        return JsonResultUtils.success();

    }
}
