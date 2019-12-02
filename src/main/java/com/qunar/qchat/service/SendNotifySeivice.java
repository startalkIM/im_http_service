package com.qunar.qchat.service;

import com.alibaba.fastjson.JSON;
import com.qunar.qchat.constants.Config;
import com.qunar.qchat.dao.model.UserInfoQtalk;
import com.qunar.qchat.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * SendNotifySeivice
 *
 * @author binz.zhang
 * @date 2019/2/14
 */
@Service
public class SendNotifySeivice {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendNotifySeivice.class);
    private static final String send_url = Config.getProperty("url_send_notify");

    public void sendNotify(List<String> user, String host) {
        String from = "admin@" + host;
        Dictionary<String, Object> args = new Hashtable<>();
        args.put("from", from);
        args.put("category", "1");
        args.put("data", "");
        args.put("to", user);
        try {
            String ret = HttpClientUtils.postJson(send_url, JSON.toJSONString(args));
            LOGGER.info("send notify to :{}, ret;{}", JSON.toJSONString(args), ret);
        } catch (Exception e) {
            LOGGER.error("发送通知消息异常,{}", e);
        }
    }
}

