package com.qunar.qchat.service;

import com.qunar.qchat.dao.model.HostInfoModel;

import java.util.List;
import java.util.Map;

/**
 * @auth dongzd.zhang
 * @Date 2019/7/9 10:39
 */
public interface IDomainService {

    HostInfoModel getDomain(String domain);

    boolean isToCDomain(String domain);

    boolean updateDomainQRCodePath(String qrCodePath, String domain);

    String getDomainQRCodePath(String domain);

    Integer getDomainNeedApprove(String domain);
    boolean isToCDomainWithoutNull(String domain);


}
