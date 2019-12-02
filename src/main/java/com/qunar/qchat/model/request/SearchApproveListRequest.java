package com.qunar.qchat.model.request;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * @auth dongzd.zhang
 * @Date 2019/6/28 19:39
 */
@Data
@ToString
public class SearchApproveListRequest implements IRequest {

    private String keyword;
    private String domain;
    private Integer status;
    private Integer offset;
    private Integer limit;

    @Override
    public boolean isRequestValid() {
        if(StringUtils.isEmpty(domain)) {
            return false;
        }
        if(offset == null) {
            offset = 0;
        }

        if(limit == null) {
            limit = 10;
        }
        return true;
    }
}
