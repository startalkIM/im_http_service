package com.qunar.qchat.model;

import java.util.List;

public class UserInfoParam {

    private String system;
    private List<UsersEntity> users;

    public void setSystem(String system) {
        this.system = system;
    }

    public void setUsers(List<UsersEntity> users) {
        this.users = users;
    }

    public String getSystem() {
        return system;
    }

    public List<UsersEntity> getUsers() {
        return users;
    }

    public static class UsersEntity {

        private String host;
        private String username;

        public void setHost(String host) {
            this.host = host;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getHost() {
            return host;
        }

        public String getUsername() {
            return username;
        }
    }
}
