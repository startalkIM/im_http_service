package com.qunar.qchat.dao;

import com.qunar.qchat.dao.model.QGetMsg;
import com.qunar.qchat.dao.model.QGetMucMsg;
import com.qunar.qchat.dao.model.QMsgReadFlag;
import com.qunar.qchat.dao.model.QMucTime;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by admin on 13/07/2017.
 */
@Component
public interface IGetMsgDao {

    public List<QGetMsg> selectMsgbyTime(
            @Param("fuser") String fuser,
            @Param("fhost") String fhost,
            @Param("tuser") String tuser,
            @Param("thost") String thost,
            @Param("direction") String direction,
            @Param("turn") String turn,
            @Param("num") long num,
            @Param("time") double time);


    public List<QGetMsg> selectMsgBackupbyTime(
            @Param("fuser") String fuser,
            @Param("fhost") String fhost,
            @Param("tuser") String tuser,
            @Param("thost") String thost,
            @Param("direction") String direction,
            @Param("turn") String turn,
            @Param("num") long num,
            @Param("time") double time);


    public List<QGetMsg> selectHistory(
            @Param("user") String user,
            @Param("host") String host,
            @Param("time") double time,
            @Param("num") long num);

    public List<QGetMsg> selectQchatHistory(
            @Param("user") String user,
            @Param("host") String host,
            @Param("time") double time,
            @Param("num") long num);


    public List<QGetMucMsg> selectMucMsgbyTime(
            @Param("muc") String muc,
            @Param("direction") String direction,
            @Param("turn") String turn,
            @Param("num") long num,
            @Param("time") double time);

    public List<QGetMucMsg> selectMucMsgBackupbyTime(
            @Param("muc") String muc,
            @Param("direction") String direction,
            @Param("turn") String turn,
            @Param("num") long num,
            @Param("time") double time);

    public Set<QGetMucMsg> selectMucHistory(
            @Param("user") String user,
            @Param("host") String host,
            @Param("domain") String domain,
            @Param("time") double time,
            @Param("num") long num);


    public List<QGetMucMsg> selectLocalDomainMucHistory(
            @Param("user") String user,
            @Param("host") String host,
            @Param("time") double time,
            @Param("num") long num);

    public List<QMucTime> selectMucTime(
            @Param("user") String user,
            @Param("host") String host);

    public List<QMucTime> selectMucTime1(
            @Param("user") String user,
            @Param("host") String host,
            @Param("time") Double time);

    public List<String> selectMucHost(
            @Param("user") String user,
            @Param("host") String host);

    public List<String> selectMucHost1(
            @Param("user") String user,
            @Param("host") String host);

    public List<QGetMsg> selectSystemHistory(
            @Param("host") String host,
            @Param("time") double time,
            @Param("num") long num);

    public List<QGetMsg> selectSystemMsg(
            @Param("host") String host,
            @Param("direction") String direction,
            @Param("turn") String turn,
            @Param("num") long num,
            @Param("time") double time);


    public String selectMucMinTime(
            @Param("user") String user,
            @Param("host") String host);

    public List<HashMap<String,Object>> selectMucDomain(
            @Param("user") String user,
            @Param("host") String host);

    public List<QGetMsg> selectConsultMsgbyTime(
            @Param("from") String from,
            @Param("fhost") String fhost,
            @Param("to") String to,
            @Param("thost") String thost,
            @Param("virtual") String virtual,
            @Param("real") String real,
            @Param("direction") String direction,
            @Param("turn") String turn,
            @Param("num") long num,
            @Param("time") double time);

    public List<QMsgReadFlag> get_readflag(
            @Param("user") String user,
            @Param("time") Double time,
            @Param("id") long id);
}
