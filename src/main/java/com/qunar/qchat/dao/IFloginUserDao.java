package com.qunar.qchat.dao;

import com.qunar.qchat.dao.model.FloginUserModel;

import java.util.List;

/**
 * @auth dongzd.zhang
 * @Date 2019/5/22 11:51
 */
public interface IFloginUserDao {

    List<String> selectFloginUserNames();

    List<FloginUserModel> selectFloginUsers();

}
