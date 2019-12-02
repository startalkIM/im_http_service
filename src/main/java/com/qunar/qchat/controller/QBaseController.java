package com.qunar.qchat.controller;

import com.qunar.qchat.constants.Config;
import com.qunar.qchat.constants.QChatConstant;
import com.qunar.qchat.dao.IHostInfoDao;
import com.qunar.qchat.dao.IInviteInfoDao;
import com.qunar.qchat.dao.model.InviteInfoModel;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.model.request.GetDepsRequest;
import com.qunar.qchat.model.request.GetInviteInfoRequest;
import com.qunar.qchat.utils.CookieUtils;
import com.qunar.qchat.utils.HttpClientUtils;
import com.qunar.qchat.utils.JsonResultUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RequestMapping("/newapi/base/")
@RestController
public class QBaseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QBaseController.class);

    @Autowired
    private IInviteInfoDao iInviteInfoDao;
    @Autowired
    private IHostInfoDao hostInfoDao;


    /**
     * 获取系统时间.
     * @param request  HttpServletRequest
     * @return JsonResult<?>
     * */
    @RequestMapping(value = "/getservertime.qunar", method = RequestMethod.GET)
    public JsonResult<?> getServerTime(HttpServletRequest request) {
        try {
            long time = System.currentTimeMillis() / 1000;
            return JsonResultUtils.success(time);
        } catch (Exception e) {
            LOGGER.error("catch error ", e);
            return JsonResultUtils.fail(1, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }


    /**
     * 获取用户邀请列表.
     * @param request GetInviteInfoRequest
     * @return JsonResult<?>
     * */
    @RequestMapping(value = "/get_invite_info.qunar", method = RequestMethod.POST)
    public JsonResult<?> getInviteInfo(@RequestBody GetInviteInfoRequest request) {
        try {
            if (!request.isRequestValid()) {
                return JsonResultUtils.fail(1, "参数错误");
            }

            //QTalk 验证domain
            if (QChatConstant.ENVIRONMENT_QTALK.equals(Config.CURRENT_ENV)) {
                if (Objects.isNull(hostInfoDao.selectHostInfoByHostName(request.getD()))) {
                    return JsonResultUtils.fail(1, "domain [" + request.getD() + "] 不存在");
                }
            }

            List<InviteInfoModel> inviteInfoModelList = iInviteInfoDao.selectInviteInfo(request.getUser(),
                                          request.getD(), request.getTime());

            if(CollectionUtils.isNotEmpty(inviteInfoModelList)) {
                inviteInfoModelList.stream().forEach(bean -> {
                    bean.setIhost(StringUtils.defaultString(bean.getIhost(), ""));
                    bean.setBody(StringUtils.defaultString(bean.getBody(), ""));
                    bean.setInviter(StringUtils.defaultString(bean.getInviter(), ""));
                    bean.setTimestamp(StringUtils.defaultString(bean.getTimestamp(), ""));
                });
            }

            return JsonResultUtils.success(inviteInfoModelList);
        } catch (Exception e) {
            LOGGER.error("catch error ", e);
            return JsonResultUtils.fail(1, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }


    //QTalk
    @RequestMapping(value = "/qtalk/get_deps.qunar", method = RequestMethod.GET)
    public Object getDeps(GetDepsRequest getDepsRequest, HttpServletRequest request){
        try{

            Map<String, Object> cookieValues  = CookieUtils.getUserbyCookie(request);
            String domain = (String)cookieValues.get("d");

            //不支持QChat调用
            if(StringUtils.isBlank(domain)) {
                return JsonResultUtils.fail(1, "参数错误");
            }

            if(QChatConstant.ENVIRONMENT_QCHAT.equals(Config.CURRENT_ENV)) {
                return JsonResultUtils.fail(1, "不支持的操作");
            }

            if (Objects.isNull(hostInfoDao.selectHostInfoByHostName(domain))) {
                return JsonResultUtils.fail(1,"domain [" + domain + "] 不存在");
            }

            String parameter = "{\"domain\":\""+ domain +"\"}";
            return HttpClientUtils.postJson(Config.URL_GET_DEPS_QTALK, parameter);
        }catch (Exception ex) {
            LOGGER.error("cache error", ex);
            return JsonResultUtils.fail(1, "服务器操作异常:\n" + ExceptionUtils.getStackTrace(ex));
        }
    }


}
