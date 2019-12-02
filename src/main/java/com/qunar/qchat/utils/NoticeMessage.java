package com.qunar.qchat.utils;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

public class NoticeMessage {

    public static String makeInviteMessage(String from, String to, String text, String extendInfo) {
        DocumentFactory df = DocumentFactory.getInstance();
        Document document = df.createDocument("utf-8");
        Element messageEle = document.addElement("message");
        messageEle.addAttribute("from", appendQCDomain(from));
        messageEle.addAttribute("to", appendQCDomain(to));
        messageEle.addAttribute("type", "chat");

        Element bodyEle = messageEle.addElement("body");
        bodyEle.addAttribute("extendInfo", extendInfo);
        bodyEle.addAttribute("msgType", "257");
        bodyEle.addAttribute("maType", "20");
        bodyEle.addAttribute("id", CreateXmppMsg.getUUID());
        bodyEle.addText(text);
        String message = CreateXmppMsg.trimXml(document);

        return message;
    }

    public static String makeNoticeMessage(String from, String to, String text, String extendInfo) {
        DocumentFactory df = DocumentFactory.getInstance();
        Document document = df.createDocument("utf-8");
        Element messageEle = document.addElement("message");
        messageEle.addAttribute("from", appendQCDomain(from));
        messageEle.addAttribute("to", appendQCDomain(to));
        messageEle.addAttribute("type", "chat");

        Element bodyEle = messageEle.addElement("body");
        bodyEle.addAttribute("extendInfo", extendInfo);
        bodyEle.addAttribute("msgType", "1");
        bodyEle.addAttribute("maType", "20");
        bodyEle.addAttribute("id", CreateXmppMsg.getUUID());
        bodyEle.addText(text);
        String message = CreateXmppMsg.trimXml(document);

        return message;
    }

    public static String appendQCDomain(String str) {
        if (!StringUtils.contains(str, "@")) {
            return StringUtils.join(str, "@ejabhost1");
        } else {
            return str;
        }
    }

}
