package com.qunar.qchat.model.request;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @auth dongzd.zhang
 * @Date 2018/12/3 20:40
 */
@Data
@ToString
public class GetHostUserDecorationRequest implements IRequest {

    private String userId;
    private String host;
    // 增量查询
    private String time;

    @Override
    public boolean isRequestValid() {
        if(StringUtils.isBlank(userId) ||
                StringUtils.isBlank(host)) {
            return false;
        }
        return true;
    }
}
