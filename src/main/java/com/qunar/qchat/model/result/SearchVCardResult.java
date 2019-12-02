package com.qunar.qchat.model.result;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/8 11:38
 */
public class SearchVCardResult {

    private String domain;
    private Object nickname;
    private Object username;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Object getNickname() {
        return nickname;
    }

    public void setNickname(Object nickname) {
        this.nickname = nickname;
    }

    public Object getUsername() {
        return username;
    }

    public void setUsername(Object username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "SearchVCardResult{" +
                "domain='" + domain + '\'' +
                ", nickname=" + nickname +
                ", username=" + username +
                '}';
    }
}
