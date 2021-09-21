package com.qunar.qchat.dao;

import com.qunar.qchat.dao.model.MucIncrementInfo;
import com.qunar.qchat.dao.model.MucInfoModel;
import com.qunar.qchat.dao.model.MucOptsModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface IMucInfoDao {

    List<MucIncrementInfo> selectMucIncrementInfo(
            @Param("u") String u,
            @Param("d") String d,
            @Param("t") Double t
    );

    List<MucIncrementInfo> selectMucIncrementInfoNew(
            @Param("u") String u,
            @Param("d") String d,
            @Param("t") String t);


    MucInfoModel updateMucInfo(MucInfoModel mucInfo);

    Integer selectMucCountByMucName(@Param("mucName") String mucName);


    List<MucInfoModel> selectMucInfoByIds(@Param("ids") List<String> ids);


    int checkMucExist(@Param("mucName") String mucName);


    Integer insertMucInfo(MucInfoModel mucInfoModel);


    MucInfoModel selectByMucName(@Param("mucName") String mucName);

    List<String> selectMucNamesByUsername(@Param("userName") String userName);

    List<MucInfoModel> selectIncrementMucVCards(@Param("ids") List<String> ids,
                                                @Param("updateTime") Date updateTime);

    List<MucOptsModel> getMucOptsByUserId(@Param("userId") String userName, @Param("userDomain") String domain);

    List<MucOptsModel> getMucOptsWithUserId(@Param("userId") String userName, @Param("userDomain") String domain, @Param("mucId") String mucId, @Param("mucDomain") String mucDomain);


}
