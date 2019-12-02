package com.qunar.qchat.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * UserInfoQtalk
 *
 * @author binz.zhang
 * @date 2018/10/12
 */
@Getter
@Setter
public class UserInfoQtalk {

    @JsonIgnore
    private long host_id = 1;

    @JsonProperty("U")
    private String user_id;

    @JsonIgnore
    private String password = "";

    @JsonProperty("N")
    private String user_name;

    @JsonProperty("D")
    private String department = "";

    @JsonIgnore
    private String ps_deptid = "";

    @JsonIgnore
    private String dep1 = "";

    @JsonIgnore
    private String dep2 = "";

    @JsonIgnore
    private String dep3 = "";

    @JsonIgnore
    private String dep4 = "";

    @JsonIgnore
    private String dep5 = "";

    @JsonProperty("pinyin")
    private String pinyin = "";

    @JsonIgnore
    private String joinDate;

    @JsonIgnore
    private Integer hire_flag;

    @JsonIgnore
    private Integer version;

    @JsonProperty("sex")
    public Integer gender = 1;

    @JsonIgnore
    public String sex;

    @JsonProperty("uType")
    public String user_type = "";

    @JsonProperty("email")
    public String email = "";

    @JsonIgnore
    private int frozen_flag = 1;

    @JsonIgnore
    private String leader;
    private boolean visibleFlag = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfoQtalk that = (UserInfoQtalk) o;
        return Objects.equals(user_name, that.user_name) &&
                Objects.equals(user_id, that.user_id) &&
                Objects.equals(department, that.department) &&
                Objects.equals(dep1, that.dep1) &&
                Objects.equals(dep2, that.dep2) &&
                Objects.equals(dep3, that.dep3) &&
                Objects.equals(dep4, that.dep4) &&
                Objects.equals(dep5, that.dep5) &&
//                Objects.equals(hire_flag, that.hire_flag) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host_id, user_id, user_name, department, dep1, dep2, dep3, dep4, dep5, email);
    }
}
