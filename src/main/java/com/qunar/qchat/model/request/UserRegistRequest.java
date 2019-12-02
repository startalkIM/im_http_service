package com.qunar.qchat.model.request;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @auth dongzd.zhang
 * @Date 2019/6/28 11:37
 */
@Data
@ToString
public class UserRegistRequest implements IRequest {

    private String telephone;
    private String username;
    private String domain;
    private String password;
    private String confirmpassword;
    private String description;

    @Override
    public boolean isRequestValid() {
        if(StringUtils.isEmpty(telephone) ||
                StringUtils.isEmpty(username) ||
                StringUtils.isEmpty(password) ||
                StringUtils.isEmpty(confirmpassword) ||
                StringUtils.isEmpty(domain)) {
            return false;
        }
        return true;
    }
}
