package com.qunar.qchat.model.request;

import java.util.List;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/5 17:30
 */
public class GetVCardInfoRequest {


    private String domain;
    private List<UserInfo> users;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<UserInfo> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfo> users) {
        this.users = users;
    }

    public static class UserInfo{
        private String user;
        private Integer version;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "user='" + user + '\'' +
                    ", version=" + version +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "GetVCardInfoRequest{" +
                "domain='" + domain + '\'' +
                ", users=" + users +
                '}';
    }
}
