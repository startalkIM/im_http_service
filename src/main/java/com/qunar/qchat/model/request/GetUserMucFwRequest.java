package com.qunar.qchat.model.request;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @auth dongzd.zhang
 * @Date 2019/5/27 15:50
 */
@Data
@ToString
public class GetUserMucFwRequest implements IRequest {

    private String userid;

    @Override
    public boolean isRequestValid() {
        if(StringUtils.isEmpty(userid)) {
            return false;
        }



        return true;
    }
}
