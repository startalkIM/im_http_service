package com.qunar.qchat.dao;

import com.qunar.qchat.dao.model.InviteInfoModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IInviteInfoDao {

    List<InviteInfoModel> selectInviteInfo(
            @Param("username") String username,
            @Param("host") String host,
            @Param("time") long time
    );
}