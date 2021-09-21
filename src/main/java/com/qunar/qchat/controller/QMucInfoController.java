package com.qunar.qchat.controller;

import com.qunar.qchat.constants.Config;
import com.qunar.qchat.constants.QChatConstant;
import com.qunar.qchat.dao.IMucInfoDao;
import com.qunar.qchat.dao.model.MucIncrementInfo;
import com.qunar.qchat.dao.model.MucInfoModel;
import com.qunar.qchat.dao.model.MucOptsModel;
import com.qunar.qchat.model.JsonResult;
import com.qunar.qchat.model.request.*;
import com.qunar.qchat.model.result.GetMucVcardResult;
import com.qunar.qchat.model.result.UpdateMucNickResult;
import com.qunar.qchat.service.IMucInfoService;
import com.qunar.qchat.utils.CookieUtils;
import com.qunar.qchat.utils.DateUtils;
import com.qunar.qchat.utils.HttpClientUtils;
import com.qunar.qchat.utils.JsonResultUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
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

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RequestMapping("/newapi/muc/")
@RestController
public class QMucInfoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QMucInfoController.class);
    private static final String FWREG = "\\{forbidden_words,(.*?)\\}";
    private static final String ADMINREG = "\\{affiliations,\\[(.*?)\\]\\}";
    private static final String ADMINTEMPLATE = "<<\"%s\">>,<<\"%s\">>";
    @Autowired
    private IMucInfoDao iMucInfoDao;

    @Autowired
    private IMucInfoService mucInfoService;

    /**
     * 获取新增群列表.
     *
     * @param httpRequest  HttpServletRequest
     * @param paramRequest GetIncrementMucsRequest
     * @return JsonResult<?>
     */
    @RequestMapping(value = "/get_increment_mucs.qunar", method = RequestMethod.POST)
    public Object getIncrement(HttpServletRequest httpRequest,
                               @RequestBody GetIncrementMucsRequest paramRequest) {
        try {
            if (Objects.isNull(paramRequest.getT())) {
                return JsonResultUtils.fail(1, "参数错误");
            }

            if (StringUtils.isBlank(paramRequest.getU())) {
                Map<String, Object> cookie = CookieUtils.getUserbyCookie(httpRequest);
                paramRequest.setU(cookie.get("u").toString());
            }
            if (StringUtils.isBlank(paramRequest.getD())) {
                Map<String, Object> cookie = CookieUtils.getUserbyCookie(httpRequest);
                paramRequest.setD(cookie.get("d").toString());
            }

            /**
             * 解决pg to_timestamp 只接受秒数的问题.
             * */
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String strTime = sdf.format(paramRequest.getT());

            List<MucIncrementInfo> mucIncrementInfoList = iMucInfoDao.selectMucIncrementInfoNew(paramRequest.getU(), paramRequest.getD(), strTime);


            List<Map<String, Object>> result = new ArrayList<>();
            mucIncrementInfoList.stream().forEach(item -> {
                Map<String, Object> map = new HashMap<>();
                map.put("M", StringUtils.defaultString(item.getMuc_name(), ""));
                map.put("D", StringUtils.defaultString(item.getDomain(), ""));
                map.put("T", StringUtils.defaultString(String.valueOf(item.getT()), ""));
                map.put("F", item.getRegisted_flag());
                result.add(map);
            });

            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("ret", true);
            resultMap.put("errcode", 0);
            resultMap.put("errmsg", "");
            if (CollectionUtils.isNotEmpty(mucIncrementInfoList)) {
                resultMap.put("version", String.valueOf(mucIncrementInfoList.get(0).getCreated_at().getTime()));
            } else {
                BigDecimal bigDecimal = new BigDecimal(paramRequest.getT());
                resultMap.put("version", bigDecimal.toString());
            }
            resultMap.put("data", result);

            return resultMap;

        } catch (Exception e) {
            LOGGER.error("catch error : {}", ExceptionUtils.getStackTrace(e));
            return JsonResultUtils.fail(0, "服务器操作异常");
        }
    }

    /**
     * 设置群信息.
     *
     * @param requests List<UpdateMucNickRequest>
     * @return JsonResult<?>
     */
    @RequestMapping(value = "/set_muc_vcard.qunar", method = RequestMethod.POST)
    public JsonResult<?> updateMucNick(@RequestBody List<UpdateMucNickRequest> requests) {
        try {
            // 校验参数
            if (CollectionUtils.isEmpty(requests)) {
                return JsonResultUtils.fail(1, "参数错误");
            }

            for (UpdateMucNickRequest request : requests) {
                if (!request.isRequestValid()) {
                    return JsonResultUtils.fail(1, "参数错误");
                }
            }


            /*List<MucInfoModel> mucInfoModels = iMucInfoDao.selectMucInfoByIds(requests.stream()
                     .map(request -> request.getMuc_name()).collect(Collectors.toList()));*/

            //fix bug
            for (UpdateMucNickRequest request : requests) {
                String tempMucName = "";
                if (request.getMuc_name().indexOf("@") == -1) {
                    tempMucName = request.getMuc_name();
                } else {
                    tempMucName = request.getMuc_name().substring(0, request.getMuc_name().indexOf("@"));
                }
                int exitCount = iMucInfoDao.checkMucExist(tempMucName);
                if (exitCount == 0) {
                    return JsonResultUtils.fail(1, "群" + request.getMuc_name() + "不存在");
                }
            }

            List<UpdateMucNickResult> resultList = new ArrayList<>();
            for (UpdateMucNickRequest request : requests) {

                //判断群数据是否存在
                Integer mucCount = iMucInfoDao.selectMucCountByMucName(request.getMuc_name());
                if (mucCount == null || mucCount == 0) {

                    MucInfoModel mucInfoModel = new MucInfoModel();
                    mucInfoModel.setMucName(request.getMuc_name());
                    mucInfoModel.setShowName(request.getNick());

                    String showNamePinyin = convertMucShowName2Pinyin(request.getNick());
                    mucInfoModel.setShowNamePinyin(showNamePinyin);

                    mucInfoModel.setMucTitle(request.getTitle());
                    mucInfoModel.setMucDesc(request.getDesc());
                    iMucInfoDao.insertMucInfo(mucInfoModel);
                } else {
                    MucInfoModel parameter = new MucInfoModel();
                    parameter.setMucName(request.getMuc_name());
                    parameter.setShowName(request.getNick());

                    String showNamePinyin = convertMucShowName2Pinyin(request.getNick());
                    parameter.setShowNamePinyin(showNamePinyin);

                    parameter.setMucTitle(request.getTitle());
                    parameter.setMucDesc(request.getDesc());
                    iMucInfoDao.updateMucInfo(parameter);
                }


                MucInfoModel newMucInfo = iMucInfoDao.selectByMucName(request.getMuc_name());
                UpdateMucNickResult result = new UpdateMucNickResult();
                if (!Objects.isNull(result)) {
                    result.setMuc_name(StringUtils.defaultString(newMucInfo.getMucName(), ""));
                    result.setVersion(StringUtils.defaultString(newMucInfo.getVersion(), ""));
                    result.setShow_name(StringUtils.defaultString(newMucInfo.getShowName(), ""));
                    result.setMuc_title(StringUtils.defaultString(newMucInfo.getMucTitle(), ""));
                    result.setMuc_desc(StringUtils.defaultString(newMucInfo.getMucDesc(), ""));
                }
                resultList.add(result);

                //发送通知
                HttpClientUtils.get(Config.UPDATE_MUC_VCARD_MSG_URL + "?muc_name=" + result.getMuc_name());
                LOGGER.info("发送群信息变更通知成功，群ID : {}", request.getMuc_name());
            }
            return JsonResultUtils.success(resultList);
        } catch (Exception ex) {
            LOGGER.error("catch error: {}", ExceptionUtils.getStackTrace(ex));
            return JsonResultUtils.fail(0, "服务器操作异常");
        }
    }


    /**
     * 将群名称转成拼音（分词）.
     *
     * @param showName
     * @return String
     */
    private String convertMucShowName2Pinyin(String showName) {

        if (StringUtils.isEmpty(showName)) {
            return showName;
        }

        //定义拼音格式
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        //分词
        Result result = ToAnalysis.parse(showName);
        List<Term> list = result.getTerms();

        StringBuilder pinyin = new StringBuilder();
        StringBuilder firstCharactorStr = new StringBuilder();

        for (Term word : list) {
            try {
                String wordName = word.getName();
                String tempWordName = wordName;
                String wordType = word.getNatureStr();
                boolean isChinese = word.getName().matches("[\\u4E00-\\u9FA5]+");
                //boolean isNumber = word.getName().matches("");
                //System.out.println(wordName + "," + wordType );

                System.out.println(wordName);

                if (isContainNumber(tempWordName) && !isChinese && !wordType.equals("en")) {
                    char[] charArray = tempWordName.toCharArray();
                    for (int i = 0; i < charArray.length; i++) {
                        String n = charArray[i] + "";
                        if (QChatConstant.NUMBER_LIST.contains(n)) {
                            pinyin.append(n);
                            if (i != charArray.length - 1) {
                                try {
                                    wordName = tempWordName.substring(i + 1, tempWordName.length());
                                } catch (Exception ex) {
                                    LOGGER.error("ex: i = " + i + "," + wordName + ", " + ex.getMessage());
                                }
                            }
                            firstCharactorStr.append(n);
                        }
                    }

                    if (!org.springframework.util.StringUtils.isEmpty(wordName)) {
                        String tempPinYIn = PinyinHelper.toHanYuPinyinString(wordName, format, "", false);
                        pinyin.append(tempPinYIn);
                        if (tempPinYIn.length() >= 1) {
                            firstCharactorStr.append(tempPinYIn.substring(0, 1));
                        }
                    }
                }

                //只处理中文和英文
                if (isChinese || wordType.equals("en")) {
                    if ("en".equals(wordType)) {
                        pinyin.append(wordName);
                        //英文- 整个单词加到首字母字段中
                        firstCharactorStr.append(wordName);
                    } else {
                        // System.out.println(wordName);
                        String tempPinYin = PinyinHelper.toHanYuPinyinString(wordName, format, "", false);
                        pinyin.append(tempPinYin);
                        //截取首字母
                        /*if(tempPinYin.length() >= 1) {
                            firstCharactorStr = firstCharactorStr + tempPinYin.substring(0, 1);
                        }*/
                        firstCharactorStr.append(getFirstPinYin(wordName, format));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                continue;
            }
        }
        String finalStr = pinyin + "|" + firstCharactorStr;
        if (finalStr.length() > 1000) {
            return finalStr.substring(0, 1000);
        }
        return finalStr;
    }


    public static boolean isContainNumber(String str) {
        char[] chars = str.toCharArray();
        for (char c : chars) {
            String n = c + "";
            if (QChatConstant.NUMBER_LIST.contains(n)) {
                return true;
            }
        }
        return false;
    }


    public static String getFirstPinYin(String word, HanyuPinyinOutputFormat format) throws Exception {

        StringBuilder firstStr = new StringBuilder();
        char[] cs = word.toCharArray();
        for (char c : cs) {
            String cStr = c + "";
            String pinyin = PinyinHelper.toHanYuPinyinString(cStr, format, "", false);
            if (!org.springframework.util.StringUtils.isEmpty(pinyin)) {
                firstStr.append(pinyin.substring(0, 1));
            }
        }
        return firstStr.toString();
    }


    /**
     * 获取群信息.
     *
     * @param request List<GetMucVcardRequest>
     * @return JsonResult<?>
     */
    @RequestMapping(value = "/get_muc_vcard.qunar", method = RequestMethod.POST)
    public JsonResult<?> getMucVCard(@RequestBody List<GetMucVcardRequest> request) {
        try {
            //LOGGER.info(request.toString());

            //检查参数是否合法
            if (!checkGetMucVCardInfoParams(request)) {
                return JsonResultUtils.fail(0, "参数错误");
            }

            List<GetMucVcardResult> results =
                    request.stream().map(item -> {
                        List<GetMucVcardRequest.MucInfo> mucInfos = item.getMucs();
                        GetMucVcardResult result = new GetMucVcardResult();
                        result.setDomain(item.getDomain());
                        if (CollectionUtils.isNotEmpty(mucInfos)) {
                            List<String> mucIds = mucInfos.stream().
                                    map(requestMucInfo -> requestMucInfo.getMuc_name()).collect(Collectors.toList());

                            List<MucInfoModel> mucInfoModels = iMucInfoDao.selectMucInfoByIds(mucIds);
                            List<GetMucVcardResult.MucInfo> mucInfoResultList =
                                    mucInfoModels.stream().map(mucInfoModel -> {
                                        GetMucVcardResult.MucInfo resultMucInfo = new GetMucVcardResult.MucInfo();
                                        resultMucInfo.setMN(StringUtils.defaultString(mucInfoModel.getMucName(), ""));
                                        resultMucInfo.setSN(StringUtils.defaultString(mucInfoModel.getShowName(), ""));
                                        resultMucInfo.setMD(StringUtils.defaultString(mucInfoModel.getMucDesc(), ""));
                                        resultMucInfo.setMT(StringUtils.defaultString(mucInfoModel.getMucTitle(), ""));
                                        resultMucInfo.setMP(StringUtils.defaultString(mucInfoModel.getMucPic(), ""));
                                        resultMucInfo.setVS(StringUtils.defaultString(mucInfoModel.getVersion(), ""));
                                        return resultMucInfo;
                                    }).collect(Collectors.toList());
                            result.setMucs(mucInfoResultList);
                        } else {
                            result.setMucs(new ArrayList<>());
                        }

                        return result;
                    }).collect(Collectors.toList());

            return JsonResultUtils.success(results);
        } catch (Exception ex) {
            LOGGER.error("catch error : {} ", ExceptionUtils.getStackTrace(ex));
            return JsonResultUtils.fail(0, "服务器操作异常");
        }
    }

    private boolean checkGetMucVCardInfoParams(List<GetMucVcardRequest> request) {
        if (CollectionUtils.isEmpty(request)) {
            return false;
        }
        for (GetMucVcardRequest item : request) {
            List<GetMucVcardRequest.MucInfo> mucInfos = item.getMucs();
            for (GetMucVcardRequest.MucInfo info : mucInfos) {
                if (StringUtils.isBlank(info.getMuc_name())) {
                    //return JsonResultUtils.fail(0, "参数错误");
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 增量获取指定用户对应的群名片.
     *
     * @param request
     * @return JsonResult<?>
     */
    @RequestMapping(value = "/get_user_increment_muc_vcard.qunar", method = RequestMethod.POST)
    public JsonResult<?> getUserIncrementMucVCard(@RequestBody GetUserIncrementMucVCardRequest request) {

        if (!request.isRequestValid()) {
            return JsonResultUtils.fail(-1, "parameter error");
        }

        List<String> mucNames = mucInfoService.selectMucNamesByUserId(request.getUserid());
        List<GetMucVcardResult.MucInfo> result = this.getIncrementMucVCardFromDB(mucNames, request.getLastupdtime());
        return JsonResultUtils.success(result);
    }

    private List<GetMucVcardResult.MucInfo> getIncrementMucVCardFromDB(List<String> mucNames, String updateTime) {
        Date updateDate = new Date(Long.parseLong(updateTime));
        List<MucInfoModel> mucInfoModels = mucInfoService.getIncrementMucVCards(mucNames, updateDate);
        List<GetMucVcardResult.MucInfo> mucInfoResultList = processMucVCardResult(mucInfoModels);
        return mucInfoResultList;
    }

    private List<GetMucVcardResult.MucInfo> processMucVCardResult(List<MucInfoModel> mucInfoModels) {
        List<GetMucVcardResult.MucInfo> mucInfoResultList =
                mucInfoModels.stream().map(mucInfoModel -> {
                    GetMucVcardResult.MucInfo resultMucInfo = new GetMucVcardResult.MucInfo();
                    resultMucInfo.setMN(StringUtils.defaultString(mucInfoModel.getMucName(), ""));
                    resultMucInfo.setSN(StringUtils.defaultString(mucInfoModel.getShowName(), ""));
                    resultMucInfo.setMD(StringUtils.defaultString(mucInfoModel.getMucDesc(), ""));
                    resultMucInfo.setMT(StringUtils.defaultString(mucInfoModel.getMucTitle(), ""));
                    resultMucInfo.setMP(StringUtils.defaultString(mucInfoModel.getMucPic(), ""));
                    resultMucInfo.setVS(StringUtils.defaultString(mucInfoModel.getVersion(), ""));

                    String updateTimeStr = DateUtils.getTimestamp(mucInfoModel.getUpdateTime());
                    resultMucInfo.setUT(StringUtils.defaultString(updateTimeStr, ""));

                    return resultMucInfo;
                }).collect(Collectors.toList());

        return mucInfoResultList;
    }


    /**
     * 获取指定用户的群forbidden字段.
     *
     * @param request
     * @return JsonResult<?>
     */
    @RequestMapping(value = "/get_user_muc_fw.qunar", method = RequestMethod.POST)
    public JsonResult<?> getUserMucFw(@RequestBody GetUserMucFwRequest request) {

        if (!request.isRequestValid()) {
            return JsonResultUtils.fail(-1, "parameter error");
        }
        String uid = request.getUserid().trim();
        if (!uid.contains("@")) {
            return JsonResultUtils.fail(-1, "parameter error");
        }
        String userId = uid.split("@")[0];
        String domain = uid.split("@")[1];
        List<String> fwMucs = new ArrayList<>();
        List<MucOptsModel> mucNames = mucInfoService.getMucOptsByUserId(userId, domain);
        Pattern fwPattern = Pattern.compile(FWREG, Pattern.CASE_INSENSITIVE);
        for (MucOptsModel mo : mucNames) {
            String opt = mo.getOpt();
            if (opt.equals("")) {
                continue;
            } else {
                Matcher matcher = fwPattern.matcher(opt);
                if (matcher.find()) {
                    if (matcher.group(1).equals("true")) {
                        fwMucs.add(mo.getMucName());
                    }
                } else {
                    LOGGER.warn("Cant find forbidden_words", opt);
                }
            }

        }
        LOGGER.info("Forbidden words groups for user {}, groups: {}", uid, fwMucs);
        return JsonResultUtils.success(fwMucs);
    }
    @RequestMapping(value = "/get_muc_fw.qunar", method = RequestMethod.POST)
    public JsonResult<?> getMucFw(@RequestBody GetMucFwRequest request) {

        if (!request.isRequestValid()) {
            return JsonResultUtils.fail(-1, "parameter error");
        }
        String uid = request.getUserid().trim();
        String groupid = request.getGroupid().trim();
        if (!uid.contains("@") || !groupid.contains("@")) {
            return JsonResultUtils.fail(-1, "parameter error");
        }
        String userId = uid.split("@")[0];
        String domain = uid.split("@")[1];
        String mucId = groupid.split("@")[0];
        String mucDomain = groupid.split("@")[1];
        List<MucOptsModel> mucNames = mucInfoService.getMucOptsWithUserId(userId, domain, mucId, mucDomain);

        if(mucNames.size() != 1){
            LOGGER.warn("Sql Error,  group found result not only {}", mucNames);
            if(mucNames.size() == 0){
                return JsonResultUtils.fail(-1, "user not in group");
            }
        }
        MucOptsModel mucOpt = mucNames.get(0);
        String mucName = mucOpt.getMucName();
        String opt = mucOpt.getOpt();
        boolean fwTag = false;
        Pattern fwPattern = Pattern.compile(FWREG, Pattern.CASE_INSENSITIVE);
        Matcher fwMatcher = fwPattern.matcher(opt);
        if (fwMatcher.find()) {
            if (fwMatcher.group(1).equals("true")) {
                fwTag = true;
            }
        }else{
            LOGGER.warn("Failed to find forbidden words for {} mucOpt {}", mucNames, mucOpt);
        }
        boolean adminTag = false;
        Pattern adminPattern = Pattern.compile(ADMINREG, Pattern.CASE_INSENSITIVE);
        Matcher adminMatcher = adminPattern.matcher(opt);
        if (adminMatcher.find()) {
            if (adminMatcher.group(1).contains(String.format(ADMINTEMPLATE, userId, domain))) {
                adminTag = true;
            }
        }else{
            LOGGER.warn("Failed to find admins for {} mucOpt {}", mucNames, mucOpt);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("group_id",mucName);
        resultMap.put("is_fw",fwTag);
        resultMap.put("is_admin", adminTag);
        return JsonResultUtils.success(resultMap);
    }
}
