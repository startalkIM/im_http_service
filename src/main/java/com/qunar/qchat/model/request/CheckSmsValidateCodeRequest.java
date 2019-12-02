package com.qunar.qchat.model.request;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @auth dongzd.zhang
 * @Date 2019/6/28 11:14
 */
@Data
@ToString
public class CheckSmsValidateCodeRequest implements IRequest {

    private String telephone;
    private String code;

    @Override
    public boolean isRequestValid() {
        if(StringUtils.isEmpty(telephone) ||
                StringUtils.isEmpty(code)) {
            return false;
        }
        return true;
    }
}
