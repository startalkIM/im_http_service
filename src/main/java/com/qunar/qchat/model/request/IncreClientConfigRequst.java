package com.qunar.qchat.model.request;

import org.apache.http.util.TextUtils;

/**
 * 查询增量配置请求参数
 * create by hubo.hu (lex) at 2018/6/14
 */
public class IncreClientConfigRequst implements IRequest {

    public String username;
    public String host;
    public long version;

    @Override
    public boolean isRequestValid() {
        if(TextUtils.isEmpty(username)
                || TextUtils.isEmpty(host)
                || version < 0) {
            return false;
        }

        return true;
    }
}
