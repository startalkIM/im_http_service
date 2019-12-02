package com.qunar.qchat.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.qunar.qchat.constants.Config;
import com.qunar.qchat.constants.QChatConstant;
import com.qunar.qchat.dao.IHostUserDao;
import com.qunar.qchat.dao.IVCardInfoDao;
import com.qunar.qchat.dao.model.HostInfoModel;
import com.qunar.qchat.dao.model.HostUserModel;
import com.qunar.qchat.dao.model.VCardInfoModel;
import com.qunar.qchat.service.IDomainService;
import com.qunar.qchat.service.IUserRegistService;
import com.qunar.qchat.utils.HttpClientUtils;
import com.qunar.qchat.utils.KeyGenerator;
import com.qunar.qchat.utils.Md5Utils;
import com.qunar.qchat.utils.RedisUtil;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @auth dongzd.zhang
 * @Date 2019/7/9 10:36
 */
@Slf4j
@Service
public class UserRegistServiceImpl implements IUserRegistService{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistServiceImpl.class);

    @Value("${sms_token}")
    private String smsToken;
    @Value("${template_id}")
    private String registTemplateId;
    @Value("${sms_send_url}")
    private String smsSendUrl;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IDomainService domainService;
    @Autowired
    private IHostUserDao hostUserDao;
    @Autowired
    private IVCardInfoDao ivCardInfoDao;

    private static final String REGIST_USER_DEFAULT_DEPT = "/注册用户";
    private static final String DEFAULT_IMG = "/file/v2/download/79bbbdea3e47a2f07fc9a003d4eb7d54.png";


    @Override
    public boolean sendRegistSmsValidateCode(String tel, String domain) {
        String validateCode = getValidateCode();
        String validateCodeKey = KeyGenerator.smsValidateCodeKey(tel);
        redisUtil.set(validateCodeKey, validateCode, 5, TimeUnit.MINUTES);
        String smsJsonParameter = getSendRegistSmsValidateCodeJsonParameter(tel, validateCode, domain);
        return sendRegistSms(smsJsonParameter);
    }

    @Override
    public boolean checkSmsValidateCode(String tel, String code) {
        String validateCodeKey = KeyGenerator.smsValidateCodeKey(tel);
        String redisSmsCode = redisUtil.get(validateCodeKey, String.class);
        if(StringUtils.isNotEmpty(redisSmsCode) && code.equals(redisSmsCode)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean regist(String tel, String name, String password, String domain, String desc) {

        String salt = KeyGenerator.getPwdSalt();
        String encPassword = "CRY:" + Md5Utils.md5ForPassword(password, salt);

        //根据domain查询hostid
        HostInfoModel hostInfo = domainService.getDomain(domain);
        if(hostInfo == null) {
            LOGGER.info("regist failed , domain {} dose not exist.", domain);
            return false;
        }

        //验证域是否为toC域
        if(!domainService.isToCDomain(domain)) {
            LOGGER.info("{} is to B doamin, can not regist.", domain);
            return false;
        }

        //查询当前域是否开启了注册审核及功能, 0 - 需要审批（默认） 1-不需要审批
        Integer needApprove = domainService.getDomainNeedApprove(domain);

        //插入host_users
        HostUserModel newUser = new HostUserModel();
        newUser.setHostId(BigInteger.valueOf(hostInfo.getId()));
        newUser.setUserId(tel);
        newUser.setUserName(name);
        newUser.setTel(tel);
        newUser.setPwdSalt(salt);
        newUser.setPassword(encPassword);
        //newUser.setInitialpwd("******");
        newUser.setVersion(1);
        newUser.setDep1(REGIST_USER_DEFAULT_DEPT);
        newUser.setDepartment(REGIST_USER_DEFAULT_DEPT);
        newUser.setHireFlag(needApprove);
        //0-未审批 1-已审批
        newUser.setApproveFlag(needApprove);
        newUser.setUserDesc(desc);
        newUser.setUserOrigin(1);

        hostUserDao.insertHostUser(newUser);
        //插入vcard_version
        VCardInfoModel newVCardInfo = new VCardInfoModel();
        newVCardInfo.setUsername(tel);
        newVCardInfo.setHost(domain);
        newVCardInfo.setUrl(DEFAULT_IMG);
        newVCardInfo.setVersion(1);
        newVCardInfo.setProfileVersion(1);
        ivCardInfoDao.insertVCardVersion(newVCardInfo);

        //发送文件传输助手消息
        sendFileTransforMsg(domain, tel);

        return true;
    }

    @Override
    public boolean sendFileTransforMsg(String domain, String toUser) {
        Document document = DocumentHelper.createDocument();
        Element message = document.addElement("message");
        String msgId = "qtalkadmin_" + UUID.randomUUID().toString().replaceAll("-", "");
        message.addAttribute("msec_times", String.valueOf(System.currentTimeMillis()));
        message.addAttribute("type", "chat");
        message.addAttribute("from", "file-transfer@" + domain);

        Element body = message.addElement("body");
        body.addAttribute("id", msgId);
        body.addAttribute("msgType", "1");
        body.addAttribute("extendInfo", "1");
        body.addText("无需数据线，电脑与手机轻松互传文件");

        Element stime = message.addElement("stime");
        Namespace namespace = new Namespace(null, "jabber:stime:delay");
        stime.addAttribute("stamp", String.valueOf(System.currentTimeMillis()));
        stime.add(namespace);

        Map<String, String> args = new HashMap<>();
        args.put("from", "file-transfer@" + domain);
        args.put("to", toUser + "@" + domain);
        args.put("message", message.asXML());
        args.put("system", "vs_qtalk_admin");
        args.put("type", "headline");

        String msgSendUrl = Config.getProperty("push_message_notice_url");

        log.info("发送文件传输助手消息url：" + msgSendUrl + "\r\n" +
                "参数：" + JSON.toJSONString(Arrays.asList(args)));


        String sendMsgResult = HttpClientUtils.postJson(msgSendUrl, JSON.toJSONString(Arrays.asList(args)));
        Map<String, Object> mapResult = JSON.parseObject(sendMsgResult, Map.class);

        System.out.println("发送文件传输助手消息结果：" + mapResult);

        Object ret = mapResult.get("ret");
        if(ret == null) {
            return false;
        }
        Boolean sendResult = (Boolean)ret;
        return sendResult;
    }

    private boolean sendRegistSms(String jsonParameter) {
        String jsonResult = HttpClientUtils.postJson(smsSendUrl, jsonParameter);
        if(StringUtils.isNotEmpty(jsonResult)) {
            SendRegistSmsResult sendRegistSmsResult = JSON.parseObject(jsonResult, SendRegistSmsResult.class);
            return sendRegistSmsResult.isRet();
        }
        return false;

    }

    @Data
    @ToString
    public static class SendRegistSmsResult {
        private boolean ret;
        private int errcode;
        private String errmsg;
        private String data;
    }




    private String getSendRegistSmsValidateCodeJsonParameter(String tel, String validateCode, String domain) {

        Map<String, String> parameters = Maps.newHashMap();
        parameters.put("token", smsToken);
        parameters.put("domain", domain);
        parameters.put("templateId", registTemplateId);
        parameters.put("telephone", tel);
        parameters.put("code", validateCode);

        return JSON.toJSONString(parameters);
    }

    private String getValidateCode() {
        String validateCode = Integer.toString((int) ((Math.random() * 9 + 1) * 100000));
        return validateCode;
    }
}
