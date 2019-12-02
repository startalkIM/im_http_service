package com.qunar.qchat.controller;

import com.alibaba.fastjson.JSON;
import com.qunar.qchat.constants.Config;
import com.qunar.qchat.constants.TableConstants;
import com.qunar.qchat.dao.IClentConfigSyncDao;
import com.qunar.qchat.dao.model.ClientConfigModel;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.model.request.IncreClientConfigRequst;
import com.qunar.qchat.model.request.SetClientConfigRequst;
import com.qunar.qchat.model.result.ClientConfigInfo;
import com.qunar.qchat.model.result.IncreClientConfigResult;
import com.qunar.qchat.utils.HttpClientUtils;
import com.qunar.qchat.utils.JsonResultUtils;
import com.qunar.qchat.utils.RedisUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * create by hubo.hu (lex) at 2018/6/14
 */

@RequestMapping("/newapi/configuration/")
@RestController
public class QClientConfigController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QClientConfigController.class);

    private static Map<String, String> supportKey = new HashMap<>();

    private static final String kMarkupNames = "kMarkupNames";
    private static final String kCollectionCacheKey = "kCollectionCacheKey";
    private static final String kStickJidDic = "kStickJidDic";
    private static final String kNotificationSetting = "kNotificationSetting";
    private static final String kNoticeStickJidDic = "kNoticeStickJidDic";
    private static final String kChatColorInfo = "kChatColorInfo";
    private static final String kCurrentFontInfo = "kCurrentFontInfo";
    private static final String kChatMessageFontInfo = "kChatMessageFontInfo";
    private static final String kCustomConfigForMac = "kCustomConfigForMac";
    private static final String kAdrFontInfo = "kAdrFontInfo";
    private static final String kConversationParamDic = "kConversationParamDic";
    private static final String kQuickResponse = "kQuickResponse";
    private static final String kStarContact = "kStarContact";
    private static final String kBlackList = "kBlackList";
    private static final String kNoDisturbToggle = "kNoDisturbToggle";

    static {
        supportKey.put(kMarkupNames, "");//用户备注（通用）
        supportKey.put(kCollectionCacheKey, "");//收藏表情（通用）
        supportKey.put(kStickJidDic, "");//	置顶会话（通用）
        supportKey.put(kNotificationSetting, "");//客户端通知中心设置（通用）
        supportKey.put(kNoticeStickJidDic, "");//会话提醒（通用）
        supportKey.put(kChatColorInfo, "");//消息气泡颜色（IOS)
        supportKey.put(kCurrentFontInfo, "");//客户端字体（IOS)
        supportKey.put(kChatMessageFontInfo, "");//客户端字体（Mac）
        supportKey.put(kCustomConfigForMac, "");//Mac专用配置（Mac）
        supportKey.put(kAdrFontInfo, "");//客户端字体（Android）
        supportKey.put(kConversationParamDic, "");//众包需求（通用）
        supportKey.put(kQuickResponse, "");//快捷回复（通用）
        supportKey.put(kStarContact, "");//星标联系人（通用）
        supportKey.put(kBlackList, "");//黑名单（通用）
        supportKey.put(kNoDisturbToggle, "");//免打扰合并开关（通用）
    }

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IClentConfigSyncDao iClentConfigSyncDao;

    @RequestMapping(value = "/getincreclientconfig.qunar", method = RequestMethod.POST)
    public JsonResult<?> getIncreClientConfig(@RequestBody IncreClientConfigRequst increClientConfigRequst) {
        try {
            if (increClientConfigRequst == null || !increClientConfigRequst.isRequestValid()) {
                return JsonResultUtils.fail(0, "请求参数异常");
            }

            String table = TableConstants.TABLE_CLIENTCONFIG;
            String username = increClientConfigRequst.username;
            String host = increClientConfigRequst.host;
            long version = increClientConfigRequst.version;

            List<ClientConfigModel> clientConfigModelList = iClentConfigSyncDao.selectIncrementConfigData(table, username, host, version);
            long maxversion = iClentConfigSyncDao.selectMaxVersion(table, username, host);

            IncreClientConfigResult increClientConfigResult = parseConfigResult(clientConfigModelList, maxversion);

            return JsonResultUtils.success(increClientConfigResult);
        } catch (Exception e) {
            LOGGER.debug("catch error {}", e);
            return JsonResultUtils.fail(0, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }


    @RequestMapping(value = "/setclientconfig.qunar", method = RequestMethod.POST)
    public JsonResult<?> setClientConfig(@RequestBody SetClientConfigRequst setClientConfigRequst) {
        try {
            if (setClientConfigRequst == null || !setClientConfigRequst.isRequestValid()) {
                return JsonResultUtils.fail(0, "请求参数异常");
            }
            String table = TableConstants.TABLE_CLIENTCONFIG;
            String operate_plat = setClientConfigRequst.operate_plat;
            int version = setClientConfigRequst.version;
            int type = setClientConfigRequst.type;
            String username = setClientConfigRequst.username;
            String host = setClientConfigRequst.host;
            String resource = setClientConfigRequst.resource;

            List<SetClientConfigRequst.BatchProcess> batchProcess = setClientConfigRequst.batchProcess;
            if (batchProcess == null || batchProcess.size() <= 0) {
                String key = setClientConfigRequst.key;
                if(!supportKey.containsKey(key)){
                    return JsonResultUtils.fail(0, "客户端配置不支持该key");
                }
                String subkey = setClientConfigRequst.subkey;
                String value = setClientConfigRequst.value;
                return getJsonResult(key, table, subkey, username, host, value, operate_plat, type, version, resource);
            } else {//批量处理逻辑
                for(SetClientConfigRequst.BatchProcess batchProcess1 : batchProcess) {
                    if(!supportKey.containsKey(batchProcess1.key)) {
                        return JsonResultUtils.fail(0, "客户端配置不支持该key + " + batchProcess1.key);
                    }
                }
                return getBatchJsonResult(table, username, host, batchProcess, operate_plat, type, version, resource);
            }
        } catch (Exception e) {
            LOGGER.debug("catch error {}", e);
            return JsonResultUtils.fail(0, "服务器操作异常:\n " + ExceptionUtils.getStackTrace(e));
        }
    }

    private JsonResult<?> getJsonResult(String key, String table, String subkey, String username, String host, String value, String operate_plat, int type, long version, String resource) {
        //查询当前最大版本号
        long maxversion = iClentConfigSyncDao.selectMaxVersion(table, username, host);
        if (type == 1) {//设置
            if(iClentConfigSyncDao.updateClientConfig(table, username, host, key, subkey, value, operate_plat, 0, maxversion + 1) == 0) {
                iClentConfigSyncDao.insertClientConfig(table, username, host, key, subkey, value, operate_plat, maxversion + 1);
            }
            //查询增量数据,版本号大于数据库版本号，则返回全量数据
            List<ClientConfigModel> clientConfigModelList = iClentConfigSyncDao.selectIncrementConfigData(table, username, host, version > maxversion ? 0 : version);
            IncreClientConfigResult increClientConfigResult = parseConfigResult(clientConfigModelList, maxversion + 1);
            //发通知
            sendNotify(username + "@" + host, maxversion + 1, resource);
            setRedisCache(key, username, host, subkey, 0);
            return JsonResultUtils.success(increClientConfigResult);
        } else if (type == 2) {//删除取消
            if(iClentConfigSyncDao.updateClientConfig(table, username, host, key, subkey, value, operate_plat, 1, maxversion + 1) == 0) {
                return JsonResultUtils.fail(0, "操作失败，设置后才能取消");
            }

            //查询增量数据,版本号大于数据库版本号，则返回全量数据
            List<ClientConfigModel> clientConfigModelList = iClentConfigSyncDao.selectIncrementConfigData(table, username, host, version > maxversion ? 0 : version);
            IncreClientConfigResult increClientConfigResult = parseConfigResult(clientConfigModelList, maxversion + 1);
            //发通知
            sendNotify(username +"@" + host, maxversion + 1, resource);
            setRedisCache(key, username, host, subkey, 1);
            return JsonResultUtils.success(increClientConfigResult);
        } else {
            return JsonResultUtils.fail(0, "操作无效");
        }
    }

    private JsonResult<?> getBatchJsonResult (String table, String username, String host, List<SetClientConfigRequst.BatchProcess> batchProcess, String operate_plat, int type, long version, String resource){
        //去重
        Set<SetClientConfigRequst.BatchProcess> filterList = new LinkedHashSet<>();
        filterList.addAll(batchProcess);
        //查询当前最大版本号
        long maxversion = iClentConfigSyncDao.selectMaxVersion(table, username, host);

        if (type == 1) {//设置
            iClentConfigSyncDao.insertBatchClientConfig(table, username, host, filterList, operate_plat, maxversion + 1);

            //查询增量数据,版本号大于数据库版本号，则返回全量数据
            List<ClientConfigModel> clientConfigModelList = iClentConfigSyncDao.selectIncrementConfigData(table, username, host, version > maxversion ? 0 : version);
            IncreClientConfigResult increClientConfigResult = parseConfigResult(clientConfigModelList, maxversion + 1);

            sendNotify(username + "@" + host, maxversion + 1, resource);
            setBatchRedisCache(username, host, filterList, 0);
            return JsonResultUtils.success(increClientConfigResult);
        } else if (type == 2) {//删除取消
            if(iClentConfigSyncDao.updateBatchClientConfig(table, username, host, filterList, 1, maxversion + 1, operate_plat) == 0) {
                return JsonResultUtils.fail(0, "操作失败，设置后才能取消");
            }

            //查询增量数据,版本号大于数据库版本号，则返回全量数据
            List<ClientConfigModel> clientConfigModelList = iClentConfigSyncDao.selectIncrementConfigData(table, username, host, version > maxversion ? 0 : version);

            IncreClientConfigResult increClientConfigResult = parseConfigResult(clientConfigModelList, maxversion + 1);

            sendNotify(username + "@" + host, maxversion + 1, resource);
            setBatchRedisCache(username, host, filterList, 1);
            return JsonResultUtils.success(increClientConfigResult);
        } else {
            return JsonResultUtils.fail(0, "操作无效");
        }
    }

    /**
     * 发送通知给客户端同步个人配置
     * @param userid
     * @param version
     */
    private void sendNotify(String userid, long version, String resource) {
        Dictionary<String, Object> args = new Hashtable<>();
        args.put("from", userid);
        args.put("to", userid);
        args.put("category", "6");

        Dictionary<String, Object> data = new Hashtable<>();
        data.put("version", version);
        if(!TextUtils.isEmpty(resource)) {
            data.put("resource", resource);
        }

        args.put("data", JSON.toJSONString(data));
        String ret = HttpClientUtils.postJson(Config.URL_SEND_NOTIFY, JSON.toJSONString(args));
        LOGGER.info("send presense to :{}, ret;{}", JSON.toJSONString(args), ret);
    }

    /**
     * 批量设置redis缓存
     * @param username
     * @param host
     * @param batchProcess
     * @param value  0：设置；  1：取消
     */
    private void setBatchRedisCache(String username, String host, Set<SetClientConfigRequst.BatchProcess> batchProcess, int value) {
        for(SetClientConfigRequst.BatchProcess batchProcess1 : batchProcess){
            setRedisCache(batchProcess1.key, username, host, batchProcess1.subkey, value);
        }
    }

    /**
     * 设置redis缓存
     * @param key
     * @param username
     * @param host
     * @param subkey
     * @param value 0：设置；  1：取消
     */
    private void setRedisCache(String key, String username, String host, String subkey, int value) {
        setSubscribe(key, username, host, subkey, value);
        setBlackListRedis(key, username, host, subkey, value);
    }

    /**
     * 修改会话通知redis缓存，pushservice使用
     * @param username
     * @param host
     * @param mucname
     * @param value 0：设置；  1：取消
     */
    private void setSubscribe(String key, String username, String host, String mucname, int value) {
        if(kNoticeStickJidDic.equalsIgnoreCase(key)){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("user:grop:subcribe:");
            stringBuilder.append(username);
            stringBuilder.append("_");
            stringBuilder.append(host);
            stringBuilder.append("_");
            stringBuilder.append(mucname);
            redisUtil.set(9, stringBuilder.toString(), value, 2 * 60 * 60, TimeUnit.SECONDS);
        }
    }

    /**
     * 更新黑名单reids缓存
     * @param key
     * @param username
     * @param host
     * @param targetuserser
     * @param value 0：设置；  1：取消
     */
    private void setBlackListRedis(String key, String username, String host, String targetuserser, int value) {
        if (kBlackList.equalsIgnoreCase(key)) {
            if(value == 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("blacklist:");
                stringBuilder.append(username);
                stringBuilder.append("@");
                stringBuilder.append(host);
                redisUtil.hPut(11, stringBuilder.toString(), targetuserser, value+"");
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("blacklist:");
                stringBuilder.append(username);
                stringBuilder.append("@");
                stringBuilder.append(host);
                redisUtil.hDel(11, stringBuilder.toString(), targetuserser);
            }
        }
    }

    /**
     * 解析，按key分类
     * @param clientConfigModels
     * @param maxversion
     * @return
     */
    private IncreClientConfigResult parseConfigResult(List<ClientConfigModel> clientConfigModels, long maxversion) {
        IncreClientConfigResult increClientConfigResult = new IncreClientConfigResult();
        increClientConfigResult.version = maxversion;

        Map<String, ClientConfigInfo> infosMap = new HashMap<>();
        for (ClientConfigModel clientConfigModel : clientConfigModels) {
            if (infosMap.containsKey(clientConfigModel.configkey)) {
                ClientConfigInfo clientConfigInfo = infosMap.get(clientConfigModel.configkey);
                ClientConfigInfo.Info info = new ClientConfigInfo.Info();
                info.subkey = clientConfigModel.subkey;
                info.configinfo = clientConfigModel.configinfo;
                info.isdel = clientConfigModel.isdel;
                clientConfigInfo.infos.add(info);
            } else {
                ClientConfigInfo clientConfigInfo = new ClientConfigInfo();
                clientConfigInfo.key = clientConfigModel.configkey;
                List<ClientConfigInfo.Info> infos = new ArrayList<>();
                ClientConfigInfo.Info info = new ClientConfigInfo.Info();
                info.subkey = clientConfigModel.subkey;
                info.configinfo = clientConfigModel.configinfo;
                info.isdel = clientConfigModel.isdel;
                infos.add(info);
                clientConfigInfo.infos = infos;

                infosMap.put(clientConfigModel.configkey, clientConfigInfo);
            }

        }
        List<ClientConfigInfo> clientConfigInfoList = new ArrayList<>();

        for (ClientConfigInfo clientConfigInfo : infosMap.values()) {
            clientConfigInfoList.add(clientConfigInfo);
        }
        increClientConfigResult.clientConfigInfos = clientConfigInfoList;
        return increClientConfigResult;
    }
}