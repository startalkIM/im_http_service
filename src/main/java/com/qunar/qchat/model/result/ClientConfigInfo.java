package com.qunar.qchat.model.result;

import java.util.List;

/**
 * create by hubo.hu (lex) at 2018/6/14
 */
public class ClientConfigInfo {

    public String key;
    public List<Info> infos;

    public static class Info {
        public String subkey;
        public String configinfo;
        public int isdel;
    }
}
