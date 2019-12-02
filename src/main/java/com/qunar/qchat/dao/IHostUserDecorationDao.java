package com.qunar.qchat.dao;

import com.qunar.qchat.dao.model.HostUserDecorationModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth dongzd.zhang
 * @Date 2018/12/3 20:24
 */
public interface IHostUserDecorationDao {

    List<HostUserDecorationModel> selectUserDecorations(@Param("userId") String userId,
                                                        @Param("host") String host);

}
