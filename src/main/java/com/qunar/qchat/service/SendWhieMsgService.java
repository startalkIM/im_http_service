package com.qunar.qchat.service;

import com.qunar.qchat.dao.ISendMsgDao;
import com.qunar.qchat.model.SendWhiteModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SendWhieMsgService {
    @Autowired
    private ISendMsgDao iSendMsgDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(SendWhieMsgService.class);

    public void insertSendInfo(SendWhiteModel sendWhiteModel) {
        try {
            iSendMsgDao.insertSelective(sendWhiteModel);
        } catch (Exception e) {
            LOGGER.info("send msg statis error ", e);
        }
    }

}
