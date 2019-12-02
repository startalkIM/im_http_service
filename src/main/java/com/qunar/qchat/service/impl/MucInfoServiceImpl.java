package com.qunar.qchat.service.impl;

import com.qunar.qchat.dao.IMucInfoDao;
import com.qunar.qchat.dao.model.MucInfoModel;
import com.qunar.qchat.service.IMucInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auth dongzd.zhang
 * @Date 2019/5/29 10:37
 */
@Service
public class MucInfoServiceImpl implements IMucInfoService {

    @Autowired
    private IMucInfoDao iMucInfoDao;


    @Override
    public List<MucInfoModel> getIncrementMucVCards(List<String> mucNames, Date updateTime) {
        List<MucInfoModel> mucInfoModels = iMucInfoDao.selectIncrementMucVCards(mucNames, updateTime);
        return mucInfoModels;
    }

    @Override
    public List<String> selectMucNamesByUserId(String userId) {
        List<String> mucNames = iMucInfoDao.selectMucNamesByUsername(userId);
        return mucNames;
    }

    @Override
    public List<String> getEjabHostMucIds(List<String> originMucIds, String suffix) {
        List<String> transferMucIds = originMucIds.stream().filter(muc -> muc.endsWith(suffix)).collect(Collectors.toList());
        return transferMucIds;
    }

}
