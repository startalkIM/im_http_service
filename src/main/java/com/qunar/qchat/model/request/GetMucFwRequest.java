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
public class GetMucFwRequest implements IRequest {

    private String userid;
    private String groupid;

    @Override
    public boolean isRequestValid() {
        return !StringUtils.isEmpty(userid) && !StringUtils.isEmpty(groupid);
    }
}
