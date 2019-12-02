package com.qunar.qchat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qunar.qchat.dao.IUserInfo;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.model.MucRoomUserInfo;
import com.qunar.qchat.model.UserInfo;
import com.qunar.qchat.model.UserInfoParam;
import com.qunar.qchat.utils.JsonResultUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/corp/userinfo/")
@RestController
public class UserInfoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoController.class);

    @Autowired
    private IUserInfo iUserInfo;

    @RequestMapping(value = "/get_user_info.qunar", method = RequestMethod.POST)
    public JsonResult<?> getUserInfo(@RequestBody String json) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            UserInfoParam params = mapper.readValue(json, UserInfoParam.class);

            List<UserInfoParam.UsersEntity> users = params.getUsers();
            String system = params.getSystem();

            if (system.equals("")) {
                return JsonResultUtils.fail(2, "system is empty");
            }

            if (users.size() == 0) {
                return JsonResultUtils.fail(2, "username is empty");
            }


            List<UserInfo> list = new ArrayList<>();
            list = iUserInfo.selectUserInfo(users);

            if (list.size() == 0) {
                return JsonResultUtils.fail(2, "not found username");
            }

            return JsonResultUtils.success(list);

        } catch (Exception e) {
            LOGGER.error("异常信息{}", e);
            return JsonResultUtils.fail(1, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }

    @RequestMapping(value = "/check_user_exist_in_group.qunar", method = RequestMethod.POST)
    public JsonResult<?> checkUserInGroup(@RequestBody String json) {
        try {
            LOGGER.info("check user exist in group params:{}",json);
            ObjectMapper mapper = new ObjectMapper();
            MucRoomUserInfo mucRoomUserInfo = mapper.readValue(json, MucRoomUserInfo.class);

            if (mucRoomUserInfo == null || !StringUtils.isNotBlank(mucRoomUserInfo.getMucName())
                    || !StringUtils.isNotBlank(mucRoomUserInfo.getDomain())
                    || !StringUtils.isNotBlank(mucRoomUserInfo.getUsername())
                    || !StringUtils.isNotBlank(mucRoomUserInfo.getHost())) {
                return JsonResultUtils.fail(2, "params is empty");
            }

            boolean isExist = iUserInfo.selectMucRoomUsersCount(mucRoomUserInfo) >= 1;
            if(isExist){
                LOGGER.info("user exist in group params:{}",json);
            }else{
                LOGGER.info("user not exist in group params:{}",json);
            }

            return JsonResultUtils.success(isExist);
        } catch (Exception e) {
            LOGGER.error("异常信息{}", e);
            return JsonResultUtils.fail(1, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }
}
