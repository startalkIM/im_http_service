package com.qunar.qchat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qunar.qchat.dao.IQCloudDao;
import com.qunar.qchat.dao.model.QCloudMain;
import com.qunar.qchat.dao.model.QCloudMainHistory;
import com.qunar.qchat.dao.model.QCloudSub;
import com.qunar.qchat.dao.model.QCloudSubHistory;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 14/07/2017.
 *
 *
 */

@RequestMapping("/newapi/qcloud/")
@RestController
public class QCloudController {

    private static final Logger LOGGER = LoggerFactory.getLogger(QCloudController.class);

    @Autowired
    private HttpServletRequest request;


    @Autowired
    private IQCloudDao cloudDao;

    public enum QCloudDataState {

        Delete(-1),Normal(1), Collection(2), Basket(3), Create(4),Update(4),;

        private int value;

        private QCloudDataState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf ( this . value );
        }
    }

    private  String getFromUser(){
        Cookie[] cookies = request.getCookies();
        String user = null;
        if (cookies != null)
        {
            for (Cookie cookie : cookies)
            {
                if (cookie.getName().equalsIgnoreCase("p_user")) {
                    user = cookie.getValue();
                    break;
                }
            }
        }
        return user;
    }

    private boolean checkQidInvalid(String user,long qid){
        QCloudMain mainCloud = cloudDao.selectQCloudMainWithId(user,qid);
        if (mainCloud == null) {
            return true;
        }
        return false;
    }

    private boolean checkQSidInvalid(String user,long qsid){
        QCloudSub subCloud = cloudDao.selectQCloudSubWithId(user,qsid);
        if (subCloud == null) {
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/saveToMain.qunar",method = RequestMethod.POST)
    public JsonResult<?> saveToMain(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息 ");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            int type = Integer.valueOf(map.get("type").toString());
            String title = (String) map.get("title");
            String desc = (String) map.get("desc");
            String content = (String) map.get("content");

            // 插入主记录
            QCloudMain pModule = new QCloudMain();
            pModule.setQ_user(user);
            pModule.setQ_type(type);
            pModule.setQ_title(title);
            pModule.setQ_introduce(desc);
            pModule.setQ_content(content);
            pModule.setQ_time(System.currentTimeMillis());
            pModule.setQ_state(QCloudDataState.Normal.getValue());
            cloudDao.insertQCloudMain(pModule);

            // 插入历史记录
            QCloudMainHistory phModule = new QCloudMainHistory();
            phModule.setQ_id(pModule.getQ_id());
            phModule.setQh_content(json);
            phModule.setQh_time(pModule.getQ_time());
            phModule.setQh_state(QCloudDataState.Create.getValue());
            cloudDao.insertQCloudMainHistory(phModule);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("qid",pModule.getQ_id());
            resultMap.put("version",pModule.getQ_time());
            return JsonResultUtils.success(resultMap);
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.debug("save main cloud error {},{} ", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

    @RequestMapping(value = "/syncCloudMainList.qunar", method = RequestMethod.POST)
    public JsonResult<?> syncCloudMainList(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            ArrayList<Map> tempInsertList = (ArrayList<Map>)map.get("insert");
            ArrayList<Map> tempUpdateList = (ArrayList<Map>)map.get("update");
            ArrayList<Map> resultList = new ArrayList<>();
            if (tempInsertList != null && tempInsertList.size() > 0) {
                ArrayList<QCloudMain> instertList = new ArrayList<>();
                for (Map<String, Object> instertMap : tempInsertList) {
                    String cid = instertMap.get("cid").toString();
                    String title = (String) instertMap.get("title");
                    String desc = (String) instertMap.get("desc");
                    String content = (String) instertMap.get("content");
                    int type = Integer.valueOf(instertMap.get("type").toString());
                    int state = QCloudDataState.Normal.getValue();
                    Object ostate = instertMap.get("state");
                    if (ostate != null) {
                        state = Integer.valueOf(ostate.toString());
                    }
                    QCloudMain entity = new QCloudMain();
                    entity.setC_id(cid);
                    entity.setQ_user(user);
                    entity.setQ_title(title);
                    entity.setQ_introduce(desc);
                    entity.setQ_content(content);
                    entity.setQ_type(type);
                    entity.setQ_state(state);
                    entity.setQ_time(System.currentTimeMillis());
                    instertList.add(entity);
                }
                cloudDao.bulkInsertQCloudMain(instertList);
                for (QCloudMain entity : instertList) {
                    Map<String,Object> tMap = new HashMap<>();
                    tMap.put("cid",entity.getC_id());
                    tMap.put("qid",entity.getQ_id());
                    tMap.put("version",entity.getQ_time());
                    resultList.add(tMap);
                }
            }
            if (tempUpdateList != null && tempUpdateList.size() > 0) {
                ArrayList<QCloudMain> updateList = new ArrayList<>();
                for (Map<String, Object> updateMap : tempUpdateList) {
                    Object oqid = updateMap.get("qid");
                    if (oqid == null)
                        continue;
                    long qid = Long.valueOf(oqid.toString());
                    String cid = updateMap.get("cid").toString();
                    String title = (String) updateMap.get("title");
                    String desc = (String) updateMap.get("desc");
                    String content = (String) updateMap.get("content");
                    int type = Integer.valueOf(updateMap.get("type").toString());
                    int state = QCloudDataState.Normal.getValue();
                    Object ostate = updateMap.get("state");
                    if (ostate != null) {
                        state = Integer.valueOf(ostate.toString());
                    }
                    QCloudMain entity = new QCloudMain();
                    entity.setQ_id(qid);
                    entity.setC_id(cid);
                    entity.setQ_title(title);
                    entity.setQ_introduce(desc);
                    entity.setQ_content(content);
                    entity.setQ_type(type);
                    entity.setQ_state(state);
                    entity.setQ_time(System.currentTimeMillis());
                    updateList.add(entity);
                }
                cloudDao.bulkUpdateQCloudMain(updateList);
                for (QCloudMain entity : updateList) {
                    Map<String,Object> tMap = new HashMap<>();
                    tMap.put("cid",entity.getC_id());
                    tMap.put("qid",entity.getQ_id());
                    tMap.put("version",entity.getQ_time());
                    resultList.add(tMap);
                }
            }
            return JsonResultUtils.success(resultList);
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.error("update main cloud error：{},json:{}", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

    @RequestMapping(value = "/syncCloudSubList.qunar", method = RequestMethod.POST)
    public JsonResult<?> syncCloudSubList(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            ArrayList<Map> tempInsertList = (ArrayList<Map>)map.get("insert");
            ArrayList<Map> tempUpdateList = (ArrayList<Map>)map.get("update");
            ArrayList<Map> resultList = new ArrayList<>();
            if (tempInsertList != null && tempInsertList.size() > 0) {
                ArrayList<QCloudSub> instertList = new ArrayList<>();
                for (Map<String, Object> instertMap : tempInsertList) {
                    Long qid = Long.valueOf(instertMap.get("qid").toString());
                    String csid = instertMap.get("csid").toString();
                    String title = (String) instertMap.get("title");
                    String desc = (String) instertMap.get("desc");
                    String content = (String) instertMap.get("content");
                    int type = Integer.valueOf(instertMap.get("type").toString());
                    int state = QCloudDataState.Normal.getValue();
                    Object ostate = instertMap.get("state");
                    if (ostate != null) {
                        state = Integer.valueOf(ostate.toString());
                    }
                    QCloudSub entity = new QCloudSub();
                    entity.setQ_id(qid);
                    entity.setCs_id(csid);
                    entity.setQs_user(user);
                    entity.setQs_title(title);
                    entity.setQs_introduce(desc);
                    entity.setQs_content(content);
                    entity.setQs_type(type);
                    entity.setQs_state(state);
                    entity.setQs_time(System.currentTimeMillis());
                    instertList.add(entity);
                }
                cloudDao.bulkInsertQCloudSub(instertList);
                for (QCloudSub entity : instertList) {
                    Map<String,Object> tMap = new HashMap<>();
                    tMap.put("csid",entity.getCs_id());
                    tMap.put("qsid",entity.getQs_id());
                    tMap.put("version",entity.getQs_time());
                    resultList.add(tMap);
                }
            }
            if (tempUpdateList != null && tempUpdateList.size() > 0) {
                ArrayList<QCloudSub> updateList = new ArrayList<>();
                for (Map<String, Object> updateMap : tempUpdateList) {
                    long qsid = Long.valueOf(updateMap.get("qsid").toString());
                    String csid = updateMap.get("csid").toString();
                    String title = (String) updateMap.get("title");
                    String desc = (String) updateMap.get("desc");
                    String content = (String) updateMap.get("content");
                    int type = Integer.valueOf(updateMap.get("type").toString());
                    int state = QCloudDataState.Normal.getValue();
                    Object ostate = updateMap.get("state");
                    if (ostate != null) {
                        state = Integer.valueOf(ostate.toString());
                    }
                    QCloudSub entity = new QCloudSub();
                    entity.setQs_id(qsid);
                    entity.setCs_id(csid);
                    entity.setQs_title(title);
                    entity.setQs_introduce(desc);
                    entity.setQs_content(content);
                    entity.setQs_type(type);
                    entity.setQs_state(state);
                    entity.setQs_time(System.currentTimeMillis());
                    updateList.add(entity);
                }
                cloudDao.bulkUpdateQCloudSub(updateList);
                for (QCloudSub entity : updateList) {
                    Map<String,Object> tMap = new HashMap<>();
                    tMap.put("csid",entity.getCs_id());
                    tMap.put("qsid",entity.getQs_id());
                    tMap.put("version",entity.getQs_time());
                    resultList.add(tMap);
                }
            }
            return JsonResultUtils.success(resultList);
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.error("update main cloud error:{},json:{} ", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

    @RequestMapping(value = "/updateMain.qunar",method = RequestMethod.POST)
    public JsonResult<?> updateMain(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            long qid = Long.valueOf(map.get("qid").toString());
            String title = (String) map.get("title");
            String desc = (String) map.get("desc");
            String content = (String) map.get("content");

            QCloudMain mainCloud = cloudDao.selectQCloudMainWithId(user,qid);
            if (mainCloud == null) {
                return JsonResultUtils.fail(0, "传入的QID不存在或者不归你所有");
            }
            if (mainCloud.getQ_state() == QCloudDataState.Delete.getValue()) {
                return JsonResultUtils.fail(0, "该条记录已删除");
            }
            if (mainCloud.getQ_state() == QCloudDataState.Basket.getValue()) {
                return JsonResultUtils.fail(0, "该条记录已在废纸篓");
            }
            if (title == null)
                title = mainCloud.getQ_title();
            if (desc == null)
                desc = mainCloud.getQ_introduce();
            if (content == null)
                content = mainCloud.getQ_content();

            // 更新主记录
            QCloudMain pModule = new QCloudMain();
            pModule.setQ_id(qid);
            pModule.setQ_title(title);
            pModule.setQ_introduce(desc);
            pModule.setQ_content(content);
            pModule.setQ_time(System.currentTimeMillis());
            cloudDao.updateQCloudMain(pModule);

            Map<String, Object> historyMap = new HashMap<>();
            historyMap.put("type",mainCloud.getQ_type());
            historyMap.put("title",pModule.getQ_title());
            historyMap.put("desc",pModule.getQ_introduce());
            historyMap.put("content",pModule.getQ_content());
            String contentJson = mapper.writeValueAsString(historyMap);

            // 插入历史记录
            QCloudMainHistory phModule = new QCloudMainHistory();
            phModule.setQ_id(pModule.getQ_id());
            phModule.setQh_content(contentJson);
            phModule.setQh_time(pModule.getQ_time());
            phModule.setQh_state(QCloudDataState.Update.getValue());
            cloudDao.insertQCloudMainHistory(phModule);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("qid",pModule.getQ_id());
            resultMap.put("version",pModule.getQ_time());
            return JsonResultUtils.success(resultMap);
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.debug("update main cloud error 异常信息{}，json数据{}", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

    private void  updateMainState(ObjectMapper mapper, QCloudMain mainCloud, int state, long stime) throws JsonProcessingException {
        // 更新主记录
        cloudDao.updateQCloudMainWithState(mainCloud.getQ_id(),state,stime);
        cloudDao.updateQCloudSubForMainIdWithState(mainCloud.getQ_id(),state,stime);

        Map<String, Object> historyMap = new HashMap<>();
        historyMap.put("type",mainCloud.getQ_type());
        historyMap.put("title",mainCloud.getQ_title());
        historyMap.put("desc",mainCloud.getQ_introduce());
        historyMap.put("content",mainCloud.getQ_content());
        String contentJson = mapper.writeValueAsString(historyMap);

        // 插入历史记录
        QCloudMainHistory phModule = new QCloudMainHistory();
        phModule.setQ_id(mainCloud.getQ_id());
        phModule.setQh_content(contentJson);
        phModule.setQh_time(stime);
        phModule.setQh_state(state);
        cloudDao.insertQCloudMainHistory(phModule);
    }

    @RequestMapping(value = "/deleteMain.qunar",method = RequestMethod.POST)
    public JsonResult<?> deleteMain(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            long qid = Long.valueOf(map.get("qid").toString());

            QCloudMain mainCloud = cloudDao.selectQCloudMainWithId(user,qid);
            if (mainCloud == null) {
                return JsonResultUtils.fail(0, "传入的QID不存在或者不归你所有");
            }
            long stime = System.currentTimeMillis();
            int dataState = QCloudDataState.Delete.getValue();
            // 更新数据
            this.updateMainState(mapper,mainCloud,dataState,stime);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("qid",mainCloud.getQ_id());
            resultMap.put("version",stime);
            return JsonResultUtils.success(resultMap);
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.error("delete main cloud error {},json:{}", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

    @RequestMapping(value = "/collectionMain.qunar",method = RequestMethod.POST)
    public JsonResult<?> collectionMain(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            long qid = Long.valueOf(map.get("qid").toString());

            QCloudMain mainCloud = cloudDao.selectQCloudMainWithId(user,qid);
            if (mainCloud == null) {
                return JsonResultUtils.fail(0, "传入的QID不存在或者不归你所有");
            }

            long stime = System.currentTimeMillis();
            int dataState = QCloudDataState.Collection.getValue();

            // 更新数据
            this.updateMainState(mapper,mainCloud,dataState,stime);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("qid",mainCloud.getQ_id());
            resultMap.put("version",stime);
            return JsonResultUtils.success(resultMap);
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.debug("delete main cloud error ", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

    @RequestMapping(value = "/moveToBasketMain.qunar",method = RequestMethod.POST)
    public JsonResult<?> moveToBasketMain(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            long qid = Long.valueOf(map.get("qid").toString());

            QCloudMain mainCloud = cloudDao.selectQCloudMainWithId(user,qid);
            if (mainCloud == null) {
                return JsonResultUtils.fail(0, "传入的QID不存在或者不归你所有");
            }

            long stime = System.currentTimeMillis();
            int dataState = QCloudDataState.Basket.getValue();

            // 更新数据
            this.updateMainState(mapper,mainCloud,dataState,stime);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("qid",mainCloud.getQ_id());
            resultMap.put("version",stime);
            return JsonResultUtils.success(resultMap);
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.error("delete main cloud error:{},json:{}", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

    @RequestMapping(value ={"/moveOutBasketMain.qunar","/cancelCollectionMain.qunar"},method = RequestMethod.POST)
    public JsonResult<?> moveOutBasketMain(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            long qid = Long.valueOf(map.get("qid").toString());

            QCloudMain mainCloud = cloudDao.selectQCloudMainWithId(user,qid);
            if (mainCloud == null) {
                return JsonResultUtils.fail(0, "传入的QID不存在或者不归你所有");
            }

            long stime = System.currentTimeMillis();
            int dataState = QCloudDataState.Normal.getValue();

            // 更新数据
            this.updateMainState(mapper,mainCloud,dataState,stime);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("qid",mainCloud.getQ_id());
            resultMap.put("version",stime);
            return JsonResultUtils.success(resultMap);
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.error("delete main cloud error {},json:{}", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

    @RequestMapping(value = "/saveToSub.qunar",method = RequestMethod.POST)
    public JsonResult<?> saveToSub(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            long qid = Long.valueOf(map.get("qid").toString());
            int type = Integer.valueOf(map.get("type").toString());
            String title = (String) map.get("title");
            String desc = (String) map.get("desc");
            String content = (String) map.get("content");

            if (this.checkQidInvalid(user,qid)) {
                return JsonResultUtils.fail(0, "传入的QID不存在或者不归你所有");
            }
            // 插入主记录
            QCloudSub pModule = new QCloudSub();
            pModule.setQ_id(qid);
            pModule.setQs_user(user);
            pModule.setQs_type(type);
            pModule.setQs_title(title);
            pModule.setQs_introduce(desc);
            pModule.setQs_content(content);
            pModule.setQs_time(System.currentTimeMillis());
            pModule.setQs_state(QCloudDataState.Normal.getValue());
            cloudDao.insertQCloudSub(pModule);

            // 插入历史记录
            QCloudSubHistory phModule = new QCloudSubHistory();
            phModule.setQs_id(pModule.getQs_id());
            phModule.setQh_content(json);
            phModule.setQh_time(pModule.getQs_time());
            phModule.setQh_state(QCloudDataState.Create.getValue());
            cloudDao.insertQCloudSubHistory(phModule);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("qsid",pModule.getQs_id());
            resultMap.put("version",pModule.getQs_time());
            return JsonResultUtils.success(resultMap);
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.debug("save sub cloud error,异常信息:{},json:{}", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

    @RequestMapping(value = "/updateSub.qunar",method = RequestMethod.POST)
    public JsonResult<?> updateSub(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            long qsid = Long.valueOf(map.get("qsid").toString());
            String title = (String) map.get("title");
            String desc = (String) map.get("desc");
            String content = (String) map.get("content");

            QCloudSub subCloud = cloudDao.selectQCloudSubWithId(user,qsid);
            if (subCloud == null) {
                return JsonResultUtils.fail(0, "传入的QSID不存在或者不归你所有");
            }
            if (subCloud.getQs_state() == QCloudDataState.Delete.getValue()) {
                return JsonResultUtils.fail(0, "该条记录已删除");
            }
            if (subCloud.getQs_state() == QCloudDataState.Basket.getValue()) {
                return JsonResultUtils.fail(0, "该条记录已在废纸篓");
            }
            if (title == null) {
                title = subCloud.getQs_title();
            }

            if (desc == null) {
                desc = subCloud.getQs_introduce();
            }

            if (content == null) {
                content = subCloud.getQs_content();
            }

            // 插入主记录
            QCloudSub pModule = new QCloudSub();
            pModule.setQs_id(qsid);
            pModule.setQs_title(title);
            pModule.setQs_introduce(desc);
            pModule.setQs_content(content);
            pModule.setQs_time(System.currentTimeMillis());
            cloudDao.updateQCloudSub(pModule);

            Map<String, Object> historyMap = new HashMap<>();
            historyMap.put("qid",subCloud.getQ_id());
            historyMap.put("type",subCloud.getQs_type());
            historyMap.put("title",pModule.getQs_title());
            historyMap.put("desc",pModule.getQs_introduce());
            historyMap.put("content",pModule.getQs_content());
            String contentJson = mapper.writeValueAsString(historyMap);

            // 插入历史记录
            QCloudSubHistory phModule = new QCloudSubHistory();
            phModule.setQs_id(pModule.getQs_id());
            phModule.setQh_content(contentJson);
            phModule.setQh_time(pModule.getQs_time());
            phModule.setQh_state(QCloudDataState.Update.getValue());
            cloudDao.insertQCloudSubHistory(phModule);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("qsid",pModule.getQs_id());
            resultMap.put("version",pModule.getQs_time());
            return JsonResultUtils.success(resultMap);
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.debug("update sub cloud error 异常信息:{},json:{}", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

    private  void updateSubState(ObjectMapper mapper, QCloudSub subCloud, int dataState, long stime) throws JsonProcessingException {
        // 更新主记录
        cloudDao.updateQCloudSubWithState(subCloud.getQs_id(),dataState,stime);

        Map<String, Object> historyMap = new HashMap<>();
        historyMap.put("type",subCloud.getQs_type());
        historyMap.put("title",subCloud.getQs_title());
        historyMap.put("desc",subCloud.getQs_introduce());
        historyMap.put("content",subCloud.getQs_content());
        String contentJson = mapper.writeValueAsString(historyMap);

        // 插入历史记录
        QCloudSubHistory phModule = new QCloudSubHistory();
        phModule.setQs_id(subCloud.getQs_id());
        phModule.setQh_content(contentJson);
        phModule.setQh_time(stime);
        phModule.setQh_state(dataState);
        cloudDao.insertQCloudSubHistory(phModule);
    }

    @RequestMapping(value = "/deleteSub.qunar",method = RequestMethod.POST)
    public JsonResult<?> deleteSub(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            long qsid = Long.valueOf(map.get("qsid").toString());

            QCloudSub subCloud = cloudDao.selectQCloudSubWithId(user,qsid);
            if (subCloud == null) {
                return JsonResultUtils.fail(0, "传入的QSID不存在或者不归你所有");
            }

            long stime = System.currentTimeMillis();
            int dataState = QCloudDataState.Delete.getValue();

            // 更新数据
            this.updateSubState(mapper,subCloud,dataState,stime);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("qsid",subCloud.getQs_id());
            resultMap.put("version",stime);
            return JsonResultUtils.success(resultMap);
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.debug("delete sub cloud error ", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

    @RequestMapping(value = "/collectionSub.qunar",method = RequestMethod.POST)
    public JsonResult<?> collectionSub(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            long qsid = Long.valueOf(map.get("qsid").toString());

            QCloudSub subCloud = cloudDao.selectQCloudSubWithId(user,qsid);
            if (subCloud == null) {
                return JsonResultUtils.fail(0, "传入的QSID不存在或者不归你所有");
            }

            long stime = System.currentTimeMillis();
            int dataState = QCloudDataState.Collection.getValue();

            // 更新数据
            this.updateSubState(mapper,subCloud,dataState,stime);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("qsid",subCloud.getQs_id());
            resultMap.put("version",stime);
            return JsonResultUtils.success(resultMap);
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.error("delete sub cloud error {},json:{}", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

    @RequestMapping(value = "/moveToBasketSub.qunar",method = RequestMethod.POST)
    public JsonResult<?> moveToBasketSub(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            long qsid = Long.valueOf(map.get("qsid").toString());

            QCloudSub subCloud = cloudDao.selectQCloudSubWithId(user,qsid);
            if (subCloud == null) {
                return JsonResultUtils.fail(0, "传入的QSID不存在或者不归你所有");
            }

            long stime = System.currentTimeMillis();
            int dataState = QCloudDataState.Basket.getValue();

            // 更新数据
            this.updateSubState(mapper,subCloud,dataState,stime);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("qsid",subCloud.getQs_id());
            resultMap.put("version",stime);
            return JsonResultUtils.success(resultMap);
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.error("delete sub cloud error {},json:{}", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

    @RequestMapping(value ={"/moveOutBasketSub.qunar","/cancelCollectionSub.qunar"},method = RequestMethod.POST)
    public JsonResult<?> moveOutBasketSub(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            long qsid = Long.valueOf(map.get("qsid").toString());

            QCloudSub subCloud = cloudDao.selectQCloudSubWithId(user,qsid);
            if (subCloud == null) {
                return JsonResultUtils.fail(0, "传入的QSID不存在或者不归你所有");
            }

            long stime = System.currentTimeMillis();
            int dataState = QCloudDataState.Normal.getValue();

            // 更新数据
            this.updateSubState(mapper,subCloud,dataState,stime);

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("qsid",subCloud.getQs_id());
            resultMap.put("version",stime);
            return JsonResultUtils.success(resultMap);
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.error("delete sub cloud error {}，json:{}", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

    @RequestMapping(value = "/getCloudMain.qunar",method = RequestMethod.POST)
    public JsonResult<?> getCloudMain(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息");
            }
            long version = 0;
            int type = -1;
            if (json.length() > 0) {
                ObjectMapper mapper = new ObjectMapper();
                Map map = mapper.readValue(json, Map.class);
                Object vo = map.get("version");
                version = vo == null?0:Long.valueOf(vo.toString());
                Object typeObject = map.get("type");
                if (typeObject != null) {
                    type = Integer.valueOf(typeObject.toString());
                }
            }
            List<QCloudMain> list = new ArrayList<>();
            if (type >= 0) {
                list = cloudDao.selectQCloudMainWithType(user,version,type);
            } else  {
                list = cloudDao.selectQCloudMain(user,version);
            }
            if (list.size() > 0) {
                List<Map<String,Object>> result = new ArrayList<>();
                for (QCloudMain cloud : list){
                    Map<String,Object> cloudMap = new HashMap<>();
                    cloudMap.put("qid",cloud.getQ_id());
                    cloudMap.put("type",cloud.getQ_type());
                    cloudMap.put("title",cloud.getQ_title());
                    cloudMap.put("desc",cloud.getQ_introduce());
                    cloudMap.put("content",cloud.getQ_content());
                    cloudMap.put("version",cloud.getQ_time());
                    cloudMap.put("state",cloud.getQ_state());

                    result.add(cloudMap);
                }
                return JsonResultUtils.success(result);
            } else  { 
                return JsonResultUtils.success(new ArrayList<>());
            }
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.debug("get main cloud error ", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

    @RequestMapping(value = "/getCloudSub.qunar",method = RequestMethod.POST)
    public JsonResult<?> getCloudSub(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            Object idO = map.get("qid");
            if (idO == null) return JsonResultUtils.fail(0, "QID不能为空");
            long qid = Long.valueOf(idO.toString());
            Object vo = map.get("version");
            long version = vo == null?0:Long.valueOf(vo.toString());
            if (this.checkQidInvalid(user,qid)) {
                return JsonResultUtils.fail(0, "传入的QID不存在或者不归你所有");
            }
            List<QCloudSub> list= new ArrayList<>();
            Object type = map.get("type");
            if (type != null) {
                list = cloudDao.selectQCloudSubWithType(qid,version,Integer.valueOf(type.toString()));
            } else  {
                list = cloudDao.selectQCloudSub(qid,version);
            }
            if (list.size() > 0) {
                List<Map<String,Object>> result = new ArrayList<>();
                for (QCloudSub cloud : list){
                    Map<String,Object> cloudMap = new HashMap<>();
                    cloudMap.put("qid",cloud.getQ_id());
                    cloudMap.put("qsid",cloud.getQs_id());
                    cloudMap.put("type",cloud.getQs_type());
                    cloudMap.put("title",cloud.getQs_title());
                    cloudMap.put("desc",cloud.getQs_introduce());
                    cloudMap.put("content",cloud.getQs_content());
                    cloudMap.put("version",cloud.getQs_time());
                    cloudMap.put("state",cloud.getQs_state());
                    result.add(cloudMap);
                }
                return JsonResultUtils.success( result);
            } else  {
                return JsonResultUtils.success(new ArrayList<>());
            }
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.debug("get sub cloud error {},json:{}", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

    @RequestMapping(value = "/getCloudMainHistory.qunar",method = RequestMethod.POST)
    public JsonResult<?> getCloudMainHistory(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            long qid = Long.valueOf(map.get("qid").toString());
            if (this.checkQidInvalid(user,qid)) {
                return JsonResultUtils.fail(0, "传入的QID不存在或者不归你所有");
            }

            List<QCloudMainHistory> list = cloudDao.selectQCloudMainHistory(qid);
            if (list.size() > 0) {
                List<Map> result = new ArrayList<>();
                for (QCloudMainHistory cloud : list){
                    Map cloudMap = mapper.readValue(cloud.getQh_content(),Map.class);
                    cloudMap.put("version",cloud.getQh_time());
                    cloudMap.put("qid",cloud.getQ_id());
                    cloudMap.put("qhid",cloud.getQh_id());
                    cloudMap.put("state",cloud.getQh_state());
                    result.add(cloudMap);
                }
                return JsonResultUtils.success(result);
            } else  {
                return JsonResultUtils.success(new ArrayList<>());
            }
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.error("save main cloud history error:{},json:{} ", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

    @RequestMapping(value = "/getCloudSubHistory.qunar",method = RequestMethod.POST)
    public JsonResult<?> getCloudSubHistory(@RequestBody String json) {
        String stackTrace = "";
        try {
            String user = this.getFromUser();
            if (user == null || user.length() <= 0) {
                return JsonResultUtils.fail(0, "无有效用户信息");
            }
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(json, Map.class);
            long qsid = Long.valueOf(map.get("qsid").toString());
            if (this.checkQSidInvalid(user,qsid)) {
                return JsonResultUtils.fail(0, "传入的QSID不存在或者不归你所有");
            }
            List<QCloudSubHistory> list = cloudDao.selectQCloudSubHistory(qsid);
            if (list.size() > 0) {
                List<Map> result = new ArrayList<>();
                for (QCloudSubHistory cloud : list){
                    Map cloudMap = mapper.readValue(cloud.getQh_content(),Map.class);
                    cloudMap.put("version",cloud.getQh_time());
                    cloudMap.put("qsid",cloud.getQs_id());
                    cloudMap.put("qhid",cloud.getQh_id());
                    cloudMap.put("state",cloud.getQh_state());
                    result.add(cloudMap);
                }
                return JsonResultUtils.success(result);
            } else  {
                return JsonResultUtils.success(new ArrayList<>());
            }
        } catch (Exception e) {
            stackTrace = ExceptionUtils.getStackTrace(e);
            LOGGER.debug("save sub history cloud error:{},json:{} ", e , json);
        }
        return JsonResultUtils.fail(0, "服务器操作异常:\n "+stackTrace);
    }

}
