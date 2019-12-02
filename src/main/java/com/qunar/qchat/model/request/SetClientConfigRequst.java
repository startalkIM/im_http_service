package com.qunar.qchat.model.request;

import org.apache.http.util.TextUtils;

import java.util.List;

/**
 * 设置或删除配置请求参数
 * create by hubo.hu (lex) at 2018/6/14
 */
public class SetClientConfigRequst implements IRequest {

    public String username;
    public String host;
    public String key;
    public String subkey;
    public String value;
    public String resource;//用户resource
    public String operate_plat;
    public int version;
    public int type;//操作类型，1：设置config  2：取消或删除
    public List<BatchProcess> batchProcess;//批量修改优先级高，先处理list，如果没有则处理key，subkey，value

    @Override
    public boolean isRequestValid() {
        if(TextUtils.isEmpty(username)
                || TextUtils.isEmpty(host)
                || TextUtils.isEmpty(operate_plat)
                || TextUtils.isEmpty(resource)
                || (type != 1 && type != 2)
                || version < 0) {
            return false;
        } else {
            if(batchProcess != null && batchProcess.size() > 0){
                return true;
            } else {
                if(TextUtils.isEmpty(key)
                        || TextUtils.isEmpty(subkey)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static class BatchProcess {
        public String key;
        public String subkey;
        public String value;

        @Override
        public int hashCode() {
            return (key + subkey).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null) return false;
            if(obj == this) return true;
            if(obj instanceof BatchProcess) {
                BatchProcess bp = (BatchProcess) obj;
                if(bp.key.equalsIgnoreCase(this.key)
                        && bp.subkey.equalsIgnoreCase(this.subkey)) {
                    return true;
                }
            }
            return false;
        }
    }
}
