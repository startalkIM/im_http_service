package com.qunar.qchat.model.request;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @auth dongzd.zhang
 * @Date 2019/7/1 17:00
 */
@Data
@ToString
public class CheckDomainTypeRequest implements IRequest {
    private String domain;

    @Override
    public boolean isRequestValid() {
        if(StringUtils.isEmpty(domain)) {
            return false;
        }
        return true;
    }
}
