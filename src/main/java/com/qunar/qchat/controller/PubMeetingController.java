package com.qunar.qchat.controller;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qunar.qchat.constants.Config;
import com.qunar.qchat.dao.SchedulingInfoDao;
import com.qunar.qchat.model.JsonResult;
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

import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/check/meeting/")
@RestController
public class PubMeetingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PubMeetingController.class);

    @Autowired
    private SchedulingInfoDao schedulingInfoDao;

    @RequestMapping(value = "/meeting_action.qunar", method = RequestMethod.POST)
    public JsonResult<?> meetingAction(@RequestBody String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);

            String username = NoticeMessage.appendQCDomain(StrUtils.getStringValue(map.get("username")));
            String meeting_id = StrUtils.getStringValue(map.get("meeting_id"));
            String action = StrUtils.getStringValue(map.get("action"));
            String action_reason = StrUtils.getStringValue(map.get("action_reason"));
            Map<String, Object> result = getMeetingInfo(meeting_id, username);

            String end_time = result.get("end_time").toString();
            String str = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(str, Locale.CHINA);
            Date date = sdf.parse(end_time);
            long timestamp = date.getTime();
            long curTime = new Date().getTime();

            if (curTime >= timestamp) {
                LOGGER.info("设置已结束的会议：{}", json);
                return JsonResultUtils.fail(1, "会议已结束:\n ");
            }

            int res = schedulingInfoDao.changeSchedulingAction(username, meeting_id, action, action_reason);
            List<SchedulingInfo> res1 = schedulingInfoDao.changeSchedulingUpdatetime(meeting_id);

            if (res == 1 && res1.size() != 0) {
                String from = NoticeMessage.appendQCDomain(Config.getProperty("meeting_robot"));
                String to = NoticeMessage.appendQCDomain(result.get("inviter").toString());
                String bcdata = "";
                if (action.equals("1")) {
                    bcdata = "同意";
                } else if (action.equals("2")) {
                    bcdata = "拒绝";
                } else {
                    bcdata = "待定";
                }

                bcdata = username + bcdata + "了\"" + result.get("meeting_name") + "\"的会议邀请，请知晓。";
                String message = NoticeMessage.makeNoticeMessage(from, to, bcdata, "");

                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("from",from);
                paramsMap.put("to",to);
                paramsMap.put("message",message);
                paramsMap.put("system", Config.getProperty("appcode"));
                String pjson = JacksonUtils.obj2String(paramsMap);

                String url = Config.getProperty("url_send_qtalk_message");
                LOGGER.info("the url is {}, the from is {}, the to is {}, the message is {}", url, from, to, message);
                String response = HttpClientUtils.postJson(url, pjson);
                LOGGER.info("the response is {}", response);

                sendAllNotify(res1);
                return JsonResultUtils.success("");
            } else {
                return JsonResultUtils.fail(1, "操作失败:\n ");
            }
        } catch (Exception e) {
            LOGGER.error("the error is {}", ExceptionUtils.getStackTrace(e));
            return JsonResultUtils.fail(1, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }

    @RequestMapping(value = "/meeting_info.qunar", method = RequestMethod.POST)
    public JsonResult<?> meetingInfo(@RequestBody String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);

            String username = NoticeMessage.appendQCDomain(StrUtils.getStringValue(map.get("username")));
            String meeting_id = StrUtils.getStringValue(map.get("meeting_id"));

            Map<String, Object> result = getMeetingInfo(meeting_id, username);

            LOGGER.info("the meeting info is {}", result.toString());
            return JsonResultUtils.success(result);
        } catch (Exception e) {
            LOGGER.error("the error is {}", ExceptionUtils.getStackTrace(e));
            return JsonResultUtils.fail(1, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }

    private Map<String, Object> getMeetingInfo(String meetingid, String username) {
        List<SchedulingInfo> res = schedulingInfoDao.selecSchedulingInfo(meetingid);

        Map<String, Object> result = new HashMap<>();
        List<String> members = new ArrayList<>();

        for (SchedulingInfo member : res) {
            if (member.getMember().equals(username)) {
                result.put("meeting_id", member.getScheduling_id());
                result.put("meeting_type", member.getScheduling_type());
                result.put("meeting_name", member.getScheduling_name());
                result.put("meeting_remarks", member.getScheduling_remarks());
                result.put("meeting_intr", member.getScheduling_intr());
                result.put("meeting_locale", member.getScheduling_locale());
                result.put("meeting_room", member.getScheduling_room());
                result.put("schedule_time", member.getSchedule_time());
                result.put("meeting_date", member.getScheduling_date());
                result.put("begin_time", member.getBegin_time());
                result.put("end_time", member.getEnd_time());
                result.put("inviter", member.getInviter());
                result.put("mem_action", member.getMem_action());
                result.put("remind_flag", member.getRemind_flag());
                result.put("action_reason", member.getAction_remark());
                result.put("canceled", member.isCanceled());
            }

            members.add(member.getMember());
        }

        result.put("member", members);

        return result;
    }


    private void sendAllNotify(List<SchedulingInfo> schedulingInfoList) {
        for(SchedulingInfo schedulingInfo: schedulingInfoList) {
            sendNotify(schedulingInfo.getMember(), "8", Long.toString((long) (Double.parseDouble(schedulingInfo.getUpdate_time()) * 1000)));
        }
    }
    /**
     * 发送通知给客户端同步个人配置
     * @param userid
     */
    private void sendNotify(String userid, String category, String updateTime) {
        Dictionary<String, Object> args = new Hashtable<>();
        args.put("from", userid);
        args.put("to", userid);
        args.put("category", category);

        Dictionary<String, Object> data = new Hashtable<>();
        data.put("updateTime", updateTime);

        args.put("data", JSON.toJSONString(data));

        String url = Config.getProperty("url_send_qtalk_notify");
        String ret = HttpClientUtils.postJson(url, JSON.toJSONString(args));
        LOGGER.info("send presense to :{}, ret;{}", JSON.toJSONString(args), ret);
    }
}
