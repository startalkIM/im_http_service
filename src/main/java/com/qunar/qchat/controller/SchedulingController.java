package com.qunar.qchat.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qunar.qchat.constants.Config;
import com.qunar.qchat.dao.SchedulingInfoDao;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.model.MeetingParam;
import com.qunar.qchat.model.SchedulingInfo;
import com.qunar.qchat.model.SchedulingJson;
import com.qunar.qchat.utils.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RequestMapping("/newapi/scheduling/")
@RestController
public class SchedulingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulingController.class);

    @Autowired
    private SchedulingInfoDao schedulingInfoDao;

    @RequestMapping(value = "/get_update_list.qunar", method = RequestMethod.POST)
    public JsonResult<?> schedulingList(@RequestBody String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> mmap = mapper.readValue(json, Map.class);

            String update_time = mmap.get("updateTime");
            String user = mmap.get("userName");
            return JsonResultUtils.success(getSchedulingList(user, update_time));
        } catch (Exception e) {
            LOGGER.error("got a error {}", e);
            return JsonResultUtils.fail(1, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }

    @RequestMapping(value = "/get_scheduling_conflict.qunar", method = RequestMethod.POST)
    public JsonResult<?> schedulingConflict(@RequestBody String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> mmap = mapper.readValue(json, Map.class);

            String beginTime = mmap.get("beginTime");
            String endTime = mmap.get("endTime");
            String user = mmap.get("checkId");

            List<SchedulingInfo>  schedulinginfoList = new ArrayList<>();

            schedulinginfoList = schedulingInfoDao.selecSchedulingConflict(user, PgTimeUtils.getCorrectTime(beginTime), PgTimeUtils.getCorrectTime(endTime));

            Map<String, Boolean> resultData = new HashMap<>();
            if(schedulinginfoList.size() == 0) {
                resultData.put("isConform", false);
            } else {
                resultData.put("isConform", true);
            }
            return JsonResultUtils.success(resultData);
        } catch (Exception e) {
            LOGGER.error("got a error {}", e);
            return JsonResultUtils.fail(1, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }

    @RequestMapping(value = "/reserve_scheduling.qunar", method = RequestMethod.POST)
    public JsonResult<?> reserveScheduling(@RequestBody String json) {
        try {
            LOGGER.info("the request is {}", json);
            ObjectMapper mapper = new ObjectMapper();
            SchedulingJson schedulingJson = mapper.readValue(json, SchedulingJson.class);

            String type = schedulingJson.getTripType();

            if(type.equals("2")){
                return doReserveScheduling(schedulingJson);
            } else {
                return JsonResultUtils.fail(1, "tripType is wrong");
            }
        } catch (Exception e) {
            LOGGER.error("got a error {}", e);
            return JsonResultUtils.fail(1, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }


    private JsonResult<?> doReserveScheduling(SchedulingJson schedulingJson) {
        String operateType = schedulingJson.getOperateType();
        if(operateType.equals("1")) {
            return doReserveSchedulingInsert(schedulingJson);
        } else if (operateType.equals("2")) {
            return doReserveSchedulingUpdate(schedulingJson);
        } else if(operateType.equals("3")) {
            return doReserveSchedulingDelete(schedulingJson);
        } else {
            return JsonResultUtils.fail(1, "operateType is wrong");
        }
    }

    private JsonResult<?> doReserveSchedulingInsert(SchedulingJson schedulingJson) {
        String update_time = schedulingJson.getUpdateTime();

        String operateType = schedulingJson.getOperateType();
        if(operateType.equals("1")) {
            String tripId = getUUID();
            schedulingJson.setTripId(tripId);
        }
        List<SchedulingInfo>  schedulinginfoList = new ArrayList<>();

        for(SchedulingJson.MemberListEntity mem: schedulingJson.getMemberList()) {
            SchedulingInfo schedulingInfo = new SchedulingInfo();
            schedulingInfo.setScheduling_id(schedulingJson.getTripId());
            schedulingInfo.setScheduling_name(schedulingJson.getTripName());
            schedulingInfo.setScheduling_remarks("");
            schedulingInfo.setScheduling_intr(schedulingJson.getTripIntr());
            schedulingInfo.setScheduling_appointment(schedulingJson.getAppointment());
            schedulingInfo.setScheduling_locale(schedulingJson.getTripLocale());
            schedulingInfo.setScheduling_locale_id(schedulingJson.getTripLocaleNumber());
            schedulingInfo.setScheduling_room(schedulingJson.getTripRoom());
            schedulingInfo.setScheduling_room_id(schedulingJson.getTripRoomNumber());
            schedulingInfo.setSchedule_time(schedulingJson.getScheduleTime());
            schedulingInfo.setScheduling_date(schedulingJson.getTripDate());
            schedulingInfo.setBegin_time(schedulingJson.getBeginTime());
            schedulingInfo.setEnd_time(schedulingJson.getEndTime());
            schedulingInfo.setInviter(schedulingJson.getTripInviter());
            schedulingInfo.setScheduling_type(schedulingJson.getTripType());
            schedulingInfo.setMember(mem.getMemberId());

            schedulinginfoList.add(schedulingInfo);
        }

        if(schedulingInfoDao.insertSchedulingInfo(schedulinginfoList) == schedulinginfoList.size()) {
            sendMessage(schedulingJson, "您收到一个行程邀请");
            return JsonResultUtils.success(getSchedulingList(schedulingJson, schedulingJson.getTripInviter(), update_time));
        } else {
            return JsonResultUtils.fail(1, "operate fail");
        }
    }

    private JsonResult<?> doReserveSchedulingUpdate(SchedulingJson schedulingJson) {
        schedulingInfoDao.deleteSchedulingInfo(schedulingJson.getTripId());
        return doReserveSchedulingInsert(schedulingJson);
    }

    private JsonResult<?> doReserveSchedulingDelete(SchedulingJson schedulingJson) {
        String update_time = schedulingJson.getUpdateTime();
        schedulingInfoDao.cancelSchedulingInfo(schedulingJson.getTripId());
        sendMessage(schedulingJson, "您的会议行程已取消");
        return JsonResultUtils.success(getSchedulingList(schedulingJson, schedulingJson.getTripInviter(), update_time));
    }

    private Map<String, Object> getSchedulingList(String user, String update_time) {
        String newUpdatetime =update_time;
        List<SchedulingInfo> schedulinginfoList = new ArrayList<>();
        schedulinginfoList = schedulingInfoDao.selecSchedulingList(user, PgTimeUtils.getCorrectTime(update_time));

        Map<String, SchedulingJson>  resultMap = new HashMap<String, SchedulingJson>();
        for(SchedulingInfo schedulingInfo: schedulinginfoList) {
            if(resultMap.containsKey(schedulingInfo.getScheduling_id())) {
                SchedulingJson schedulingJson = resultMap.get(schedulingInfo.getScheduling_id());
                List<SchedulingJson.MemberListEntity> memberListEntities = schedulingJson.getMemberList();

                SchedulingJson.MemberListEntity entity = new SchedulingJson.MemberListEntity();
                entity.setMemberId(schedulingInfo.getMember());
                entity.setMemberState(schedulingInfo.getMem_action());
                entity.setMemberStateDescribe(schedulingInfo.getAction_remark());

                memberListEntities.add(entity);
            } else {
                SchedulingJson schedulingJson = new SchedulingJson();
                schedulingJson.setTripId(schedulingInfo.getScheduling_id());
                schedulingJson.setBeginTime(schedulingInfo.getBegin_time());
                schedulingJson.setEndTime(schedulingInfo.getEnd_time());
                schedulingJson.setScheduleTime(schedulingInfo.getSchedule_time());
                schedulingJson.setTripType(schedulingInfo.getScheduling_type());
                schedulingJson.setAppointment(schedulingInfo.getScheduling_appointment());
                schedulingJson.setTripDate(schedulingInfo.getScheduling_date());
                schedulingJson.setTripIntr(schedulingInfo.getScheduling_intr());
                schedulingJson.setTripInviter(schedulingInfo.getInviter());
                schedulingJson.setTripLocale(schedulingInfo.getScheduling_locale());
                schedulingJson.setTripLocaleNumber(schedulingInfo.getScheduling_locale_id());
                schedulingJson.setTripName(schedulingInfo.getScheduling_name());
                schedulingJson.setTripRemark(schedulingInfo.getScheduling_remarks());
                schedulingJson.setTripRoom(schedulingInfo.getScheduling_room());
                schedulingJson.setTripRoomNumber(schedulingInfo.getScheduling_room_id());
                schedulingJson.setCanceled(schedulingInfo.isCanceled());

                List<SchedulingJson.MemberListEntity> memberListEntities = new ArrayList<>();

                SchedulingJson.MemberListEntity entity = new SchedulingJson.MemberListEntity();
                entity.setMemberId(schedulingInfo.getMember());
                entity.setMemberState(schedulingInfo.getMem_action());
                entity.setMemberStateDescribe(schedulingInfo.getAction_remark());

                memberListEntities.add(entity);
                schedulingJson.setMemberList(memberListEntities);

                resultMap.put(schedulingInfo.getScheduling_id(), schedulingJson);
            }
            newUpdatetime = schedulingInfo.getUpdate_time();
        }

        Collection<SchedulingJson> schedulingJsonList = resultMap.values();

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("trips", schedulingJsonList);

        if(!newUpdatetime.equals(update_time)) {
            newUpdatetime = Long.toString((long) (Double.parseDouble(newUpdatetime) * 1000));
        }

        resultData.put("updateTime", newUpdatetime);


        return resultData;
    }

    private Map<String, Object> getSchedulingList(SchedulingJson schedulingJson, String user, String update_time) {
        Map<String, Object> resultData = getSchedulingList(user, update_time);
        String updateTime = resultData.get("updateTime").toString();
        sendAllNotify(schedulingJson, updateTime);
        return resultData;
    }

        private static void sendMessage(SchedulingJson schedulingJson, String title) {
        String from = NoticeMessage.appendQCDomain(Config.getProperty("meeting_robot"));
        String url = Config.getProperty("push_message_notice_url");
        String bcdata = "您收到一个行程邀请消息，请升级客户端查看";
        String jumpUrl = Config.getProperty("meeting_url");

        for (SchedulingJson.MemberListEntity mem: schedulingJson.getMemberList()) {
            String to = NoticeMessage.appendQCDomain(mem.getMemberId());

            Map<String,Object> minviter = new HashMap<>();
            minviter.put("行程发起人", schedulingJson.getTripInviter());
            Map<String,Object> mname = new HashMap<>();
            mname.put("行程名称", schedulingJson.getTripRoom());
            //keyValues.put("行程日期", meeting_date);
            Map<String,Object> mbegin = new HashMap<>();
            mbegin.put("开始时间", schedulingJson.getBeginTime());
            Map<String,Object> mend = new HashMap<>();
            mend.put("结束时间", schedulingJson.getEndTime());
            Map<String,Object> mplace = new HashMap<>();
            mplace.put("行程地点", schedulingJson.getTripLocale() + schedulingJson.getTripRoom());
            Map<String,Object> mremark = new HashMap<>();
            mremark.put("备注", schedulingJson.getTripRemark());

            ArrayList<Map> keyvalues = new ArrayList<Map>();
            keyvalues.add(minviter);
            keyvalues.add(mname);
            keyvalues.add(mbegin);
            keyvalues.add(mend);
            keyvalues.add(mplace);
            keyvalues.add(mremark);

            Map<String, Object> params = new HashMap<>();
            params.put("id", schedulingJson.getTripId());

            Map<String, Object> extendMap = new HashMap<>();
            extendMap.put("keyValues", keyvalues);
            extendMap.put("params", params);
            extendMap.put("title", title);
            extendMap.put("url", jumpUrl);

            String extendInfo = JacksonUtils.obj2String(extendMap);

            String message = NoticeMessage.makeInviteMessage(from, to, bcdata, extendInfo);

            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("from",from);
            paramsMap.put("to",to);
            paramsMap.put("message",message);
            String json = JacksonUtils.obj2String(paramsMap);

            LOGGER.info("the url is {}, the from is {}, the to is {}, the message is {}", url, from, to, message);
            String response = HttpClientUtils.postJson(url, json);
            LOGGER.info("the response is {}", response);
        }
    }

    private static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "");
    }

    private void sendAllNotify(SchedulingJson schedulingJson, String updateTime) {
        for(SchedulingJson.MemberListEntity mem: schedulingJson.getMemberList()) {
            sendNotify(mem.getMemberId(), "8", updateTime);
        }
    }
    /**
     * 发送通知给客户端同步个人配置
     * @param userid
     * @param category
     * @param updateTime
     */
    private void sendNotify(String userid, String category, String updateTime) {
        Dictionary<String, Object> args = new Hashtable<>();
        args.put("from", userid);
        args.put("to", userid);
        args.put("category", category);

        Dictionary<String, Object> data = new Hashtable<>();
        data.put("updateTime", updateTime);

        args.put("data", JSON.toJSONString(data));

        String url = Config.getProperty("url_send_notify");
        String ret = HttpClientUtils.postJson(url, JSON.toJSONString(args));
        LOGGER.info("send presense to :{}, ret;{}", JSON.toJSONString(args), ret);
    }

}
