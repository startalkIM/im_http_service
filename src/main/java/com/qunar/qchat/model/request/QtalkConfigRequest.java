package com.qunar.qchat.model.request;


import com.qunar.qchat.utils.JacksonUtils;
import org.apache.commons.lang3.StringUtils;

public class QtalkConfigRequest {
    private String ldapUrl;
    private String ldapBase;
    private String ldapUsername;
    private String ldapPassword;
    private String ldapSearchBase;
    private String ldapResultMapping;
    private String ldapDepartmentSplit;
    private String intervalTime;
    private boolean needDeleteData;


    public String getLdapUrl() {
        return ldapUrl;
    }

    public void setLdapUrl(String ldapUrl) {
        this.ldapUrl = ldapUrl;
    }

    public String getLdapBase() {
        return ldapBase;
    }

    public void setLdapBase(String ldapBase) {
        this.ldapBase = ldapBase;
    }

    public String getLdapUsername() {
        return ldapUsername;
    }

    public void setLdapUsername(String ldapUsername) {
        this.ldapUsername = ldapUsername;
    }

    public String getLdapPassword() {
        return ldapPassword;
    }

    public void setLdapPassword(String ldapPassword) {
        this.ldapPassword = ldapPassword;
    }

    public String getLdapSearchBase() {
        return ldapSearchBase;
    }

    public void setLdapSearchBase(String ldapSearchBase) {
        this.ldapSearchBase = ldapSearchBase;
    }

    public String getLdapResultMapping() {
        return ldapResultMapping;
    }

    public void setLdapResultMapping(String ldapResultMapping) {
        this.ldapResultMapping = ldapResultMapping;
    }

    public String getLdapDepartmentSplit() {
        return ldapDepartmentSplit;
    }

    public void setLdapDepartmentSplit(String ldapDepartmentSplit) {
        this.ldapDepartmentSplit = ldapDepartmentSplit;
    }

    public String getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(String intervalTime) {
        this.intervalTime = intervalTime;
    }

    public boolean isNeedDeleteData() {
        return needDeleteData;
    }

    public void setNeedDeleteData(boolean needDeleteData) {
        this.needDeleteData = needDeleteData;
    }

    static class ResultMapping{
        private String userId;
        private String userName;
        private String department;
        private String email;
        private String sex;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String checkMapping() {
            if(StringUtils.isAnyEmpty(userId, userName, department, email, sex)) {
                return "mapping param error";
            }
            return null;
        }
    }

    public String check() {
        if (StringUtils.isAnyEmpty(ldapUrl, ldapBase, ldapUsername, ldapPassword, ldapSearchBase, ldapResultMapping, intervalTime)) {
            return "param error";
        }
        ResultMapping resultMapping = JacksonUtils.string2Obj(ldapResultMapping, ResultMapping.class);
        if (resultMapping == null || StringUtils.isNotEmpty(resultMapping.checkMapping())) {
            return "resultMapping param error";
        }
        return null;
    }
}
