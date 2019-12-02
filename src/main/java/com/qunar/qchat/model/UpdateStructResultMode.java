package com.qunar.qchat.model;

import com.qunar.qchat.dao.model.UserInfoQtalk;

import java.util.List;

/**
 * UpdateStructResultMode
 *
 * @author binz.zhang
 * @date 2018/11/19
 */
public class UpdateStructResultMode {
    List<UserInfoQtalk> update;
    List<UserInfoQtalk> delete;
    Integer version;

    public UpdateStructResultMode(List<UserInfoQtalk> update, List<UserInfoQtalk> delete) {
        this.update = update;
        this.delete = delete;
    }

    public UpdateStructResultMode() {
    }

    public void setUpdate(List<UserInfoQtalk> update) {
        this.update = update;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setDelete(List<UserInfoQtalk> delete) {
        this.delete = delete;
    }

    public List<UserInfoQtalk> getUpdate() {
        return update;
    }

    public List<UserInfoQtalk> getDelete() {
        return delete;
    }

    public Integer getVersion() {
        return version;
    }
}
