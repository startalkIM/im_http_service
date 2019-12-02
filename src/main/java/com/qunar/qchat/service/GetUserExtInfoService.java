package com.qunar.qchat.service;


import com.google.common.base.Strings;
import com.qunar.qchat.constants.BaseCode;
import com.qunar.qchat.constants.BasicConstant;
import com.qunar.qchat.dao.IHostInfoDao;
import com.qunar.qchat.dao.IUserInfo;
import com.qunar.qchat.dao.model.HostInfoModel;
import com.qunar.qchat.dao.model.UserInfoQtalk;
import com.qunar.qchat.model.GetTelResult;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.model.request.GetLeaderRequest;
import com.qunar.qchat.model.request.GetMobileRequest;
import com.qunar.qchat.utils.JacksonUtils;
import com.qunar.qchat.utils.JsonResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

@Service
@Slf4j
public class GetUserExtInfoService {

    @Resource
    private IUserInfo iUserInfo;

    @Resource
    private IHostInfoDao iHostInfoDao;

    /**
     * 获取用户手机号
     *
     * @return
     */
    public GetTelResult getTheMobile(GetMobileRequest getMobileRequest) {
        GetTelResult result = new GetTelResult();
        if (getMobileRequest == null || Strings.isNullOrEmpty(getMobileRequest.getQtalk_id()) || Strings.isNullOrEmpty(getMobileRequest.getUser_id())) {
            log.info("get the mobile fail due to the illegal param:{}", JacksonUtils.obj2String(getMobileRequest));
            result.setErrcode(1);
            result.setMsg("参数缺失");
            return result;
        }
        HostInfoModel hostInfoModel = iHostInfoDao.selectDefaultHost();
        if (!checkUserExist(getMobileRequest.getUser_id(), hostInfoModel)) {
            log.info("get the mobile fail due to the user illegal:{}", JacksonUtils.obj2String(getMobileRequest));
            result.setErrcode(1);
            result.setMsg("无权限查询");
            return result;
        }
        String tel = iUserInfo.getUserMobile(getMobileRequest.getUser_id(), hostInfoModel.getId());
        if(Strings.isNullOrEmpty(tel)){
            result.setErrcode(403);
            result.setMsg("查询手机号为空");
            return result;
        }
        HashMap<String, String> phone = new HashMap<>();
        phone.put("phone",tel);
        result.setMsg("查询成功");
        result.setErrcode(0);
        result.setData(phone);
        return result;
    }

    /**
     * 获取直属领导
     *
     * @return
     */
    public GetTelResult getLeader(GetLeaderRequest getLeaderRequest) {
        GetTelResult result = new GetTelResult();
        if (getLeaderRequest == null || Strings.isNullOrEmpty(getLeaderRequest.getUser_id())) {
            log.info("get the leader fail due to the illegal param:{}", JacksonUtils.obj2String(getLeaderRequest));
            result.setErrcode(1);
            result.setMsg("参数缺失");
            return result;
        }
        HostInfoModel hostInfoModel = iHostInfoDao.selectDefaultHost();
        HashMap<String,Object> data = new HashMap<>(4);
        UserInfoQtalk user = iUserInfo.selectUserByUserId(getLeaderRequest.getQtalk_id(),hostInfoModel.getId());
        UserInfoQtalk leader = null;
        if(Strings.isNullOrEmpty(user.getUser_id())){
            leader = user;
        }else {
            leader = iUserInfo.selectUserByUserId(user.getLeader(),hostInfoModel.getId());
        }
        if(leader==null){
            result.setMsg("不存在的员工");
            result.setErrcode(403);
            return result;
        }
        data.put("leader",leader.getUser_name());
        data.put("email",leader.getEmail());
        data.put("qtalk_id",leader.getUser_id());
        data.put("sn","");
        result.setMsg("查询成功");
        result.setErrcode(0);
        result.setData(data);
        return result;

    }

    public boolean checkUserExist(String userId, HostInfoModel hostInfoModel) {
        Integer hire = iUserInfo.getUserHireFlag(userId, hostInfoModel.getId());
        if (hire == null || hire.equals(0)) {
            return false;
        }
        return true;
    }

}
