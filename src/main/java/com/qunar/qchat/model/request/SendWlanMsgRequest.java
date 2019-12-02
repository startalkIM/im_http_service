package com.qunar.qchat.model.request;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/9 13:55
 */

@Data
@ToString
public class SendWlanMsgRequest implements IRequest {

    private String from;
    private String type;
    //private String domain;
    private String key;
    private String count;
   /* private String fromHost;
    private String toHost;*/
    private String msg_type;
    private String body;
    private String extend_info;
    private List<ToUser> to;

    @Override
    public boolean isRequestValid() {
        if(StringUtils.isBlank(from) ||
                from.split("@").length < 2 ||
                StringUtils.isBlank(key) ||
                StringUtils.isBlank(count) ||
                StringUtils.isBlank(type) ||
                StringUtils.isBlank(msg_type) ||
                CollectionUtils.isEmpty(to)) {

            return false;
        }

        for(ToUser toUser : to) {
            String u = toUser.getUser();
            if(StringUtils.isBlank(u) ||
                    u.split("@").length < 2) {
                return false;
            }

            if("consult".equals(getType())) {
                if (StringUtils.isBlank(toUser.getChannelid()) ||
                        StringUtils.isBlank(toUser.getQchatid())) {
                    return false;
                }

                if ("5".equals(toUser.getQchatid()) &&
                        StringUtils.isBlank(toUser.getRealto())) {
                    return false;
                }
            }
        }

        return true;
    }


    @Data
    @ToString
    public static class ToUser{
        private String user;
        private String realto;
        private String channelid;
        private String qchatid;
    }

}
