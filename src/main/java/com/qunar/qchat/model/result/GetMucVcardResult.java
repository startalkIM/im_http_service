package com.qunar.qchat.model.result;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @auth dongzd.zhang
 * @Date 2018/11/1 16:16
 */
public class GetMucVcardResult {

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
        return "GetMucVcardResult{" +
                "domain='" + domain + '\'' +
                ", mucs=" + mucs +
                '}';
    }

    public static class MucInfo {

        private String MN;

        private String SN;

        private String MD;

        private String MT;

        private String MP;

        private String VS;

        private String UT;


        @JsonProperty("MN")
        public String getMN() {
            return MN;
        }

        public void setMN(String MN) {
            this.MN = MN;
        }

        @JsonProperty("SN")
        public String getSN() {
            return SN;
        }

        public void setSN(String SN) {
            this.SN = SN;
        }

        @JsonProperty("MD")
        public String getMD() {
            return MD;
        }

        public void setMD(String MD) {
            this.MD = MD;
        }

        @JsonProperty("MT")
        public String getMT() {
            return MT;
        }

        public void setMT(String MT) {
            this.MT = MT;
        }

        @JsonProperty("MP")
        public String getMP() {
            return MP;
        }

        public void setMP(String MP) {
            this.MP = MP;
        }

        @JsonProperty("VS")
        public String getVS() {
            return VS;
        }

        public void setVS(String VS) {
            this.VS = VS;
        }

        @JsonProperty("UT")
        public String getUT() {
            return UT;
        }

        public void setUT(String UT) {
            this.UT = UT;
        }

        @Override
        public String toString() {
            return "MucInfo{" +
                    "MN='" + MN + '\'' +
                    ", SN='" + SN + '\'' +
                    ", MD='" + MD + '\'' +
                    ", MT='" + MT + '\'' +
                    ", MP='" + MP + '\'' +
                    ", VS='" + VS + '\'' +
                    ", UT='" + UT + '\'' +
                    '}';
        }
    }
}
