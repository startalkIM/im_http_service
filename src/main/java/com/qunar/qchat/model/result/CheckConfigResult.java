package com.qunar.qchat.model.result;

import java.util.List;

public class CheckConfigResult {

    /**
     * otherconfig : {"thanksurl":"https://qt.qunar.com/qtalkIosThanks.html","aacollectionurl":"https://tu.qunar.com/red_envelope_aa.php","myredpackageurl":"https://tu.qunar.com/my_red_env.php","balanceurl":"https://pay.qunar.com/m/member/asset/balance/withdraw.html","cachefileurl":"http://l-tqserver1.cc.dev.cn6.qunar.com:8000","redpackageurl":"https://tu.qunar.com/red_envelope.php"}
     * exists : true
     * company : qunar
     * ability : {"base":["friend","group"],"group":["redpackage"]}
     * trdextendmsg : [{"trdextendId":"Task_list","scope":3,"icon":"https://qt.qunar.com/trd_extend_icons/toupiao.png","i18NTitle":"","linkType":1,"linkurl":"http://tu.qunar.com/vote/vote_list.php","title":"任务系统","support":2}]
     * version : 1
     */
    private OtherconfigEntity otherconfig;
    private boolean exists;
    private String company;
    private AbilityEntity ability;
    private List<TrdextendmsgEntity> trdextendmsg;
    private int version;

    public void setOtherconfig(OtherconfigEntity otherconfig) {
        this.otherconfig = otherconfig;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setAbility(AbilityEntity ability) {
        this.ability = ability;
    }

    public void setTrdextendmsg(List<TrdextendmsgEntity> trdextendmsg) {
        this.trdextendmsg = trdextendmsg;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public OtherconfigEntity getOtherconfig() {
        return otherconfig;
    }

    public boolean isExists() {
        return exists;
    }

    public String getCompany() {
        return company;
    }

    public AbilityEntity getAbility() {
        return ability;
    }

    public List<TrdextendmsgEntity> getTrdextendmsg() {
        return trdextendmsg;
    }

    public int getVersion() {
        return version;
    }

    public static class OtherconfigEntity {
        /**
         * thanksurl : https://qt.qunar.com/qtalkIosThanks.html
         * aacollectionurl : https://tu.qunar.com/red_envelope_aa.php
         * myredpackageurl : https://tu.qunar.com/my_red_env.php
         * balanceurl : https://pay.qunar.com/m/member/asset/balance/withdraw.html
         * cachefileurl : http://l-tqserver1.cc.dev.cn6.qunar.com:8000
         * redpackageurl : https://tu.qunar.com/red_envelope.php
         */
        private String thanksurl;
        private String aacollectionurl;
        private String myredpackageurl;
        private String balanceurl;
        private String cachefileurl;
        private String redpackageurl;

        public void setThanksurl(String thanksurl) {
            this.thanksurl = thanksurl;
        }

        public void setAacollectionurl(String aacollectionurl) {
            this.aacollectionurl = aacollectionurl;
        }

        public void setMyredpackageurl(String myredpackageurl) {
            this.myredpackageurl = myredpackageurl;
        }

        public void setBalanceurl(String balanceurl) {
            this.balanceurl = balanceurl;
        }

        public void setCachefileurl(String cachefileurl) {
            this.cachefileurl = cachefileurl;
        }

        public void setRedpackageurl(String redpackageurl) {
            this.redpackageurl = redpackageurl;
        }

        public String getThanksurl() {
            return thanksurl;
        }

        public String getAacollectionurl() {
            return aacollectionurl;
        }

        public String getMyredpackageurl() {
            return myredpackageurl;
        }

        public String getBalanceurl() {
            return balanceurl;
        }

        public String getCachefileurl() {
            return cachefileurl;
        }

        public String getRedpackageurl() {
            return redpackageurl;
        }
    }

    public static class AbilityEntity {
        /**
         * base : ["friend","group"]
         * group : ["redpackage"]
         */
        private List<String> base;
        private List<String> group;

        public void setBase(List<String> base) {
            this.base = base;
        }

        public void setGroup(List<String> group) {
            this.group = group;
        }

        public List<String> getBase() {
            return base;
        }

        public List<String> getGroup() {
            return group;
        }
    }

    public static class TrdextendmsgEntity {
        /**
         * trdextendId : Task_list
         * scope : 3
         * icon : https://qt.qunar.com/trd_extend_icons/toupiao.png
         * i18NTitle :
         * linkType : 1
         * linkurl : http://tu.qunar.com/vote/vote_list.php
         * title : 任务系统
         * support : 2
         */
        private String trdextendId;
        private int scope;
        private String icon;
        private String i18NTitle;
        private int linkType = 0;
        private String linkurl="";
        private String title;
        private int support;

        public void setTrdextendId(String trdextendId) {
            this.trdextendId = trdextendId;
        }

        public void setScope(int scope) {
            this.scope = scope;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setI18NTitle(String i18NTitle) {
            this.i18NTitle = i18NTitle;
        }

        public void setLinkType(int linkType) {
            this.linkType = linkType;
        }

        public void setLinkurl(String linkurl) {
            this.linkurl = linkurl;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setSupport(int support) {
            this.support = support;
        }

        public String getTrdextendId() {
            return trdextendId;
        }

        public int getScope() {
            return scope;
        }

        public String getIcon() {
            return icon;
        }

        public String getI18NTitle() {
            return i18NTitle;
        }

        public int getLinkType() {
            return linkType;
        }

        public String getLinkurl() {
            return linkurl;
        }

        public String getTitle() {
            return title;
        }

        public int getSupport() {
            return support;
        }
    }
}
