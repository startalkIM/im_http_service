package com.qunar.qchat.dao;

import com.qunar.qchat.dao.model.QCloudMain;
import com.qunar.qchat.dao.model.QCloudMainHistory;
import com.qunar.qchat.dao.model.QCloudSub;
import com.qunar.qchat.dao.model.QCloudSubHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by admin on 14/07/2017.
 */
@Component
public interface IQCloudDao {

    public void insertQCloudMain(QCloudMain entity);
    public void insertQCloudSub(QCloudSub entity);
    public void updateQCloudMainWithState(@Param("qid") long qid, @Param("state") int state, @Param("stime") long stime);
    public void updateQCloudSubWithState(@Param("qsid") long qsid, @Param("state") int state, @Param("stime") long stime);
    public void updateQCloudSubForMainIdWithState(@Param("qid") long qid, @Param("state") int state, @Param("stime") long stime);
    public void updateQCloudMain(QCloudMain entity);
    public void updateQCloudSub(QCloudSub entity);
    public void insertQCloudMainHistory(QCloudMainHistory entity);
    public void insertQCloudSubHistory(QCloudSubHistory entity);
    public List<QCloudMain> selectQCloudMain(@Param("user") String user, @Param("stime") long stime);
    public List<QCloudMain> selectQCloudMainWithType(@Param("user") String user, @Param("stime") long stime, @Param("type") int type);
    public QCloudMain selectQCloudMainWithId(@Param("user") String user, @Param("qid") long qid);
    public List<QCloudSub> selectQCloudSub(@Param("qid") long qid, @Param("stime") long stime);
    public List<QCloudSub> selectQCloudSubWithType(@Param("qid") long qid, @Param("stime") long stime, @Param("type") int type);
    public QCloudSub selectQCloudSubWithId(@Param("user") String user, @Param("qsid") long qsid);
    public List<QCloudMainHistory> selectQCloudMainHistory(@Param("qid") long qid);
    public List<QCloudSubHistory> selectQCloudSubHistory(@Param("qsid") long qsid);

    public void bulkInsertQCloudMain(List<QCloudMain> cloudMainList);
    public void bulkInsertQCloudSub(List<QCloudSub> cloudSubList);
    public void bulkUpdateQCloudMain(List<QCloudMain> cloudMainList);
    public void bulkUpdateQCloudSub(List<QCloudSub> cloudSubList);

}
