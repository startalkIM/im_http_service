package com.qunar.qchat.model.request;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/8 15:04
 */
public class GetRecommendRobotRequest {
    private Integer type = -1;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "GetRecommendRobotRequest{" +
                "type='" + type + '\'' +
                '}';
    }
}
