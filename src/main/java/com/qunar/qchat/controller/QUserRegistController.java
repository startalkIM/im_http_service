package com.qunar.qchat.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.qunar.qchat.constants.Config;
import com.qunar.qchat.constants.QChatConstant;
import com.qunar.qchat.dao.IHostUserDao;
import com.qunar.qchat.dao.model.HostInfoModel;
import com.qunar.qchat.dao.model.HostUserModel;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.model.request.*;
import com.qunar.qchat.model.result.SearchApproveListResult;
import com.qunar.qchat.service.IDomainService;
import com.qunar.qchat.service.IUserRegistService;
import com.qunar.qchat.utils.HttpClientUtils;
import com.qunar.qchat.utils.JsonResultUtils;
import com.qunar.qchat.utils.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @auth dongzd.zhang
 * @Date 2019/7/9 11:20
 */
@Slf4j
@Controller
@RequestMapping("/newapi/nck")
public class QUserRegistController {

    @Autowired
    private IUserRegistService userRegistService;
    @Autowired
    private IDomainService domainService;
    @Autowired
    private IHostUserDao hostUserDao;

    private static final String REGIST_COOKIE_NAME = "sms_validate_code";
    //密码匹配规则
    public static final String PWD_REG = "^.{8,16}$";
    public static final String SEND_SMS_TYPE_REGIST = "regist";
    public static final String SEND_SMS_TYPE_APPROVE = "approve";


    /**
     * 发送注册短信验证码.
     * @param request
     * @return JsonResult
     * */
    @RequestMapping(value = "/sms/sendvalidatecode.qunar", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<?> sendUserRegistValidateCode(@RequestBody SendRegistValidateCodeRequest request) {
        if(!request.isRequestValid()) {
            return JsonResultUtils.fail(-1, "参数错误");
        }

        //验证当前域是否为toC域
        HostInfoModel hostInfoModel = domainService.getDomain(request.getDomain());
        if(hostInfoModel == null) {
            return JsonResultUtils.fail(2001, "域不存在");
        }

        //查询当前域是否接入了短信服务
        /*if(!smsRegitDomain.equals(request.getDomain())) {
            return JsonResultUtils.fail(2011, "该域没有接入短信服务");
        }*/

        if(SEND_SMS_TYPE_REGIST.equals(request.getType())) {

            boolean isToCDomain = domainService.isToCDomain(request.getDomain());
            if (!isToCDomain) {
                return JsonResultUtils.fail(2002, "当前域不需要注册");
            }


            Integer hireUserCount = hostUserDao.selectCountFireUserByUserId(request.getTelephone(), hostInfoModel.getId());
            if(hireUserCount != null && hireUserCount > 0) {
                hostUserDao.deleteFireUserByUserId(request.getTelephone(), hostInfoModel.getId(), hostInfoModel.getHost());
            }

            List<HostUserModel> hostUserModelList = hostUserDao.selectByHostAndUserId(hostInfoModel.getId(), request.getTelephone());
            if (CollectionUtils.isNotEmpty(hostUserModelList)) {
                return JsonResultUtils.fail(2003, "该用户已注册");
            }
        } else if (SEND_SMS_TYPE_APPROVE.equals(request.getType())){
            //查询当前域下是否存在改用户
            List<HostUserModel> hostUserModelList = hostUserDao.selectByHostAndUserId(hostInfoModel.getId(), request.getTelephone());
            if(hostUserModelList.isEmpty()) {
                return JsonResultUtils.fail(2010, "该用户未注册");
            }
        } else {
            return JsonResultUtils.fail(-2, "不支持的操作");
        }

        boolean sendResult = userRegistService.sendRegistSmsValidateCode(request.getTelephone(), request.getDomain());
        if(sendResult) {
            //查询当前域是否需要注册审批
            Integer needApprove = domainService.getDomainNeedApprove(request.getDomain());
            return JsonResultUtils.success(String.valueOf(needApprove));
        }
        return JsonResultUtils.fail(2000, "发送失败");
    }

    /**
     * 注册 下一步.
     * @param request
     * @return JsonResult
     * */
    @RequestMapping(value = "/userregist/check_sms_validate_code.qunar", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<?> checkSmsValidateCode(@RequestBody CheckSmsValidateCodeRequest request,
                                              HttpServletResponse httpServletResponse) {
        if(!request.isRequestValid()) {
            return JsonResultUtils.fail(-1, "错参数误");
        }
        boolean checkResult = userRegistService.checkSmsValidateCode(request.getTelephone(), request.getCode());
        if(checkResult) {
            //种cookie
            String checkSuccessCooke = Md5Utils.md5(request.getTelephone(), REGIST_COOKIE_NAME);
            Cookie cookie = new Cookie(REGIST_COOKIE_NAME, checkSuccessCooke);
            //默认5分钟内有效
            cookie.setMaxAge(5 * 60);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
            return JsonResultUtils.success();
        }
        return JsonResultUtils.fail(2004, "短信验证码无效");
    }

    /**
     * 查询审核状 下一步.
     * @param request
     * @return
     * */
    @RequestMapping(value = "/userregist/check_approve_status.qunar", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<?> checkSmsValidateCode(@RequestBody CheckApproveSmsValidateCodeRequest request) {
        if(!request.isRequestValid()) {
            return JsonResultUtils.fail(-1, "参数错误");
        }
        HostInfoModel hostInfoModel = domainService.getDomain(request.getDomain());
        if(hostInfoModel == null) {
            return JsonResultUtils.fail(2001, "域不存在");
        }
        boolean checkResult = userRegistService.checkSmsValidateCode(request.getTelephone(), request.getCode());
        if(checkResult) {
            //查询用户审核状态
            Integer approveStatus = hostUserDao.selectApproveStatus(request.getTelephone(), hostInfoModel.getId());
            return JsonResultUtils.success(approveStatus);
        }
        return JsonResultUtils.fail(2004, "短信验证码无效");
    }

    @RequestMapping(value = "/userregist/regist.qunar", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<?> userRegist(HttpServletRequest httpServletRequest, @RequestBody UserRegistRequest request) {
        System.out.println("注册参数：" + request.toString());
        if(!request.isRequestValid()) {
            return JsonResultUtils.fail(-1, "错参数误");
        }
        //验证密码格式
        Pattern pattern = Pattern.compile(PWD_REG);
        Matcher matcher = pattern.matcher(request.getPassword());
        if (!matcher.find()) {
            return JsonResultUtils.fail(2007, "密码格式错误");
        }
        if(!request.getPassword().equals(request.getConfirmpassword())) {
            return JsonResultUtils.fail(2005, "密码与确认密码不一致");
        }
        if(!validateRegistCookie(httpServletRequest)) {
            return JsonResultUtils.fail(2006, "操作超时");
        }
        HostInfoModel hostInfoModel = domainService.getDomain(request.getDomain());
        if(hostInfoModel == null) {
            return JsonResultUtils.fail(2001, "域不存在");
        }

        //验证用户是否已经存在
        List<HostUserModel> hostUserModelList = hostUserDao.selectByHostAndUserId(hostInfoModel.getId(), request.getTelephone());
        if(CollectionUtils.isNotEmpty(hostUserModelList)) {
            return JsonResultUtils.fail(2003, "该用户已注册");
        }

        //验证域名是否存在
        boolean registResult = userRegistService.regist(request.getTelephone(), request.getUsername(),
                request.getPassword(),
                request.getDomain(), request.getDescription());
        if(registResult) {
            return JsonResultUtils.success();
        }
        return JsonResultUtils.fail(2007, "注册失败");
    }


    private boolean validateRegistCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            String curCookieName = cookie.getName();
            if(REGIST_COOKIE_NAME.equals(curCookieName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查询审批列表
     * @param request
     * @return JsonResult
     * */
    @RequestMapping(value = "/userregist/approve/search.qunar")
    @ResponseBody
    public JsonResult<?> searchApproveList(@RequestBody SearchApproveListRequest request) {
        if(!request.isRequestValid()) {
            return JsonResultUtils.fail(-1, "参数错误");
        }
        //查询当前域名是否存在
        HostInfoModel hostInfoModel = domainService.getDomain(request.getDomain());
        if(hostInfoModel == null) {
            return JsonResultUtils.fail(2008,"域名不存在");
        }
        //检查是否为toC域
        boolean isToCDomain = domainService.isToCDomain(request.getDomain());
        if(!isToCDomain) {
            return JsonResultUtils.fail(2009, "当前域不是toC域");
        }

        Integer count = hostUserDao.searchHostUsersCount(hostInfoModel.getId(), request.getStatus(), request.getKeyword());

        List<HostUserModel> hostUserModels = hostUserDao.searchHostUsers(hostInfoModel.getId(), request.getStatus(),
                request.getKeyword(),  request.getLimit(),request.getOffset());

        List<SearchApproveListResult> resultList = new ArrayList<>(hostUserModels.size());

        for(HostUserModel model : hostUserModels) {
            SearchApproveListResult result = new SearchApproveListResult();
            result.setId(model.getId());
            result.setName(model.getUserName());
            result.setTel(model.getTel());
            result.setDesc(model.getUserDesc());
            result.setStatus(model.getApproveFlag());
            result.setCreateTime(model.getCreateTime());
            resultList.add(result);
        }

        Map<String, Object> result = Maps.newHashMap();
        result.put("total", count);
        result.put("rows", resultList);
        return JsonResultUtils.success(result);
    }

    /**
     * 审批用户.
     * @param
     * */
    @RequestMapping(value = "/userregist/approve/updatestatus.qunar")
    @ResponseBody
    public JsonResult<?> approveHostUsers(@RequestBody ApproveUsersRequest request) {
        if(!request.isRequestValid()) {
            return JsonResultUtils.fail(-1, "参数错误");
        }
        if(CollectionUtils.isEmpty(request.getUserIds())) {
            return JsonResultUtils.success();
        }
        HostInfoModel hostInfoModel = domainService.getDomain(request.getDomain());
        int effective = hostUserDao.updateApproveStatus(request.getUserIds(), request.getStatus(), hostInfoModel.getId());
        if(effective > 0) {
            return JsonResultUtils.success(effective);
        }
        return JsonResultUtils.fail(2010, "更新失败");
    }

    /**
     * 判断一个域是否为toC域.
     * @param request
     * @return JsonResult
     * */
    @RequestMapping(value = "/userregist/check_domain_type.qunar", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<?> checkDomainType(@RequestBody CheckDomainTypeRequest request) {
        if(!request.isRequestValid()) {
            return JsonResultUtils.fail(-1, "参数错误");
        }
        HostInfoModel hostInfoModel = domainService.getDomain(request.getDomain());
        if(hostInfoModel == null) {
            return JsonResultUtils.fail(2008,"域名不存在");
        }
        boolean isToCDomain = domainService.isToCDomain(request.getDomain());
        Map<String, String> result = Maps.newHashMap();
        result.put("type", isToCDomain ? "1" : "0");
        return JsonResultUtils.success(result);
    }

    /**
     * 长链转短链.
     * @param request
     * @return JsonResult
     * */
    @RequestMapping(value = "/userregist/shorturlconvert.qunar", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult<?> shortUrlConvert(@RequestBody ShortUrlConvertRequest request) throws Exception {
        if(!request.isRequestValid()) {
            return JsonResultUtils.fail(-1, "参数错误");
        }

        String convertUrl = Config.getProperty("shorturl_convert_url");
        String paramJson = "{\"url\":\""+request.getUrl()+"\"}";
        String strResult = HttpClientUtils.postJson(convertUrl, paramJson);
        if(StringUtils.isEmpty(strResult)) {
            return JsonResultUtils.fail(2009, "短链转换失败");
        }
        Map<String, Object> mapResult = JSON.parseObject(strResult, Map.class);
        return JsonResultUtils.success(mapResult.get("data"));
    }



}
