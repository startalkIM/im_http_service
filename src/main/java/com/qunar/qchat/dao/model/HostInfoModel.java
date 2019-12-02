package com.qunar.qchat.dao.model;

import java.util.Date;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/6 11:12
 */
public class HostInfoModel {

    private Integer id;
    private String host;
    private String description;
    private Date createTime;
    private String hostAdmin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getHostAdmin() {
        return hostAdmin;
    }

    public void setHostAdmin(String hostAdmin) {
        this.hostAdmin = hostAdmin;
    }

    @Override
    public String toString() {
        return "HostInfoModel{" +
                "id=" + id +
                ", host='" + host + '\'' +
                ", description='" + description + '\'' +
                ", createTime=" + createTime +
                ", hostAdmin='" + hostAdmin + '\'' +
                '}';
    }
}
