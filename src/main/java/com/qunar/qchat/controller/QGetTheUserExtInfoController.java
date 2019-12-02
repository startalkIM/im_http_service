package com.qunar.qchat.controller;

import com.qunar.qchat.model.GetTelResult;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.model.request.GetLeaderRequest;
import com.qunar.qchat.model.request.GetMobileRequest;
import com.qunar.qchat.service.GetUserExtInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 获取用户的额外的信息,目前支持手机号跟直属领导的信息
 */
@RequestMapping("/newapi/")
@RestController
public class QGetTheUserExtInfoController {
    @Resource
    private GetUserExtInfoService getUserExtInfoService;

    @PostMapping("/getMobile.qunar")
    @ResponseBody
    public GetTelResult getMobiel(@RequestBody GetMobileRequest getMobileRequest) {
        return getUserExtInfoService.getTheMobile(getMobileRequest);
    }

    @PostMapping("/getLeader.qunar")
    @ResponseBody
    public GetTelResult getLeader(@RequestBody GetLeaderRequest getLeaderRequest) {
        return getUserExtInfoService.getLeader(getLeaderRequest);
    }
}
