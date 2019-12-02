package com.qunar.qchat.dao;

import com.qunar.qchat.model.SendWhiteModel;
import org.springframework.stereotype.Component;


@Component
public interface ISendMsgDao {

    void insertSelective(SendWhiteModel sendWhiteModel);

}
