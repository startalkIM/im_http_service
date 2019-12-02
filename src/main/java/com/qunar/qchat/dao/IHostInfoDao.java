package com.qunar.qchat.dao;

import com.qunar.qchat.dao.model.HostInfoModel;
import org.apache.ibatis.annotations.Param;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/6 11:02
 */
public interface IHostInfoDao {


    /**
     * 根据host查询.
     * @param host
     * @return HostInfoModel
     * */
    HostInfoModel selectHostInfoByHostName(@Param("domain") String host);

    /**
     * 选取默认host信息
      * @return
     */
    HostInfoModel selectDefaultHost();


    /**
     * 根据域id查询域类型.
     * @param host
     * @return Integer 0 - toB, 1 - toC
     * */
    Integer selectHostType(@Param("host") String host);

    /**
     * 根据host查询域数量.
     * @param host
     * @return Integer
     * */
    Integer selectHostCountByHost(@Param("host") String host);

    /**
     * 更新host对应的二维码路径.
     * @param qrCodrPath
     * @param host
     * @return Integer
     * */
    Integer updateHostQRCode(@Param("qrCodrPath") String qrCodrPath, @Param("host") String host);

    /**
     * 查询qrCodePath.
     * @param host
     * @return String
     * */
    String selectHostQRCodePath(@Param("host") String host);


    /**
     * 查询域是否开启了注册审批.
     * @param host
     * @return Integer
     * */
    Integer selectNeedApproveByHost(@Param("host") String host);
}
