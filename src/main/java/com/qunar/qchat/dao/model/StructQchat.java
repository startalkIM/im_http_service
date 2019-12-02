package com.qunar.qchat.dao.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * UserModeQchat
 *
 * @author binz.zhang
 * @date 2018/12/4
 */
public class StructQchat {

    @JsonProperty("UL")
    List<UserInfoQchat> UL;

    @JsonProperty("SD")
    List<StructQchat> SD;

    @JsonProperty("D")
    public String D;

    @JsonProperty("id")
    public Integer id;

    public StructQchat(List<UserInfoQchat> users, List<StructQchat> child, String depName, Integer id) {
        this.UL = users;
        this.SD = child;
        this.D = depName;
        this.id = id;
    }

    public List<UserInfoQchat> getUL() {
        return UL;
    }

    public List<StructQchat> getSD() {
        return SD;
    }

    public String getD() {
        return D;
    }

    public Integer getId() {
        return id;
    }

    public void setULsDep(String depTemp){
        if(null==this.UL){
            return;
        }
        if(depTemp.startsWith("/QChatStaff")){
            depTemp = depTemp.substring(11);
        }
        for (UserInfoQchat user:this.UL) {
            user.setDeps(depTemp);
        }
    }

    public  static class UserInfoQchat{
        @JsonProperty("N")
        String N;

        @JsonProperty("U")
        String U;

        @JsonProperty("W")
        String W;

        @JsonProperty("DEP")
        String deps;

        public UserInfoQchat(String username, String userId, String dep) {
            this.N = username;
            this.U = userId;
            this.W = dep;
        }

        public void setN(String n) {
            N = n;
        }

        public void setU(String u) {
            U = u;
        }

        public void setW(String w) {
            W = w;
        }

        public void setDeps(String deps) {
            this.deps = deps;
        }

        public String getN() {
            return N;
        }

        public String getU() {
            return U;
        }

        public String getW() {
            return W;
        }

        public String getDeps() {
            return deps;
        }
    }
}