package com.qunar.qchat.dao;

import com.qunar.qchat.dao.model.ClientConfigModel;
import com.qunar.qchat.model.request.SetClientConfigRequst;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public interface IClentConfigSyncDao {

    public long selectMaxVersion(
            @Param("table") String table,
            @Param("username") String username,
            @Param("host") String host
    );

    public List<ClientConfigModel> selectIncrementConfigData(
            @Param("table") String table,
            @Param("username") String username,
            @Param("host") String host,
            @Param("version") long version
    );

    public int insertClientConfig(
            @Param("table") String table,
            @Param("username") String username,
            @Param("host") String host,
            @Param("key") String key,
            @Param("subkey") String subkey,
            @Param("value") String value,
            @Param("operate_plat") String operate_plat,
            @Param("version") long version
    );

    public int insertBatchClientConfig(
            @Param("table") String table,
            @Param("username") String username,
            @Param("host") String host,
            @Param("datas") Set<SetClientConfigRequst.BatchProcess> datas,
            @Param("operate_plat") String operate_plat,
            @Param("version") long version);

    public int updateClientConfig(
            @Param("table") String table,
            @Param("username") String username,
            @Param("host") String host,
            @Param("key") String key,
            @Param("subkey") String subkey,
            @Param("value") String value,
            @Param("operate_plat") String operate_plat,
            @Param("isdel") int isdel,
            @Param("version") long version
    );
    public int updateBatchClientConfig(
            @Param("table") String table,
            @Param("username") String username,
            @Param("host") String host,
            @Param("datas") Set<SetClientConfigRequst.BatchProcess> datas,
            @Param("isdel") int isdel,
            @Param("version") long version,
            @Param("operate_plat") String operate_plat);

}