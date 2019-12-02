package com.qunar.qchat.utils;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessageUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageUtils.class);
    private static final Integer MAX_MSG_SIZE = 64*1000;//限制消息的长度最大64KB

    public static String makeMessage(String type, String from, String fromHost, String to, String toHost, String extendInfo, String msgType, String content) {
        String fromJid = from.concat("@").concat(fromHost);
        String toJid =to.concat("@").concat(toHost);

        Document document = DocumentHelper.createDocument();
        Element message = document.addElement("message");

        message.addAttribute("from", fromJid);
        message.addAttribute("to", toJid);
        message.addAttribute("type", type);

        Element body = message.addElement("body");
        body.addAttribute("id", "http-" + UUID.randomUUID().toString());
        body.addAttribute("msgType", msgType);
        body.addAttribute("maType", "20");
        body.addAttribute("extendInfo", extendInfo);
        body.addText(content);

        Map<String, String> args = new HashMap<>();
        args.put("from", fromJid);
        args.put("to", toJid);
        args.put("message", message.asXML());
        args.put("system", "qtalk-corp-service");
        args.put("type", type);
        return JacksonUtils.obj2String(args);
    }
    public static String makeMessage(String type, String from, String fromHost, String to, String toHost, String extendInfo, String msgType, String content,
                                     String backupinfo,String autoReply) {
        String fromJid = from.concat("@").concat(fromHost);
        String toJid =to.concat("@").concat(toHost);

        Document document = DocumentHelper.createDocument();
        Element message = document.addElement("message");

        message.addAttribute("from", fromJid);
        message.addAttribute("to", toJid);
        message.addAttribute("auto_reply",autoReply);
        message.addAttribute("type", type);

        Element body = message.addElement("body");
        body.addAttribute("id", "qtalk-corp-service-" + UUID.randomUUID().toString());
        body.addAttribute("msgType", msgType);
        body.addAttribute("maType", "20");
        body.addAttribute("extendInfo", extendInfo);
        body.addAttribute("backupinfo", backupinfo);
        body.addText(content);

        Map<String, String> args = new HashMap<>();
        args.put("from", fromJid);
        args.put("to", toJid);
        args.put("message", message.asXML());
        args.put("system", "qtalk-corp-service");
        args.put("type", type);
        return JacksonUtils.obj2String(args);
    }

    public  static boolean checkMsgSize(String msgType, String content,
                                        String backupinfo,String extendInfo) throws UnsupportedEncodingException {
        Document document = DocumentHelper.createDocument();
        Element body = document.addElement("body");
        body.addAttribute("id", "qtalk-corp-service-" + UUID.randomUUID().toString());
        body.addAttribute("msgType", msgType);
        body.addAttribute("maType", "20");
        body.addAttribute("extendInfo", extendInfo);
        body.addAttribute("backupinfo", backupinfo);
        body.addText(content);
        String msg = body.asXML();
        if(msg==null){
            return false;
        }
        Integer size = msg.getBytes("utf-8").length;
        if(size>MAX_MSG_SIZE){
            LOGGER.warn("msg too long msg is {},size is {}",msg,size);
            return false;
        }
        return true;
    }
}
