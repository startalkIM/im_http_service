package com.qunar.qchat.model.request;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @auth dongzd.zhang
 * @Date 2018/10/18 14:44
 */
public class IncreUsersRequest implements IRequest {

    private Integer version;
    private String domain;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }


    @Override
    public boolean isRequestValid() {

        //System.out.println("version: " + version + ", domain: " + domain);

        if(Objects.isNull(version)
                || StringUtils.isBlank(domain)
                || version < 0) {
            return false;
        }
        return true;
    }

}
