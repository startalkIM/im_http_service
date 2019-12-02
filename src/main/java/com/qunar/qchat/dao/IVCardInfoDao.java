package com.qunar.qchat.dao;

import com.qunar.qchat.dao.model.VCardInfoModel;
import org.apache.ibatis.annotations.Param;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/5 17:46
 */
public interface IVCardInfoDao {

    /**
     * 根据username和host查询数据条数.
     * @param username
     * @param host
     * @return Integer
     * */
    Integer getCountByUsernameAndHost(@Param("username") String username,
                                      @Param("host") String host);

    /**
     * 查询指定用户的信息.
     * @param username
     * @param host
     * @param version
     * @return VCardInfoModel
     * */
    VCardInfoModel selectByUsernameAndHost(@Param("username") String username,
                                           @Param("host") String host,
                                           @Param("version") Integer version);

    /**
     * 新增名片.
     * @param vCardInfoModel
     * @return
     * */
    Integer insertVCardVersion(VCardInfoModel vCardInfoModel);
}
