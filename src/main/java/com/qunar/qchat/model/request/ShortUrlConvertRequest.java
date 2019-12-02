package com.qunar.qchat.model.request;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @auth dongzd.zhang
 * @Date 2019/7/11 17:08
 */
@Data
@ToString
public class ShortUrlConvertRequest implements IRequest {

    private String url;

    @Override
    public boolean isRequestValid() {
        if(StringUtils.isEmpty(url)) {
            return false;
        }
        return true;
    }
}