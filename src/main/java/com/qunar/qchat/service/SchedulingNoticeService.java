package com.qunar.qchat.service;

import com.qunar.qchat.constants.Config;
import com.qunar.qchat.dao.SchedulingInfoDao;
import com.qunar.qchat.model.SchedulingInfo;
import com.qunar.qchat.utils.DateUtils;
import com.qunar.qchat.utils.HttpClientUtils;
import com.qunar.qchat.utils.JacksonUtils;
import com.qunar.qchat.utils.NoticeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchedulingNoticeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulingNoticeService.class);

    @Autowired
    private SchedulingInfoDao schedulingInfoDao;

    public SchedulingNoticeService() {
    }

    public  void processTask() {
        LOGGER.info("processTask xxx");
        processPreScheduling();
        processPostScheduling();
    }

    public void processPreScheduling() {
        LOGGER.info("processPreTask xxx");
        List<SchedulingInfo> res = schedulingInfoDao.selectPreScheduling();

        List<Integer> ids = new ArrayList<Integer>();

        for (SchedulingInfo member : res) {
            LOGGER.info("meeting name is {}, meeting locale is {}, begin_time is {}", member.getScheduling_name(), member.getScheduling_locale(), member.getBegin_time());
            String url = Config.getProperty("push_message_notice_url");
            String from = NoticeMessage.appendQCDomain(Config.getProperty("meeting_robot"));
            String to = NoticeMessage.appendQCDomain(member.getMember());
            String message = NoticeMessage.makeNoticeMessage(from, to, "您受邀的行程\"" + member.getScheduling_name() + "\"(" + member.getScheduling_locale() + ":" + member.getScheduling_room() + ")将于15分钟后开始(会议开始时间：" + DateUtils.getFormatTime(member.getBegin_time()) + ")", "");

            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("from",from);
            paramsMap.put("to",to);
            paramsMap.put("message",message);
            String json = JacksonUtils.obj2String(paramsMap);
            LOGGER.info("the url is {}, the from is {}, the to is {}, the message is {}", url, from, to, message);
            String response = HttpClientUtils.postJson(url, json);
            LOGGER.info("the response is {}", response);
            ids.add(member.getId());
        }

        if (!ids.isEmpty()) {
            LOGGER.info("processPreTask readflag {}", 1);
            schedulingInfoDao.updateRemindFlag(ids, "1");
        }
    }

    public void processPostScheduling() {
        LOGGER.info("processPostTask xxx");
        List<SchedulingInfo> res = schedulingInfoDao.selectPostScheduling();

        List<Integer> ids = new ArrayList<Integer>();

        for (SchedulingInfo member : res) {
            LOGGER.info("meeting name is {}, meeting locale is {}, begin_time is {}", member.getScheduling_name(), member.getScheduling_locale(), member.getEnd_time());
            String url = Config.getProperty("push_message_notice_url");
            String from = NoticeMessage.appendQCDomain(Config.getProperty("meeting_robot"));
            String to = NoticeMessage.appendQCDomain(member.getMember());
            String message = NoticeMessage.makeNoticeMessage(from, to, "您受邀的行程\"" + member.getScheduling_name() + "\"(" + member.getScheduling_locale() + ":" + member.getScheduling_room() + ")将于15分钟后结束(会议结束时间：" + DateUtils.getFormatTime(member.getEnd_time()) + ")", "");
            LOGGER.info("the url is {}, the from is {}, the to is {}, the message is {}", url, from, to, message);
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("from",from);
            paramsMap.put("to",to);
            paramsMap.put("message",message);
            String json = JacksonUtils.obj2String(paramsMap);
            LOGGER.info("the url is {}, the from is {}, the to is {}, the message is {}", url, from, to, message);
            String response = HttpClientUtils.postJson(url, json);
            LOGGER.info("the response is {}", response);
            ids.add(member.getId());
        }

        if (!ids.isEmpty()) {
            LOGGER.info("processPostTask readflag {}", 2);
            schedulingInfoDao.updateRemindFlag(ids, "2");
        }
    }
}
