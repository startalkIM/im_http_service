package com.qunar.qchat.model.result;

import java.util.ArrayList;
import java.util.List;

/**
 * 增量配置返回结果
 * create by hubo.hu (lex) at 2018/6/15
 */
public class IncreClientConfigResult {

    public long version;
    public List<ClientConfigInfo> clientConfigInfos = new ArrayList<>();

}
