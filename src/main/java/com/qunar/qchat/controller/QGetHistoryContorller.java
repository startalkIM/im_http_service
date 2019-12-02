package com.qunar.qchat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.qunar.qchat.dao.IGetMsgDao;
import com.qunar.qchat.dao.model.QGetMsg;
import com.qunar.qchat.dao.model.QGetMucMsg;
import com.qunar.qchat.dao.model.QMsgReadFlag;
import com.qunar.qchat.dao.model.QMucTime;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.utils.CookieUtils;
import com.qunar.qchat.utils.JsonResultUtils;
import com.qunar.qchat.utils.Xml2Json;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by admin on 14/07/2017.
 *
 *
 */

@RequestMapping("/newapi/")
@RestController
public class QGetHistoryContorller {

    private static final Logger LOGGER = LoggerFactory.getLogger(QGetHistoryContorller.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IGetMsgDao getMsgDao;


    @RequestMapping(value = "/getmsgs.qunar",method = RequestMethod.POST)
    public JsonResult<?> getMsg(HttpServletRequest request, @RequestBody String json) {

        List<Map<String, Object>> listRes = new ArrayList<>();

        try{
            if (json.length() > 0) {


                ObjectMapper mapper = new ObjectMapper();
                Map map = mapper.readValue(json, Map.class);
                List<QGetMsg> qlist = new ArrayList<>();

                String fuser = (String) map.get("from");
                String tuser = (String) map.get("to");
                String fhost = (String) map.get("fhost");
                String thost = (String) map.get("thost");
                String flag = "";
                if (map.containsKey("f")) {
                    flag = map.get("f").toString();
                }

                String include = "";
                if (map.containsKey("include")) {
                    include = map.get("include").toString();
                }

                String direction = conversionDir(map.get("direction").toString(), include);
                String turn = conversionAsc(direction);

                Map<String,Object> cookies = CookieUtils.getUserbyCookie(request);
                String U = cookies.get("u").toString();
                if (Strings.isNullOrEmpty(U) || (!U.equals(fuser) && !U.equals(tuser) )){
                    return JsonResultUtils.fail(3,"ukey not match");
                }

                long num = Long.valueOf(map.get("num").toString());
                String domain = map.get("domain").toString();
                Double time = getCorrectTime(map.get("time").toString());
                Double mtime = getMonthTime();

                if (time  > mtime ) {
                    qlist = getMsgDao.selectMsgbyTime(fuser, fhost, tuser, thost, direction, turn, num, time);
                }
                if (qlist.size() == 0 || time < mtime){
                    qlist = getMsgDao.selectMsgBackupbyTime(fuser, fhost, tuser, thost, direction, turn, num, time);
                }

                if (turn.equals("desc") && qlist != null){
                    Collections.reverse(qlist);
                }

                for (QGetMsg qmsg:qlist) {
                    try {
                        Map<String, Object> resultMap = new HashMap<>();
                        String body = qmsg.getM_body();
                        resultMap = Xml2Json.xmppToMap(body);
                        resultMap.put("from", qmsg.getM_from());
                        resultMap.put("from_host", qmsg.getFrom_host());
                        resultMap.put("to", qmsg.getM_to());
                        resultMap.put("to_host", qmsg.getTo_host());
                        if ("t".equals(flag)) {
                            resultMap.put("read_flag", qmsg.getRead_flag());
                        } else {
                            if (qmsg.getRead_flag() != 0) {
                                resultMap.put("read_flag", 1);
                            } else {
                                resultMap.put("read_flag", 0);
                            }
                        }
                        resultMap.put("t", qmsg.getDate_part());
                        listRes.add(resultMap);
                    } catch (Exception e) {
                        LOGGER.error("a bad message: {}" , qmsg.getM_body());
                    }
                }

            }
        }catch (Exception e){
            LOGGER.error("catch error {} {}", ExceptionUtils.getStackTrace(e) , json);
            return JsonResultUtils.fail(0, "服务器操作异常:\n "+ExceptionUtils.getStackTrace(e));
        }
        return JsonResultUtils.success(listRes);
    }

    @RequestMapping(value = "/getreadflag.qunar",method = RequestMethod.POST)
    public JsonResult<?> getReadflag(HttpServletRequest request, @RequestBody String json) {

        List<Map<String, Object>> listRes = new ArrayList<>();

        try{
            if (json.length() > 0) {


                ObjectMapper mapper = new ObjectMapper();
                Map map = mapper.readValue(json, Map.class);
                List<QMsgReadFlag> qlist = new ArrayList<>();

                Double time = getCorrectTime(map.get("time").toString());
                String domain = map.get("domain").toString();
                long id = 0;
                if (map.containsKey("id")) {
                    id = Long.valueOf(map.get("id").toString());
                }

                Map<String,Object> cookies = CookieUtils.getUserbyCookie(request);
                String U = cookies.get("u").toString();

                qlist = getMsgDao.get_readflag(U, time, id);

                for (QMsgReadFlag qmsg:qlist) {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("msgid",qmsg.getMsg_id());
                    resultMap.put("readflag",qmsg.getRead_flag());
                    listRes.add(resultMap);
                }

            }
        }catch (Exception e){
            LOGGER.error("catch error {} {}", ExceptionUtils.getStackTrace(e) , json);
            return JsonResultUtils.fail(0, "服务器操作异常:\n "+ExceptionUtils.getStackTrace(e));
        }
        return JsonResultUtils.success(listRes);
    }

    @RequestMapping(value = "/getconsultmsgs.qunar",method = RequestMethod.POST)
    public JsonResult<?> get_consult_msg(@RequestBody String json) {

        List<Map<String, Object>> listRes = new ArrayList<>();

        try{
            if (json.length() > 0) {
                ObjectMapper mapper = new ObjectMapper();
                Map map = mapper.readValue(json, Map.class);
                List<QGetMsg> qlist = new ArrayList<>();

                String fuser = (String) map.get("from");
                String tuser = (String) map.get("to");
                String virtual = (String) map.get("virtual");
                String fhost = (String) map.get("fhost");
                String thost = (String) map.get("thost");
                String flag = "";
                if (map.containsKey("f")) {
                    flag = map.get("f").toString();
                }
                String include = "";
                if (map.containsKey("include")) {
                    include = map.get("include").toString();
                }

                String direction = conversionDir(map.get("direction").toString(), include);
                String turn = conversionAsc(direction);

                Map<String,Object> cookies = CookieUtils.getUserbyCookie(request);
                String U = cookies.get("u").toString();
                if (Strings.isNullOrEmpty(U) || (!U.equals(fuser) && !U.equals(tuser) && !U.equals(virtual) )){
                   return JsonResultUtils.fail(3,"ukey not match");
                }

                long num = Long.valueOf(map.get("num").toString());
                String domain = map.get("domain").toString();
                Double time = getCorrectTime(map.get("time").toString());
                Double mtime = getMonthTime();

                String real = "{"+ tuser + "@" + thost + "}";

                if (time  > mtime ) {
                    qlist = getMsgDao.selectConsultMsgbyTime(fuser, fhost, tuser, thost,virtual,real, direction, turn, num, time);
                }
                if (qlist.size() == 0 || time < mtime){
                    qlist = getMsgDao.selectConsultMsgbyTime(fuser, fhost, tuser, thost,virtual,real, direction, turn, num, time);
                }


                if (turn.equals("desc") && qlist != null){
                    Collections.reverse(qlist);
                }

                for (QGetMsg qmsg:qlist) {
                    try {
                        Map<String, Object> resultMap = new HashMap<>();
                        String body = qmsg.getM_body();
                        resultMap = Xml2Json.xmppToMap(body);
                        resultMap.put("from", qmsg.getM_from());
                        resultMap.put("from_host", qmsg.getFrom_host());
                        resultMap.put("to", qmsg.getM_to());
                        resultMap.put("to_host", qmsg.getTo_host());
                        if ("t".equals(flag)) {
                            resultMap.put("read_flag", qmsg.getRead_flag());
                        } else {
                            if (qmsg.getRead_flag() != 0) {
                                resultMap.put("read_flag", 1);
                            } else {
                                resultMap.put("read_flag", 0);
                            }
                        }
                        listRes.add(resultMap);
                    } catch (Exception e) {
                        LOGGER.error("a bad message: {}", qmsg.getM_body());
                    }
                }

            }
        }catch (Exception e){
            LOGGER.error("catch error {} {}", ExceptionUtils.getStackTrace(e) , json);
            return JsonResultUtils.fail(0, "服务器操作异常:\n "+ExceptionUtils.getStackTrace(e));
        }
        return JsonResultUtils.success(listRes);
    }


    @RequestMapping(value = "/gethistory.qunar",method = RequestMethod.POST)
    public JsonResult<?> gethistory(@RequestBody String json) {

        try{
            LOGGER.info("gethistory.qunar start ");
            if (json.length() > 0) {
                ObjectMapper mapper = new ObjectMapper();
                List<QGetMsg> qlist = new ArrayList<>();

                Map map = mapper.readValue(json, Map.class);

                String user = (String) map.get("user");
                String host = (String) map.get("host");
                String domain = map.get("domain").toString();
                String flag = "";
                if (map.containsKey("f")) {
                    flag = map.get("f").toString();
                }
                Double time = getCorrectTime(map.get("time").toString());
                long num = Long.valueOf(map.get("num").toString());

                Map<String,Object> cookies = CookieUtils.getUserbyCookie(request);
                String U = cookies.get("u").toString();
                if (Strings.isNullOrEmpty(U) || (!U.equals(user))){
                    return JsonResultUtils.fail(3,"ukey not match");

                }

                qlist = getMsgDao.selectHistory(user,host,time,num);

                List<Map<String, Object>> listRes = new ArrayList<>();

                for (QGetMsg qmsg : qlist) {
                    try {
                        Map<String, Object> resultMap = new HashMap<>();
                        String body = qmsg.getM_body();
                        resultMap = Xml2Json.xmppToMap(body);

                        add_real_from_and_to(resultMap);

                        resultMap.put("from", qmsg.getM_from());
                        resultMap.put("from_host", qmsg.getFrom_host());
                        resultMap.put("to", qmsg.getM_to());
                        resultMap.put("to_host", qmsg.getTo_host());
                        if ("t".equals(flag)) {
                            resultMap.put("read_flag", qmsg.getRead_flag());
                        } else {
                            if (qmsg.getRead_flag() != 0) {
                                resultMap.put("read_flag", 1);
                            } else {
                                resultMap.put("read_flag", 0);
                            }
                        }
                        //    resultMap.put("time",qmsg.getCreate_time());
                        resultMap.put("t", qmsg.getDate_part());

                        listRes.add(resultMap);
                    } catch (Exception e) {
                        LOGGER.error("a bad message: {}", qmsg.getM_body());
                    }
                }

                Map<String, Object> rMap = new HashMap<>();
                rMap.put("from",user);
                rMap.put("bodys",listRes);

                return JsonResultUtils.success(listRes);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("catch error {} {}", ExceptionUtils.getStackTrace(e) , json);
            return JsonResultUtils.fail(0, "服务器操作异常:\n "+ExceptionUtils.getStackTrace(e));
        }
        LOGGER.info("gethistory.qunar end ");

        return JsonResultUtils.success("");
    }


    @RequestMapping(value = "/get_system_msgs.qunar",method = RequestMethod.POST)
    public JsonResult<?> get_system_msgs(@RequestBody String json) {

        List<Map<String, Object>> listRes = new ArrayList<>();

        try{
            if (json.length() > 0) {
                ObjectMapper mapper = new ObjectMapper();
                Map map = mapper.readValue(json, Map.class);
                List<QGetMsg> qlist = new ArrayList<>();

                String include = "";
                if (map.containsKey("include")) {
                    include = map.get("include").toString();
                }

                String direction = conversionDir(map.get("direction").toString(), include);
                String turn = conversionAsc(direction);


                long num = Long.valueOf(map.get("num").toString());
                String domain = map.get("domain").toString();
                Double time = getCorrectTime(map.get("time").toString());

                qlist = getMsgDao.selectSystemMsg(domain,direction,turn,num,time);

                if (turn.equals("desc") && qlist != null){
                    Collections.reverse(qlist);
                }

                for (QGetMsg qmsg:qlist) {
                    try {
                        Map<String, Object> resultMap = new HashMap<>();
                        String body = qmsg.getM_body();
                        resultMap = Xml2Json.xmppToMap(body);

                        listRes.add(resultMap);
                    } catch (Exception e) {
                        LOGGER.error("a bad message: {}", qmsg.getM_body());
                    }
                }
            }
        }catch (Exception e)
        {
            LOGGER.error("catch error {} {}", ExceptionUtils.getStackTrace(e) , json);
            return JsonResultUtils.fail(0, "服务器操作异常:\n "+ExceptionUtils.getStackTrace(e));
        }

        return JsonResultUtils.success(listRes);
    }

    @RequestMapping(value = "/get_system_history.qunar",method = RequestMethod.POST)
    public JsonResult<?> get_system_history(@RequestBody String json) {

        try{
            if (json.length() > 0) {
                ObjectMapper mapper = new ObjectMapper();
                List<QGetMsg> qlist = new ArrayList<>();
                Map map = mapper.readValue(json, Map.class);

                String user = (String) map.get("user");
                String domain = map.get("domain").toString();
                Double time = getCorrectTime(map.get("time").toString());
                long num = Long.valueOf(map.get("num").toString());

                qlist = getMsgDao.selectSystemHistory(domain,time,num);

                List<Map<String, Object>> listRes = new ArrayList<>();
                for (QGetMsg qmsg : qlist) {
                    try {
                        Map<String, Object> resultMap = new HashMap<>();
                        String body = qmsg.getM_body();
                        resultMap = Xml2Json.xmppToMap(body);
                        if (null != resultMap) {
                            // 暂时写死是1，系统消息没有阅读指针。1,05
                            resultMap.put("read_flag", "1");
                            listRes.add(resultMap);
                        }
                    } catch (Exception e) {
                        LOGGER.error("a bad message: {}", qmsg.getM_body());
                    }
                }

                return JsonResultUtils.success(listRes);
            }
        }catch (Exception e) {
            LOGGER.error("catch error {} {}", ExceptionUtils.getStackTrace(e) , json);
            return JsonResultUtils.fail(0, "服务器操作异常:\n "+ExceptionUtils.getStackTrace(e));
        }
        return JsonResultUtils.success("");
    }


    @RequestMapping(value = "/get_muc_readmark.qunar",method = RequestMethod.POST)
    public JsonResult<?> get_muc_readmark(@RequestBody String json) {
        try{
            if (json.length() > 0) {
                ObjectMapper mapper = new ObjectMapper();
                List<String> dmlist = new ArrayList<>();
                Set<QMucTime> qMucTimes = new HashSet<QMucTime>();

                Map map = mapper.readValue(json, Map.class);
                String user = (String) map.get("user");
                String host = (String) map.get("host");

                Map<String,Object> cookies = CookieUtils.getUserbyCookie(request);
                String U = cookies.get("u").toString();
                if (Strings.isNullOrEmpty(U) ||  (!U.equals(user))){
                    return JsonResultUtils.fail(3,"ukey not match");
                }

                dmlist = getMsgDao.selectMucHost(user,host);

                for (String dm : dmlist ){
                    qMucTimes.addAll(getMsgDao.selectMucTime(user,host));
                }

                return JsonResultUtils.success(qMucTimes);
            }
        }catch (Exception e) {
            LOGGER.error("catch error {} {}", ExceptionUtils.getStackTrace(e) , json);
            return JsonResultUtils.fail(0, "服务器操作异常:\n "+ExceptionUtils.getStackTrace(e));
        }
        return JsonResultUtils.success("");
    }

    @RequestMapping(value = "/get_muc_readmark1.qunar",method = RequestMethod.POST)
    public JsonResult<?> get_muc_readmark1(@RequestBody String json) {
        try{
            if (json.length() > 0) {
                ObjectMapper mapper = new ObjectMapper();
                List<String> dmlist = new ArrayList<>();
                Set<QMucTime> qMucTimes = new HashSet<QMucTime>();

                Map map = mapper.readValue(json, Map.class);
                String user = (String) map.get("user");
                String host = (String) map.get("host");
                Double time = getCorrectTime1(map.get("time").toString());

                Map<String,Object> cookies = CookieUtils.getUserbyCookie(request);
                String U = cookies.get("u").toString();
                if (Strings.isNullOrEmpty(U) ||  (!U.equals(user))){
                    return JsonResultUtils.fail(3,"ukey not match");
                }

                dmlist = getMsgDao.selectMucHost(user,host);

                for (String dm : dmlist ){
                    qMucTimes.addAll(getMsgDao.selectMucTime1(user,host, time));
                }

                return JsonResultUtils.success(qMucTimes);
            }
        }catch (Exception e) {
            LOGGER.error("catch error {} {}", ExceptionUtils.getStackTrace(e) , json);
            return JsonResultUtils.fail(0, "服务器操作异常:\n "+ExceptionUtils.getStackTrace(e));
        }
        return JsonResultUtils.success("");
    }

    @RequestMapping(value = "/getmucmsgs.qunar",method = RequestMethod.POST)
    public JsonResult<?> getMucMsg(@RequestBody String json) {
        try{
            if (json.length() > 0) {
                ObjectMapper mapper = new ObjectMapper();
                Map map = mapper.readValue(json, Map.class);
                List<QGetMucMsg> qmuclist = new ArrayList<>();
                List<Map<String, Object>> listRes = new ArrayList<>();

                String muc = (String) map.get("muc");
                String domain = map.get("domain").toString();
                String include = "";
                if (map.containsKey("include")) {
                    include = map.get("include").toString();
                }

                String direction = conversionDir(map.get("direction").toString(), include);
                String turn = conversionAsc(direction);

                long num = Long.valueOf(map.get("num").toString());
                Double time = getCorrectTime(map.get("time").toString());
                Double mtime = getMonthTime();
                if (time  > mtime ) {
                    qmuclist = getMsgDao.selectMucMsgbyTime(muc, direction, turn, num, time);
                }
                if (qmuclist.size() == 0 || time < mtime){
                    qmuclist = getMsgDao.selectMucMsgBackupbyTime(muc, direction, turn, num, time);
                }

                if (turn.equals("desc") && qmuclist != null) {
                    Collections.reverse(qmuclist);
                }

                for (QGetMucMsg qmucmsg : qmuclist){
                    try {
                        Map<String, Object> resultMap = new HashMap<>();
                        String body = qmucmsg.getPacket();
                        resultMap = Xml2Json.xmppToMap(body);
                        resultMap.put("muc", qmucmsg.getMuc_room_name());
                        resultMap.put("nick", qmucmsg.getNick());
                        resultMap.put("host", qmucmsg.getHost());
                        resultMap.put("t", qmucmsg.getDate_part());

                        Map<String, Object> bodyMap = new HashMap<>();
                        bodyMap = (HashMap<String, Object>) resultMap.get("message");

                        resetRealFrom(resultMap);

                        listRes.add(resultMap);
                    }  catch (Exception e) {
                        LOGGER.error("a bad message: {}", qmucmsg.getPacket());
                    }
                }

                return JsonResultUtils.success(listRes);
            }
        }catch (Exception e)
        {
            LOGGER.error("catch error {} {}", ExceptionUtils.getStackTrace(e) , json);
            return JsonResultUtils.fail(0, "服务器操作异常:\n "+ExceptionUtils.getStackTrace(e));
        }
        return JsonResultUtils.success("");
    }

    @RequestMapping(value = "/getmuchistory.qunar",method = RequestMethod.POST)
    public JsonResult<?> getMucHistory(@RequestBody String json) {
        try{
            if (json.length() > 0) {
                ObjectMapper mapper = new ObjectMapper();
                List<String> hosts = new ArrayList<>();
                Set<Map<String, Object>> listRes = new LinkedHashSet<>();
                Map map = mapper.readValue(json, Map.class);

                String user = (String) map.get("user");
                String host = (String) map.get("host");
                String domain = map.get("domain").toString();
                Double time = getCorrectTime(map.get("time").toString());
                long num = Long.valueOf(map.get("num").toString());

                Map<String,Object> cookies = CookieUtils.getUserbyCookie(request);
                String U = cookies.get("u").toString();
                if (Strings.isNullOrEmpty(U) ||  (!U.equals(user))){
                    return JsonResultUtils.fail(3,"ukey not match");
                }

                hosts = getMsgDao.selectMucHost(user,host);

                List<QGetMucMsg> res = getHostHistory(user,host,time,hosts,num);
                if (time != -1.0) {
                    Collections.sort(res);
                }

                int i = 0;

                for (QGetMucMsg qmucmsg : res){
                    try {
                        Map<String, Object> resultMap = new HashMap<>();
                        String body = qmucmsg.getPacket();
                        resultMap = Xml2Json.xmppToMap(body);
                        resultMap.put("muc", qmucmsg.getMuc_room_name());
                        resultMap.put("nick", qmucmsg.getNick());
                        resultMap.put("host", qmucmsg.getHost());
                        resultMap.put("t", qmucmsg.getDate_part());


                        resetRealFrom(resultMap);

                        listRes.add(resultMap);
                        i++;

                        if (i >= num) {
                            if (resultMap.size() + 1 <= res.size()) {
                                String t1 = qmucmsg.getCreate_time();
                                String t2 = res.get(resultMap.size() + 1).getCreate_time();
                                if (t1.equals(t2)) {
                                    i--;
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error("a bad message: {}", qmucmsg.getPacket());
                    }
                }
                LOGGER.info("getmuchistory.qunar 12");

                return JsonResultUtils.success(listRes);
            }
        }catch (Exception e)
        {
            LOGGER.error("catch error {} {}", ExceptionUtils.getStackTrace(e) , json);
            return JsonResultUtils.fail(0, "服务器操作异常:\n "+ExceptionUtils.getStackTrace(e));
        }
        return JsonResultUtils.success("");
    }

    @RequestMapping(value = "/getmuchistory_v2.qunar",method = RequestMethod.POST)
    public JsonResult<?> getMucHistory_v2(@RequestBody String json) {
        try{
            if (json.length() > 0) {
                ObjectMapper mapper = new ObjectMapper();
                List<String> hosts = new ArrayList<>();
                Map map = mapper.readValue(json, Map.class);

                String user = (String) map.get("user");
                String host = (String) map.get("host");
                String domain = map.get("domain").toString();
                Double time = getCorrectTime(map.get("time").toString());
                long num = Long.valueOf(map.get("num").toString());

                Map<String,Object> cookies = CookieUtils.getUserbyCookie(request);
                String U = cookies.get("u").toString();
                if (Strings.isNullOrEmpty(U) ||  (!U.equals(user))){
                    return JsonResultUtils.fail(3,"ukey not match");
                }

                hosts = getMsgDao.selectMucHost(user,host);

                List<Map<String, Object>> res = getHostHistory_v2(user,host,time,hosts,num);
                return JsonResultUtils.success(res);
            }
        }catch (Exception e)
        {
            LOGGER.error("catch error {} {}", ExceptionUtils.getStackTrace(e) , json);
            return JsonResultUtils.fail(0, "服务器操作异常:\n "+ExceptionUtils.getStackTrace(e));
        }
        return JsonResultUtils.success("");
    }

    private List<Map<String, Object>> getHostHistory_v2(String user,String host,Double time,List<String> hosts,long num)
    {
        List<Map<String, Object>> resList = new ArrayList<>();
        Set<String> sets = new HashSet<String>();

        for (String domain : hosts) {
            if (domain.equals("conference.ejabhost1") || domain.equals("conference.ejabhost2")){
                sets.add(domain);
            }else{
                sets.add("conference.other");
            }
        }
        return getHistoryByHost_v2(user,host,sets,time,num);
    }

    private List<Map<String, Object>> getHistoryByHost_v2(String user,String host ,Set<String> sets,double time,long num)  {
        List<Map<String, Object>> listRes = new ArrayList<>();
        Map<String, Object> MsgsMap = new HashMap<>();
        List<QGetMucMsg> smsglist = new ArrayList<>();
        Map<String, Object> mucMaps = new HashMap<>();
        get_muc_domain(sets,user,host,mucMaps);

        for (String domain:sets){
            smsglist.addAll(get_muc_msgs_by_time(domain,user,host,time,num));
        }
        if (time != -1.0) {
            Collections.sort(smsglist);
        }

        int i = 0;
        for(QGetMucMsg qGetMucMsg:smsglist){
            try {
                Map<String, Object> resultMap = new HashMap<>();
                String body = qGetMucMsg.getPacket();
                resultMap = Xml2Json.xmppToMap(body);
                resultMap.put("N", qGetMucMsg.getNick());
                resultMap.put("D", qGetMucMsg.getHost());
                resultMap.put("t", qGetMucMsg.getDate_part());

                List<Map<String, Object>> jmlists = (List<Map<String, Object>>) MsgsMap.get(qGetMucMsg.getMuc_room_name());
                if (jmlists == null) {
                    jmlists = new ArrayList<>();
                }
                jmlists.add(resultMap);
                MsgsMap.put(qGetMucMsg.getMuc_room_name(), jmlists);
                i++;

                if (i >= num) {
                    if (MsgsMap.size() + 1 <= smsglist.size()) {
                        String t1 = qGetMucMsg.getCreate_time();
                        String t2 = smsglist.get(MsgsMap.size() + 1).getCreate_time();
                        if (t1.equals(t2)) {
                            i--;
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            } catch (Exception e) {
                LOGGER.error("a bad message: {}", qGetMucMsg.getPacket());
            }
        }

        for (String key : MsgsMap.keySet()) {
            Map<String, Object> MucMap = new HashMap<>();
            MucMap.put("Time", "0");
            MucMap.put("Domain", get_mdomain(key,host,mucMaps));
            MucMap.put("ID", key);

            List<Map<String, Object>> msglists =  (List<Map<String, Object>>)MsgsMap.get(key);
            MucMap.put("Msgs",msglists );
            listRes.add(MucMap);
        }
        return listRes;
    }

    private void get_muc_domain(Set<String> sets,String user,String host,Map<String,Object> maps){
        List<QMucTime> mucTimes = new ArrayList<>();
      for (String domain:sets){
          mucTimes.addAll(getMsgDao.selectMucTime(user,host));
          for (QMucTime m:mucTimes) {
                maps.put(m.getMuc_name(),m.getDomain());
          }
        }
    }

    private String get_mdomain(String muc,String def_host,Map<String,Object> maps){
        Object o = maps.get(muc);
        if (o == null){
            return def_host;
        }
        return o.toString();
    }

    private List<QGetMucMsg> getHostHistory(String user,String host,Double time,List<String> hosts,long num)
    {
        List<QGetMucMsg>  hostRes = new ArrayList<>();
        Set<String> set = new HashSet<String>();

        for (String domain : hosts) {
            if (domain.equals("conference.ejabhost1") || domain.equals("conference.ejabhost2")){
                set.add(domain);
            }else{
                set.add("conference.other");
            }
        }
        for (String domain:set){
            hostRes.addAll(getHistoryByHost(user,host, domain, time,num));
        }
        return hostRes;
    }

    private List<QGetMucMsg> getHistoryByHost(String user,String host ,String domain,double time,long num)  {
        return get_muc_msgs_by_time(domain,user,host,time,num);
    }


    private String conversionDir(String direction)
    {
        if (direction.equals("0")){
            return "<";
        }else{
            return ">";
        }
    }

    private String conversionDir(String direction, String include) {
        if (direction.equals("0")) {
            if (include.equals("t")) {
                return "<=";
            } else {
                return "<";
            }
        } else {
            if (include.equals("t")) {
                return ">=";
            } else {
                return ">";
            }
        }
    }

    private double getCorrectTime(String time)
    {
        DecimalFormat df = new DecimalFormat("#.######");

        if (Double.parseDouble(time) >= 1000000000000L) {
            String time2 =   df.format(Double.parseDouble(time)/1000);
            return Double.valueOf(time2);
        }else if (Double.parseDouble(time) == 0){
            return System.currentTimeMillis();
        }else {
            return Double.valueOf(time);
        }
    }

    private double getCorrectTime1(String time)
    {
        DecimalFormat df = new DecimalFormat("#.######");

        if (Double.parseDouble(time) >= 1000000000000L) {
            String time2 =   df.format(Double.parseDouble(time)/1000);
            return Double.valueOf(time2);
        }else {
            return Double.valueOf(time);
        }
    }

    private double getMonthTime()
    {
        Calendar calendar = Calendar.getInstance();// 获取当前日期

        calendar.add(Calendar.DAY_OF_YEAR,-30);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis()/1000;
    }

    private List<QGetMucMsg> get_muc_msgs_by_time(String domain,String user,String host,double time,long num)
    {
        List<QGetMucMsg> smsglist = new ArrayList<>();

        if (time == -1){
            List<QMucTime> mucTimes = getMsgDao.selectMucTime(user,host);
            for (QMucTime mucTime : mucTimes){
                List<QGetMucMsg>  mucMsgs = getMsgDao.selectMucMsgbyTime(mucTime.getMuc_name(),">","asc",1L,((Double.valueOf(mucTime.getDate()))/1000));
                smsglist.addAll(mucMsgs);
            }
        }else {
            smsglist.addAll(getMsgDao.selectLocalDomainMucHistory(user,host , time, num));
        }
        return smsglist;
    }

    private void add_real_from_and_to(Map<String,Object> rmap){
        try {
            Map<String,Object>  map = (Map<String,Object>)rmap.get("message");
            if (map.get("type").toString().equals("consult")){
                if (map.get("realfrom") == null ){
                    map.put("realfrom","");
                }
                if (map.get("realto") == null ){
                    map.put("realto","");
                }
                rmap.put("message",map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getSenderJid(Map<String,Object> map){
        try {

            if ( map.get("sendjid") != null){
                return map.get("sendjid").toString();
            }
            if ( map.get("realfrom") != null){
                return map.get("realfrom").toString();
            }
            return "";
        }catch (Exception e){
            return "";
        }
    }

    private void resetRealFrom(Map<String,Object> resultMap ){
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap = (HashMap<String,Object>)resultMap.get("message");
        String sendjid = getSenderJid(bodyMap);

        if (bodyMap.get("sendjid") == null){
            bodyMap.put("sendjid",sendjid);
        }
        if (bodyMap.get("realfrom") == null){
            bodyMap.put("realfrom",sendjid);
        }

        resultMap.put("message",bodyMap);
    }
}
