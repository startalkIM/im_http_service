package com.qunar.qchat.model.request;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @auth dongzd.zhang
 * @Date 2019/6/28 20:40
 */
@Data
@ToString
public class ApproveUsersRequest implements IRequest {
    private List<Integer> userIds;
    private String domain;
    private Integer status;


    @Override
    public boolean isRequestValid() {
        if(StringUtils.isEmpty(domain)) {
            return false;
        }
        return true;
    }
}
