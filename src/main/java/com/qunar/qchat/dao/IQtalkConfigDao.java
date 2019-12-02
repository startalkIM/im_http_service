package com.qunar.qchat.dao;

import com.qunar.qchat.dao.model.QtalkConfigModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public interface IQtalkConfigDao {
    List<QtalkConfigModel> getConfigMap();

    int insertConfigs(List<QtalkConfigModel> list);

    int insertOrUpdateConfig(QtalkConfigModel qtalkConfigModel);
    int deleteConfig();

}
