package com.qunar.qchat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qunar.qchat.constants.Config;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.model.SendMessageParam;
import com.qunar.qchat.model.SendWhiteModel;
import com.qunar.qchat.service.SendWhieMsgService;
import com.qunar.qchat.utils.HttpClientUtils;
import com.qunar.qchat.utils.JsonResultUtils;
import com.qunar.qchat.utils.MessageUtils;
import com.qunar.qchat.utils.StrUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/corp/message/")
@RestController
public class RouteMessageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RouteMessageController.class);

    @Autowired
    private SendWhieMsgService sendWhieMsgService;

    @RequestMapping(value = "/send_http_message.qunar", method = RequestMethod.POST)
    public JsonResult<?> sendHttpMessage(@RequestBody String json) {
        try {
            LOGGER.info("the send http message is {}", json);
            ObjectMapper mapper = new ObjectMapper();
            SendMessageParam params = mapper.readValue(json, SendMessageParam.class);
            String system = StrUtils.getStringValue(params.getSystem(), "");
            if (system.equals("") || system.equals("qtalk_corp_service") || system.equals("test")) {
                return JsonResultUtils.fail(1, "no system");
            }
            String type = StrUtils.getStringValue(params.getType());
            String from = StrUtils.getStringValue(params.getFrom());
            String fromHost = StrUtils.getStringValue(params.getFromhost());
            String extendInfo = StrUtils.getStringValue(params.getExtendinfo());
            String msgType = StrUtils.getStringValue(params.getMsgtype());
            String content = StrUtils.getStringValue(params.getContent());
            String autoReply = StrUtils.getStringValue(params.getAuto_reply());
            String backupinfo = StrUtils.getStringValue(params.getBackupinfo());

            SendWhiteModel sendWhiteModel = new SendWhiteModel();
            sendWhiteModel.setAppcode(system);
            sendWhiteModel.setFromUser(from);
            sendWhiteModel.setReviewFlag(0);
            if(!MessageUtils.checkMsgSize(msgType,content,backupinfo,extendInfo)){
                return JsonResultUtils.fail(101, "MSG too lang");
            }
            for (SendMessageParam.ToEntity e : params.getTo()) {
                String to = StrUtils.getStringValue(e.getUser());
                String toHost = StrUtils.getStringValue(e.getHost());
                String message = MessageUtils.makeMessage(type, from, fromHost, to, toHost, extendInfo, msgType, content, backupinfo, autoReply);
                LOGGER.info("第三方消息发送From:{},To:{},Msg:{}", from, to, message);
                sendThirdpartyMessage(message);
            }
            return JsonResultUtils.success();
        } catch (Exception e) {
            LOGGER.error("send http message fail:{}",e);
            return JsonResultUtils.fail(1, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }

    private String sendThirdpartyMessage(String msg) {
        String responseBody = HttpClientUtils.postJson(Config.getProperty("ejabberd_url") + "/send_thirdmessage", msg);
        if (responseBody != null) {
            LOGGER.info("receiveThirdpartySendQueue push 成功，body:{} ,response:{}", msg, responseBody);
        } else {
            LOGGER.error("receiveThirdpartySendQueue push 失败，body:{}", msg);
        }
        return responseBody;
    }
}
