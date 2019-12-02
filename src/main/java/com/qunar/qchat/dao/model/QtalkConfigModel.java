package com.qunar.qchat.dao.model;

import java.math.BigInteger;

public class QtalkConfigModel {
    private BigInteger id;
    private String configKey;
    private String configValue;

    public QtalkConfigModel() {
    }

    public QtalkConfigModel(String configKey, String configValue) {
        this.configKey = configKey;
        this.configValue = configValue;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
}
