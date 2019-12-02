package com.qunar.qchat.model.request;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/1 12:09
 */
@Data
@ToString
public class UpdateMucNickRequest implements IRequest {

    private String muc_name;
    private String nick;
    private String title;
    private String desc;

    @Override
    public boolean isRequestValid() {
        if(StringUtils.isBlank(this.muc_name)) {
            return false;
        }
        return true;
    }
}
