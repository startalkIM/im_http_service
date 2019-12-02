package com.qunar.qchat.model.result;

import lombok.Data;
import lombok.ToString;

import java.math.BigInteger;
import java.util.Date;

/**
 * @auth dongzd.zhang
 * @Date 2019/7/1 10:31
 */
@Data
@ToString
public class SearchApproveListResult {
    private BigInteger id;
    private String name;
    private String tel;
    private String desc;
    private Integer status;
    private Date createTime;
}
