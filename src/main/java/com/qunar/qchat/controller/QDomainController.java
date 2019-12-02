package com.qunar.qchat.controller;

import com.alibaba.fastjson.JSON;
import com.qunar.qchat.constants.Config;
import com.qunar.qchat.constants.QChatConstant;
import com.qunar.qchat.dao.IFloginUserDao;
import com.qunar.qchat.dao.IHostUserDao;
import com.qunar.qchat.dao.IVCardInfoDao;
import com.qunar.qchat.dao.model.HostInfoModel;
import com.qunar.qchat.dao.model.VCardInfoModel;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.model.request.GetUserStatusRequest;
import com.qunar.qchat.model.request.GetVCardInfoRequest;
import com.qunar.qchat.model.result.GetQChatVcardResult;
import com.qunar.qchat.model.result.GetQTalkVcardResult;
import com.qunar.qchat.model.result.GetVCardInfoResult;
import com.qunar.qchat.model.result.SearchVCardResult;
import com.qunar.qchat.service.IDomainService;
import com.qunar.qchat.utils.CommonRedisUtil;
import com.qunar.qchat.utils.HttpClientUtils;
import com.qunar.qchat.utils.JsonResultUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @auth dongzd.zhang
 * @Date 2018/10/19 16:05
 */

@RequestMapping("/newapi/domain")
@RestController
public class QDomainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QDomainController.class);

    @Autowired
    private CommonRedisUtil commonRedisUtil;

    @Autowired
    private IVCardInfoDao vCardInfoDao;
    @Autowired
    private IFloginUserDao floginUserDao;

    @Autowired
    private IDomainService domainService;
    @Autowired
    private IHostUserDao hostUserDao;

    @RequestMapping(value = "/get_user_status.qunar", method = RequestMethod.POST)
    public JsonResult<?> getUserStatus(@RequestBody GetUserStatusRequest request) {
        //QMonitor.recordOne("get_user_status");
        try {

            if(Objects.isNull(request) ||
                    Objects.isNull(request.getUsers()) ||
                        CollectionUtils.isEmpty(request.getUsers())) {
                return JsonResultUtils.fail(1, "参数错误");
            }

            List<String> originUsers = new ArrayList<>(request.getUsers());
            List<Map<String, Object>> resultData = new ArrayList<>();
            List<Map<String, String>> userStatus = new ArrayList<>();
            Map<String, Object> rowData = new HashMap<>();

            /** 处理机器人用户 */
            long step1StartTime = System.currentTimeMillis();
            List<String> floginUsers = floginUserDao.selectFloginUserNames();
            if(CollectionUtils.isNotEmpty(floginUsers)) {

                if(CollectionUtils.isNotEmpty(originUsers)) {

                    /** 获取当前domain */
                    String firstUserId = originUsers.get(0);
                    if(StringUtils.isBlank(firstUserId) ||
                            firstUserId.indexOf("@") == -1) {
                        return JsonResultUtils.fail(1, "参数错误");
                    }
                    String domain = firstUserId.split("@")[1];
                    //加上了domian 后的 flogin users
                    //List<String> processedFloginUsers = new ArrayList<>(floginUsers.size());
                    for(String floginUserId : floginUsers) {
                        String processedFloginUserId = floginUserId + "@" + domain;
                        //processedFloginUsers.add(processedFloginUserId);
                        if(originUsers.contains(processedFloginUserId)) {
                            Map<String, String> currentUserStatus = new HashMap<>();
                            currentUserStatus.put("u", StringUtils.defaultString(processedFloginUserId, ""));
                            currentUserStatus.put("o", "online");
                            userStatus.add(currentUserStatus);
                            originUsers.remove(processedFloginUserId);
                        }
                    }
                }
            }

            for(String key : originUsers) {

                if(StringUtils.isBlank(key) ||
                        key.indexOf("@") == -1) {
                    return JsonResultUtils.fail(1, "参数错误");
                }

                String status = commonRedisUtil.getUserStatus(key);
                Map<String, String> currentUserStatus = new HashMap<>();
                currentUserStatus.put("u", StringUtils.defaultString(key, ""));
                currentUserStatus.put("o", StringUtils.defaultString(status,""));
                userStatus.add(currentUserStatus);
            }
            rowData.put("ul", userStatus);
            resultData.add(rowData);

            return JsonResultUtils.success(rowData);
        }catch (Exception ex) {
            LOGGER.error("catch error", ex);
            return JsonResultUtils.fail(0, "服务器异常：" + ExceptionUtils.getStackTrace(ex));
        }
    }



    /**
     * 获取用户信息.
     * @param requests List<GetVCardInfoRequest>
     * @return  JsonResult<?>
     * */
    @RequestMapping(value = "/get_vcard_info.qunar", method = RequestMethod.POST)
    public JsonResult<?> getVCardInfo(@RequestBody List<GetVCardInfoRequest> requests) {

        //LOGGER.info(requests.toString());
        //LOGGER.info(requests.toString());
        try {
            if (!checkGetVcardInfoParameters(requests)) {
                return JsonResultUtils.fail(1, QChatConstant.PARAMETER_ERROR);
            }

            List<Map<String, Object>> finalResult = new ArrayList<>();
            for (GetVCardInfoRequest request : requests) {

                Map<String, Object> map = new HashMap<>();
                map.put("domain", request.getDomain());

                List<GetVCardInfoResult> users = new ArrayList<>();
                List<GetVCardInfoRequest.UserInfo> userInfos = request.getUsers();

                for (GetVCardInfoRequest.UserInfo userInfo : userInfos) {

                    Integer count = vCardInfoDao.getCountByUsernameAndHost(userInfo.getUser(), request.getDomain());
                    if (count > 0) {
                        VCardInfoModel result = vCardInfoDao.selectByUsernameAndHost(userInfo.getUser(), request.getDomain(), userInfo.getVersion());
                        if(Objects.nonNull(result)) {
                            GetVCardInfoResult resultBean = new GetVCardInfoResult();
                            HostInfoModel hostInfoModel = domainService.getDomain(request.getDomain());
                            Integer adminFlag = hostUserDao.selectAdminFlagByUserId(userInfo.getUser(), hostInfoModel.getId());
                            resultBean.setType("");
                            resultBean.setLoginName(StringUtils.defaultString(userInfo.getUser(), ""));
                            resultBean.setEmail("");
                            resultBean.setGender(StringUtils.defaultString(String.valueOf(result.getGender()), ""));
                            resultBean.setNickname(StringUtils.defaultString(result.getNickname(), ""));
                            resultBean.setWebname(StringUtils.defaultString(result.getNickname(), ""));
                            resultBean.setV(StringUtils.defaultString(String.valueOf(result.getVersion()), ""));
                            resultBean.setImageurl(Objects.isNull(result.getUrl()) ?
                                    getImageUrl(String.valueOf(result.getGender()))
                                    : result.getUrl());
                            resultBean.setUid("0");
                            resultBean.setUsername(StringUtils.defaultString(userInfo.getUser(), ""));
                            resultBean.setDomain(request.getDomain());
                            resultBean.setCommenturl(QChatConstant.VCARD_COMMON_URL);
                            resultBean.setMood(StringUtils.defaultString(result.getMood(), ""));
                            boolean isToCDomain = domainService.isToCDomain(request.getDomain());
                            resultBean.setAdminFlag((isToCDomain && adminFlag != null && adminFlag == 1));
                            users.add(resultBean);
                        }
                    }
                }
                map.put("users", users);
                finalResult.add(map);
            }

            return JsonResultUtils.success(finalResult);

        }catch (Exception ex) {
            LOGGER.error("catch error : {}", ExceptionUtils.getStackTrace(ex));
            return JsonResultUtils.fail(0, QChatConstant.SERVER_ERROR);
        }
    }

    private boolean checkGetVcardInfoParameters(List<GetVCardInfoRequest> requests) {
        if (CollectionUtils.isEmpty(requests)) {
            return false;
        }

        for(GetVCardInfoRequest request : requests) {
            if(StringUtils.isEmpty(request.getDomain())) {
                return false;
            }

            List<GetVCardInfoRequest.UserInfo> userInfoList = request.getUsers();
            for(GetVCardInfoRequest.UserInfo userInfo : userInfoList) {
                if(StringUtils.isEmpty(userInfo.getUser())) {
                    return false;
                }
            }
        }
        return true;
    }


    @RequestMapping(value = "/search_vcard.qunar", method = RequestMethod.GET)
    public JsonResult<?> searchVCard(String p, String v, String keyword) {

        try {

            if (StringUtils.isBlank(p) ||
                    StringUtils.isBlank(v) ||
                    StringUtils.isBlank(keyword)) {
                return JsonResultUtils.fail(1, "参数错误");
            }

            String getVCardInfoUrl = Config.GET_VCARD_INFO_URL + "?p=" + p + "&v=" + v + "&username=" + keyword;
            String httpResult = HttpClientUtils.get(getVCardInfoUrl);

                Map<?, ?> jsonResult = JSON.parseObject(httpResult, Map.class);
                Object data = jsonResult.get("data");
                if (Objects.isNull(data)) {
                    return JsonResultUtils.success();
                }

                if(StringUtils.isBlank(data.toString())) {
                    return JsonResultUtils.success();
                }

                Map<?, ?> dataResult = JSON.parseObject(data.toString(), Map.class);

                SearchVCardResult result = new SearchVCardResult();
                result.setDomain("ejabhost1");
                result.setNickname(dataResult.get("nickname"));
                result.setUsername(dataResult.get("username"));

                return JsonResultUtils.success(result);
            } catch (Exception ex) {
                LOGGER.error("catch error", ex);
                return JsonResultUtils.fail(0, "服务器异常：" + ExceptionUtils.getStackTrace(ex));
            }



    }

    private String getImageUrl(String gender) {
        if ("2".equals(gender)) {
            return QChatConstant.VCARD_IMAGE_URL_FEMAIL;
        }
        return QChatConstant.VCARD_IMAGE_URL_MAIL;
    }

}
