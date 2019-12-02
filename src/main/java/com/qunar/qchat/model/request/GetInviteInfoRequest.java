package com.qunar.qchat.model.request;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/6 12:15
 */
@Data
@ToString
public class GetInviteInfoRequest implements IRequest {

    private String user;
    private String d;
    private Long time;

    @Override
    public boolean isRequestValid() {
        if(StringUtils.isBlank(this.d) ||
                StringUtils.isBlank(this.user) ||
                this.time == null) {
            return false;
        }
        return true;
    }
}
