package com.qunar.qchat.model.request;

import java.util.List;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/1 15:33
 */
public class GetMucVcardRequest {

    private String domain;
    private List<MucInfo> mucs;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<MucInfo> getMucs() {
        return mucs;
    }

    public void setMucs(List<MucInfo> mucs) {
        this.mucs = mucs;
    }

    @Override
    public String toString() {
        return "GetMucVcardRequest{" +
                "domain='" + domain + '\'' +
                ", mucs=" + mucs +
                '}';
    }

    public static class MucInfo {
        private String muc_name;
        private String version;

        public String getMuc_name() {
            return muc_name;
        }

        public void setMuc_name(String muc_name) {
            this.muc_name = muc_name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        @Override
        public String toString() {
            return "MucInfo{" +
                    "muc_name='" + muc_name + '\'' +
                    ", version='" + version + '\'' +
                    '}';
        }
    }

}



