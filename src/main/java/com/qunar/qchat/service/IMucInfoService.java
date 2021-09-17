package com.qunar.qchat.service;

import com.qunar.qchat.dao.model.MucInfoModel;
import com.qunar.qchat.dao.model.MucOptsModel;

import java.util.Date;
import java.util.List;

/**
 * @auth dongzd.zhang
 * @Date 2019/5/29 10:33
 */
public interface IMucInfoService {

    /**
     * 增量获取群名片.
     * @param mucNames 群ID列表.
     * @param updateTime 更新时间，查询改时间之后的数据
     * @return  List<MucInfoModel>
     * */
    List<MucInfoModel> getIncrementMucVCards(List<String> mucNames, Date updateTime);


    /**
     * 根据userid查询所属群ID列表.
     * @param userId 用户ID
     * @return List<String>
     * */
    List<String> selectMucNamesByUserId(String userId);


    /**
     * 过滤指定的群ID.
     * @param originMucIds 原始群ID列表.
     * @param host
     * @return List<String>
     * */
    List<String> getEjabHostMucIds(List<String> originMucIds, String host);

    /**
     * 根据userid查询所属群ID列表,之后返回群信息.
     * @param userId 用户ID
     * @return List<String>
     * */
    List<MucOptsModel> getMucOptsByUserId(String userId, String domain);


    /**
     * 根据userid和groupid查询群禁言状态,之后返回群id+群设置.
     * @param userId 用户ID
     * @return List<String>
     * */
    List<MucOptsModel> getMucOptsWithUserId(String userId, String domain, String mucId, String mucDomain);


}
