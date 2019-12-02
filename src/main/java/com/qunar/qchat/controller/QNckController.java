package com.qunar.qchat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qunar.qchat.constants.Config;
import com.qunar.qchat.constants.QChatConstant;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.model.request.NckRequest;
import com.qunar.qchat.model.request.SendWlanMsgRequest;
import com.qunar.qchat.model.result.CheckPushKeyResult;
import com.qunar.qchat.utils.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RequestMapping("/newapi/nck/")
@RestController
public class QNckController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QBaseController.class);

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AuthUtil authUtil;

    @RequestMapping(value = "/get_anony_token.qunar", method = RequestMethod.POST)
    public JsonResult<?> getAnonyToken(@RequestBody NckRequest nckRequest, HttpServletRequest request, HttpServletResponse response) {
        try {
            LOGGER.info("getAnonyToken begin param :{}", JacksonUtils.obj2String(nckRequest));
            String gid = nckRequest.getGid();
            String plat = nckRequest.getPlat();
            String version = nckRequest.getVersion();
            gid = getGidByCookie(request, plat, gid);
            LOGGER.info("getAnonyToken gid:{}", gid);
            if (StringUtils.isAnyBlank(gid, plat, version)) {
                return JsonResultUtils.fail(440, "参数错误");
            }
            gid = gid.toLowerCase();
            String key = plat + gid;
            JSONObject result = new JSONObject();
            result.put("username", gid);
            result.put("switchOn", true);

            // RedisUtil.delete(1, key);
            JSONObject jsonObject = redisUtil.get(1, key, JSONObject.class);

            if (jsonObject != null && jsonObject.getJSONObject("anony") != null) {
                result.put("token", jsonObject.toString());
                redisUtil.expire(1, key, Config.TOKEN_VALID_TIME, TimeUnit.DAYS);
            } else {
                String token = UUID.randomUUID().toString();
                JSONObject tokenJson = new JSONObject();
                JSONObject platJson = new JSONObject();
                tokenJson.put("anony", platJson);
                platJson.put("plat", plat);
                platJson.put("uuid", gid);
                platJson.put("token", token);
                result.put("token", tokenJson.toString());
                redisUtil.set(key, tokenJson.toString(), Config.TOKEN_VALID_TIME, TimeUnit.DAYS);
                responseSetCookie(plat, gid, response);
            }
            LOGGER.info("getAnonyToken result:{}", result.toString());
            return JsonResultUtils.success(result);

        } catch (Exception e) {
            LOGGER.error("catch error ", e);
            return JsonResultUtils.fail(500, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }

    private String getGidByCookie(HttpServletRequest request, String plat, String gid) {
        if ("web".equals(plat)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("anony_gid")) {
                        return cookie.getValue();
                    }
                }
            }
            return StringUtils.isEmpty(gid) ? UUID.randomUUID().toString().replace("-", "") : gid;
        }
        return gid;
    }

    private void responseSetCookie(String plat, String gid, HttpServletResponse response) {
        if ("web".equals(plat)) {
            Cookie cookie = new Cookie("anony_gid", gid);
            cookie.setDomain(Config.COOKIE_DOMAIN);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(Config.COOKIE_MAX_AGE);
            response.addCookie(cookie);
        }
    }


    @RequestMapping(value = "/send_wlan_msg.qunar", method = RequestMethod.POST)
    public JsonResult<?> sendWlanMessage(@RequestBody List<SendWlanMsgRequest> requests, HttpServletRequest servletRequest) {


        try {
            //验证环境
            if (StringUtils.isBlank(Config.CURRENT_ENV) ||
                    (!QChatConstant.ENVIRONMENT_QTALK.equals(Config.CURRENT_ENV.trim()) &&
                            !QChatConstant.ENVIRONMENT_QCHAT.equals(Config.CURRENT_ENV.trim()) &&
                            !QChatConstant.ENVIRONMENT_HOST.equals(Config.CURRENT_ENV.trim()))) {
                return JsonResultUtils.fail(1,"环境错误或当前环境不支持该操作");
            }

            if (CollectionUtils.isEmpty(requests) || !sendWlanMessageParameterValidate(requests)) {
                return JsonResultUtils.fail(1, "参数错误");
            }

            List<Map<String, String>> failedMsgs = new ArrayList<>();
            List<Map<String, String>> messageList = new ArrayList<>();

            for (SendWlanMsgRequest request : requests) {
                LOGGER.info("request paraeter: {}, current env:{}", request.toString(), Config.CURRENT_ENV);

                String failedMsgId = CommonUtil.uuid();

                //QTalk && 公共域
                if (QChatConstant.ENVIRONMENT_QTALK.equals(Config.CURRENT_ENV.trim()) ||
                        QChatConstant.ENVIRONMENT_HOST.equals(Config.CURRENT_ENV.trim())) {
                    CheckPushKeyResult checkPushKeyResult = authUtil.checkMacUserToken(
                            request.getKey(), request.getCount());
                    if (!checkPushKeyResult.isValidate()) {
                        failedMsgs.add(buildFailedMsg(new String[]{"msgId", failedMsgId},
                                new String[]{"reason", "登录验证失败"}));
                        LOGGER.info("send message {} failed, reason: {}", failedMsgId, "验证失败");
                        continue;
                    }
                }

                //QChat
                else { //(QChatConstant.ENVIRONMENT_QCHAT.equals(Config.CURRENT_ENV.trim())) {
                    String q = "";
                    String v = "";
                    String t = "";

                    Cookie[] cookies = servletRequest.getCookies();

                    if (ArrayUtils.isEmpty(cookies)) {
                        failedMsgs.add(buildFailedMsg(new String[]{"msgId", failedMsgId},
                                new String[]{"reason", "无效qvt参数"}));
                        LOGGER.info("send message {} failed, reason: {}", failedMsgId, "无效qvt参数");
                        continue;
                    }

                    for (Cookie cookie : cookies) {
                        if ("_q".equals(cookie.getName())) {
                            q = cookie.getValue();
                        } else if ("_v".equals(cookie.getName())) {
                            v = cookie.getValue();
                        } else if ("_t".equals(cookie.getName())) {
                            t = cookie.getValue();
                        }
                    }

                    if (StringUtils.isBlank(q) ||
                            StringUtils.isBlank(v) ||
                            StringUtils.isBlank(t)) {
                        failedMsgs.add(buildFailedMsg(new String[]{"msgId", failedMsgId},
                                new String[]{"reason", "无效qvt参数"}));
                        LOGGER.info("send message {} failed, reason: {}", failedMsgId, "无效qvt参数");
                        continue;
                    } else {
                        List<NameValuePair> nvList = new LinkedList<>();
                        nvList.add(new BasicNameValuePair("qCookie", q));
                        nvList.add(new BasicNameValuePair("vCookie", v));
                        nvList.add(new BasicNameValuePair("tCookie", t));
                        nvList.add(new BasicNameValuePair("userIp", "10.88.134.17"));
                        nvList.add(new BasicNameValuePair("userMac", "44:A8:42:1D:B8:05"));
                        nvList.add(new BasicNameValuePair("usersource", "qchat"));

                        try {
                            String rResponse = HttpClientUtils.post(Config.LOGIN_VALIDATE_URL, nvList);
                            ObjectMapper mapper = new ObjectMapper();
                            Map map = mapper.readValue(rResponse, Map.class);
                            if (!map.get("status").toString().equals("200")) {

                                failedMsgs.add(buildFailedMsg(new String[]{"msgId", failedMsgId},
                                        new String[]{"reason", "QChat登陆验证失败"}));
                                LOGGER.info("send message {} failed, reason: {}", failedMsgId, "QChat登陆验证失败");
                                continue;
                            }
                        } catch (Exception ex) {
                            LOGGER.error("cache error ", ex);

                            failedMsgs.add(buildFailedMsg(new String[]{"msgId", failedMsgId},
                                    new String[]{"reason", "QChat登陆验证失败, 外部接口异常"}));
                            LOGGER.info("send message {} failed, reason: {}", failedMsgId, "QChat登陆验证失败, 外部接口异常");
                            continue;
                        }
                    }
                }


                try {
                    List<SendWlanMsgRequest.ToUser> toUsers = request.getTo();
                    for (SendWlanMsgRequest.ToUser u : toUsers) {
                        Map<String, String> msgParameter;
                        if ("consult".equals(request.getType())) {
                            msgParameter = makeConsultNoticeMessage(request,
                                    u.getUser(), u.getRealto(),
                                    u.getChannelid(), u.getQchatid());
                            messageList.add(msgParameter);
                        } else {
                            msgParameter = makeNoticeMessage(request, u.getUser());
                            messageList.add(msgParameter);
                        }

                        //发送消息
                        /*String httpResult = HttpClientUtils.postJson(
                                Config.PUSH_MESSAGE_NOTICE_URL,
                                jsonParameter);

                        LOGGER.info("send message result: {}", httpResult);

                        if (StringUtils.isBlank(httpResult)) {
                            failedMsgs.add(buildFailedMsg(new String[]{"msgId", failedMsgId},
                                    new String[]{"reason", "消息发送失败"}));

                            LOGGER.info("send message {} failed, reason: {}", failedMsgId, "消息发送失败");
                            continue;
                        }*/

                        //QMonitor.recordOne("qchat_send_wlan_msg_success_count");
                    }

                } catch (Exception ex) {
                    LOGGER.error("catch error {}", ExceptionUtils.getStackTrace(ex));
                    //QMonitor.recordOne("qchat_send_wlan_msg_failed_count");

                    /*failedMsgs.add(buildFailedMsg(new String[]{"msgId", failedMsgId},
                            new String[]{"reason", "消息发送失败"}));*/

                    failedMsgs.add(buildFailedMsg(new String[]{"msgId", failedMsgId},
                            new String[]{"reason", "消息组装失败"}));

                    //LOGGER.info("send message {} failed, reason: {}", failedMsgId, "消息发送失败");
                    LOGGER.info("send message {} failed, reason: {}", failedMsgId, "消息组装失败");
                    continue;
                }
            }


            if(failedMsgs.size() > 0) {
                return JsonResultUtils.fail(1, "发送方登录验证失败");
            }

            //发送消息
            String httpResult = HttpClientUtils.postJson(
                    Config.PUSH_MESSAGE_NOTICE_URL,
                    JSON.toJSONString(messageList));

            LOGGER.info("send parameter: {}",JSON.toJSONString(messageList));
            LOGGER.info("send message url: {}", Config.PUSH_MESSAGE_NOTICE_URL);
            LOGGER.info("send message result: {}", httpResult);

            if (StringUtils.isBlank(httpResult)) {
                /*failedMsgs.add(buildFailedMsg(new String[]{"msgId", failedMsgId},
                        new String[]{"reason", "消息发送失败"}));

                LOGGER.info("send message {} failed, reason: {}", failedMsgId, "消息发送失败");
                continue;*/

                return JsonResultUtils.fail(1, "消息发送失败");
            }

            /*if (failedMsgs.size() > 0) {
                return JsonResultUtils.fail(1, failedMsgs.toString());
            }*/

            return JsonResultUtils.success("发送成功");

        } catch (Exception ex) {
            LOGGER.error("catch error {}", ExceptionUtils.getStackTrace(ex));
            return JsonResultUtils.fail(1, "服务器操作异常");
        }
    }

    private boolean sendWlanMessageParameterValidate(List<SendWlanMsgRequest> requests) {
        for (SendWlanMsgRequest request : requests) {
            if (!request.isRequestValid()) {
                return false;
            }
        }
        return true;
    }

    private Map<String, String> buildFailedMsg(String[] ... msgs) {
        Map<String, String> map = new HashMap<>();
        for(String[] kv :msgs) {
            map.put(kv[0], kv[1]);
        }
        return map;
    }


    private static Map<String, String> makeNoticeMessage(SendWlanMsgRequest request, String user) {

        String fromUser = request.getFrom().split("@")[0];
        String fromHost = request.getFrom().split("@")[1];

        String toUser = user.split("@")[0];
        String toHost = user.split("@")[1];

        Document document = DocumentHelper.createDocument();
        Element message = document.addElement("message");

        message.addAttribute("from", request.getFrom());
        message.addAttribute("to", toUser + "@" + toHost);
        message.addAttribute("type", request.getType());

        Element body = message.addElement("body");
        body.addAttribute("id", "im_http_service-" + UUID.randomUUID().toString());
        body.addAttribute("msgType", request.getMsg_type());
        body.addAttribute("maType", "20");
        body.addAttribute("extendInfo", request.getExtend_info());
        body.addText(request.getBody());


        Map<String, String> args = new HashMap<>();
        args.put("from", fromUser + "@" + fromHost);
        args.put("to", toUser + "@" + toHost);
        args.put("message", message.asXML());
        args.put("system", "im_http_service");
        args.put("type", request.getType());

        //return JacksonUtils.obj2String(args);

        return args;
    }

    private static Map<String, String> makeConsultNoticeMessage(SendWlanMsgRequest request, String user, String realTo, String channelId,
                                                                String qchatId) {

        Document document = DocumentHelper.createDocument();
        Element message = document.addElement("message");

        message.addAttribute("from", request.getFrom());
        message.addAttribute("to", user);
        message.addAttribute("type", request.getType());
        message.addAttribute("realfrom", request.getFrom());
        message.addAttribute("realto", realTo);
        message.addAttribute("channelid", channelId);
        message.addAttribute("qchatid", qchatId);

        Element body = message.addElement("body");
        body.addAttribute("msgType", request.getMsg_type());
        body.addAttribute("maType", "20");
        body.addAttribute("extendInfo", request.getExtend_info());

        Map<String, String> args = new HashMap<>();
        args.put("from", request.getFrom());
        args.put("to", user);
        args.put("message", message.asXML());
        args.put("system", "im_http_service");
        args.put("type", request.getType());

        //return JacksonUtils.obj2String(args);
        return args;
    }

}
