package com.qunar.qchat.utils;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/22.
 */
public class Xml2Json {
    private static final Logger LOGGER = LoggerFactory.getLogger(Xml2Json.class);
    public static final String TEXT_CONT = "<text_cont>";//用于各个标签的CDATA部分

    public static Map<String, Object> xmppToMap(String xmpp) {
        if (StringUtils.isEmpty(xmpp)) {
            return null;
        }

        SAXReader saxReader = new SAXReader();
        Map<String, Object> jsonRes = new HashMap<>();
        Map<String, Object> message = new HashMap<>();
        Map<String, Object> body = new HashMap<>();
        Map<String, Object> time = new HashMap<>();


        try {
            Document document = saxReader.read(new ByteArrayInputStream(xmpp.getBytes(HttpUtils.UTF8)));
            Element messageEle = document.getRootElement();
            List<Attribute> msgAttrList = messageEle.attributes();
            for (Attribute msgAttr : msgAttrList) {
                message.put(msgAttr.getQualifiedName(), msgAttr.getValue());
            }

            putAttrlist(messageEle,"body",body);
            putAttrlist(messageEle,"stime",time);

            jsonRes.put("message",message);
            jsonRes.put("body",body);
            jsonRes.put("time",time);

        } catch (DocumentException e) {
            LOGGER.error("解析xmpp消息出错, xmpp: {} ", xmpp, e);
        }
        return jsonRes;
    }

    public static void putAttrlist(Element messageEle,String key,Map<String, Object> attrmap)
    {
        Element element = messageEle.element(key);
        if (element == null)
        {
            attrmap.put(key,"");
        }else{
            List<Attribute> AttrList = element.attributes();
            for (Attribute Attr : AttrList) {
                attrmap.put(Attr.getQualifiedName(), Attr.getValue());
            }
            if (key.equals("body")) {
                attrmap.put("content", element.getText());
            }
        }
    }
}
