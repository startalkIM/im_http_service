package com.qunar.qchat.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.qunar.qchat.constants.Config;
import com.qunar.qchat.constants.QChatConstant;
import com.qunar.qchat.model.JsonResult;
import org.apache.http.util.TextUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

public class ConsultUtils {

    private static final Logger logger = LoggerFactory.getLogger(ConsultUtils.class);
    private static final String image_body = "[obj type=&quot;image&quot; value=&quot;%s&quot; width=200 height=200]";

    public static boolean sendMessage(String from, String to, String realfrom, String realto, String message, boolean toSeat, boolean isImage) {
        String messageParam = createConsultMessage(message, from, realfrom, to, realto,toSeat, isImage);

        logger.info("send message: {} - {} - {}", from, to, messageParam);

        return sendThirdMessage(from, to, messageParam);
    }

    private static boolean sendThirdMessage(String from, String to, String message) {
        if (TextUtils.isEmpty(from) || TextUtils.isEmpty(to))
            return false;
        from = makeSureUserJid(from, QChatConstant.QCHAR_HOST);
        to = makeSureUserJid(to, QChatConstant.QCHAR_HOST);

        Map<String, String> param = Maps.newHashMap();
        param.put(QChatConstant.Note.FROM, from);
        param.put(QChatConstant.Note.TO, to);
        param.put(QChatConstant.Note.MESSAGE, message);
        param.put("system", "vs_qchat_admin");
        String response = HttpClientUtils.postJson(Config.SEND_NOTE_URL, JSON.toJSONString(param));


        if (!Strings.isNullOrEmpty(response)) {
            JsonResult qChatResult = JSON.parseObject(response, JsonResult.class);
            if (qChatResult != null && qChatResult.isRet()) {
                return true;
            }
        }
        return false;
    }

    private static String createConsultMessage(
            String content,
            String from,
            String realFrom,
            String to,
            String realTo, boolean toSeat, boolean isImage) {

        Map<String, String> channelIdValue = Maps.newHashMap();
        channelIdValue.put("cn", "consult");
        channelIdValue.put("d", "recv" );
        channelIdValue.put("usrType", "usr");
        Document document = DocumentHelper.createDocument();
        Element message = document.addElement(QChatConstant.Note.MESSAGE);
        message.addAttribute(QChatConstant.Note.FROM, from);
        message.addAttribute(QChatConstant.Note.TO, to);
        if (!Strings.isNullOrEmpty(realFrom)) {
            message.addAttribute(QChatConstant.Note.REAL_FROM, realFrom);
        }
        if (!Strings.isNullOrEmpty(realTo)) {
            message.addAttribute(QChatConstant.Note.REAL_TO, realTo);
        }
        String qchatId = toSeat ? QChatConstant.Note.QCHAT_ID_USER2SEAT : QChatConstant.Note.QCHAT_ID_SEAT2USER;
        message.addAttribute(QChatConstant.Note.Type, QChatConstant.Note.CONSULT);
        message.addAttribute(QChatConstant.Note.IS_HIDDEN_MSG, "0");
        message.addAttribute(QChatConstant.Note.CHANNEL_ID, JSON.toJSONString(channelIdValue));
        message.addAttribute(QChatConstant.Note.QCHAT_ID, qchatId); // 客人发给qchat客户端为4 qchat客户端给客人发是5
        message.addAttribute(QChatConstant.Note.XMLNS, QChatConstant.Note.JABBER_CLIENT);

        message.addAttribute(QChatConstant.Note.MESSAGE_AUTO_REPLY, "false");

        message.addAttribute(QChatConstant.Note.MESSAGE_NO_UPDATE_MSG_LOG, "true");

        Element body = message.addElement(QChatConstant.Note.BODY);
        body.addAttribute(QChatConstant.Note.ID, QChatConstant.QCADMIN + UUID.randomUUID().toString().replace("-", ""));
        body.addAttribute(QChatConstant.Note.MSGTYPE, "1");
        body.addAttribute(QChatConstant.Note.MATYPE, QChatConstant.Note.MATYPE_WEB);
        body.addText("%s");

//        Element element = body.addComment("obj");
//        element.addAttribute("type","image");
//        element.addAttribute("value",content);
//        element.addAttribute("width","44");
//        element.addAttribute("height","44");

        Element active = message.addElement(QChatConstant.Note.ACTIVE);
        active.addAttribute(QChatConstant.Note.XMLNS, QChatConstant.Note.JABBER_URL);
        if (isImage) {
            String image = String.format(image_body, content);
            return String.format(message.asXML(), image);
        }
        return String.format(message.asXML(), content);

//        String image = String.format(image_body, content);
//        String s = message.asXML();
//        s = String.format(message.asXML(), image);
//        System.out.println(s);
//        return String.format(message.asXML(), image);
    }

    private static String makeSureUserJid(String jid, String defaultDomain) {
        if (Strings.isNullOrEmpty(jid))
            return "";

        if (jid.indexOf("/") > 0) {
            jid = jid.substring(0, jid.indexOf("/"));
        }

        if (!jid.contains("@") && !TextUtils.isEmpty(defaultDomain)) {
            jid += "@";
            jid += defaultDomain;
        }

        return jid;
    }
}
