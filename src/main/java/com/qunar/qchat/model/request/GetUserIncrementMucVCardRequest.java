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
public class GetUserIncrementMucVCardRequest implements IRequest {

    private String userid;
    private String lastupdtime;

    @Override
    public boolean isRequestValid() {
        if(StringUtils.isEmpty(userid) ||
                StringUtils.isEmpty(lastupdtime)) {
            return false;
        }

        try{
            Long.parseLong(lastupdtime);
        }catch (Exception ex) {
            return false;
        }

        return true;
    }
}
