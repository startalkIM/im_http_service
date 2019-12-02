package com.qunar.qchat.dao;

import com.qunar.qchat.model.SchedulingInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by admin on 13/07/2017.
 */
@Component
public interface SchedulingInfoDao {

    public int  insertSchedulingInfo(
            @Param("schedulingInfos") List<SchedulingInfo> schedulingInfos
    );

    public List<SchedulingInfo> selecSchedulingList(
            @Param("user") String user,
            @Param("update_time") Double update_time
    );

    public List<SchedulingInfo> selecSchedulingConflict(
            @Param("user") String user,
            @Param("begin_time") Double begin_time,
            @Param("end_time") Double end_time
    );

    public int deleteSchedulingInfo(
            @Param("scheduling_id") String scheduling_id
    );

    public int cancelSchedulingInfo(
            @Param("scheduling_id") String scheduling_id
    );

    public List<SchedulingInfo> selectPreScheduling(
    );

    public List<SchedulingInfo> selectPostScheduling(
    );

    public int updateRemindFlag(
            @Param("ids") List<Integer> ids,
            @Param("flag") String flag
    );


    public int changeSchedulingAction(
            @Param("username") String username,
            @Param("scheduling_id") String scheduling_id,
            @Param("action") String action,
            @Param("action_reason") String action_reason
    );

    public List<SchedulingInfo> changeSchedulingUpdatetime(
            @Param("scheduling_id") String scheduling_id
    );

    public List<SchedulingInfo> selecSchedulingInfo(
            @Param("scheduling_id") String scheduling_id
    );

}
