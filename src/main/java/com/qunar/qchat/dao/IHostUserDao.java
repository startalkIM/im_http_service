package com.qunar.qchat.dao;

import com.qunar.qchat.dao.model.HostUserModel;
import com.qunar.qchat.dao.model.UserInfoQtalk;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IHostUserDao {

    List<HostUserModel> selectIncrementByVersion(@Param("table") String table,
                                                 @Param("version") Integer version,
                                                 @Param("hostId") Integer hostId);

    Integer selectMaxVersion(@Param("table") String table);
    List<UserInfoQtalk> selectOnJobUserFromHostUser(@Param("hostID") Integer hostId);

    void insertUser(@Param("UserInfoQtalk") UserInfoQtalk userInfoQtalk);

    void updateHostUserHireType(@Param("UserInfoQtalk") UserInfoQtalk userInfoQtalk);


    int updateHostUser(@Param("UserInfoQtalk") UserInfoQtalk userInfoQtalk);
    int deleteHostUsers();

    Integer insertHostUser(HostUserModel hostUserModel);

    List<HostUserModel> selectByHostAndUserId(@Param("hostId") Integer hostId, @Param("userId") String userId);

    Integer searchHostUsersCount(@Param("hostId") Integer hostId,
                                 @Param("approveFlag") Integer approveFlag,
                                 @Param("keyword") String keyword);

    List<HostUserModel> searchHostUsers(@Param("hostId") Integer hostId,
                                        @Param("approveFlag") Integer approveFlag,
                                        @Param("keyword") String keyword,
                                        @Param("limit") Integer limit,
                                        @Param("offset") Integer offset);

    Integer updateApproveStatus(@Param("ids") List<Integer> ids,
                                @Param("status") Integer status,
                                @Param("hostId") Integer hostId);

    Integer selectApproveStatus(@Param("userId") String userId,
                                @Param("hostId") Integer hostId);

    Integer selectAdminFlagByUserId(@Param("userId")String userId,
                                    @Param("hostId")Integer hostId);

    Integer selectCountFireUserByUserId(@Param("userId") String userId, @Param("hostId") Integer hostId);

    Integer deleteFireUserByUserId(@Param("userId") String userId, @Param("hostId") Integer hostId, @Param("host") String host);
}
