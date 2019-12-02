package com.qunar.qchat.model.request;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @auth dongzd.zhang
 * @Date 2019/6/27 16:43
 */
@Data
@ToString
public class SendRegistValidateCodeRequest implements IRequest {

    private String telephone;
    private String domain;
    private String type;

    @Override
    public boolean isRequestValid() {
        if(StringUtils.isEmpty(telephone) ||
                StringUtils.isEmpty(domain) ||
                StringUtils.isEmpty(type)) {
            return false;
        }
        return true;
    }
}
