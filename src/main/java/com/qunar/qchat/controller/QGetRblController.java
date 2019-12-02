package com.qunar.qchat.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.qunar.qchat.dao.IGetRblDao;
import com.qunar.qchat.dao.model.GetRblMsg;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.utils.JsonResultUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequestMapping("/newapi/")
@RestController
public class QGetRblController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QGetRblController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IGetRblDao iGetRblDao;


    @RequestMapping(value = "/getrbl.qunar",method = RequestMethod.POST)
    public JsonResult<?> getMsg(HttpServletRequest request, @RequestBody String json) {

        List<GetRblMsg> msgs = new ArrayList<>();
        List<GetRblMsg> groupmsgs = new ArrayList<>();

        try{
            if (json.length() > 0) {


                ObjectMapper mapper = new ObjectMapper();
                Map map = mapper.readValue(json, Map.class);

                String user = (String) map.get("user");
                String domain = (String) map.get("domain");

                msgs = iGetRblDao.selectRblMsg(user);
                groupmsgs = iGetRblDao.selecRblMucMsg(user);

                if (groupmsgs!=null) {
                    msgs.addAll(groupmsgs);
                    Collections.sort(msgs);
                }

            }
        }catch (Exception e){
            LOGGER.error("catch error:{} ", ExceptionUtils.getStackTrace(e));
            return JsonResultUtils.fail(0, "服务器操作异常:\n "+ExceptionUtils.getStackTrace(e));
        }


        return JsonResultUtils.success(msgs);
    }





}
