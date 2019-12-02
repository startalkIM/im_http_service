package com.qunar.qchat.aop.routingdatasource;

/**
 * 数据源枚举
 * 如果添加了新的数据源，请修改这个枚举
 *
 * @author chengya.li on 14-6-23 11:26
 */
public enum DataSources {

    /**
     * TTS主库
     */
    QIM_SLAVE("qim-slave"),
    QCHAT_SLAVE("qchat-slave"),
    HASH_USER1("hash-user1")
    ;

    /**
     * Spring中配置的数据源的id
     */


    private String key;

    private DataSources(String key) {this.key = key;}

    public String key(){return key;}

}
