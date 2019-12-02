package com.qunar.qchat.model.request;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @auth dongzd.zhang
 * @Date 2019/7/3 16:58
 */
@Data
@ToString
public class CheckApproveSmsValidateCodeRequest implements IRequest {

    private String telephone;
    private String code;
    private String domain;

    @Override
    public boolean isRequestValid() {
        if(StringUtils.isEmpty(telephone) ||
                StringUtils.isEmpty(code) ||
                StringUtils.isEmpty(domain)) {
            return false;
        }
        return true;
    }
}
