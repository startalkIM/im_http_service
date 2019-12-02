package com.qunar.qchat.service.impl;

import com.qunar.qchat.dao.IHostInfoDao;
import com.qunar.qchat.dao.model.HostInfoModel;
import com.qunar.qchat.service.IDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @auth dongzd.zhang
 * @Date 2019/7/9 10:40
 */
@Service
public class DomainServiceImpl implements IDomainService{

    @Autowired
    private IHostInfoDao hostInfoDao;

    @Override
    public HostInfoModel getDomain(String domain) {
        return hostInfoDao.selectHostInfoByHostName(domain);
    }

    @Override
    public boolean isToCDomain(String domain) {
        Integer hostType = hostInfoDao.selectHostType(domain);
        return hostType == 1;
    }

    @Override
    public boolean updateDomainQRCodePath(String qrCodePath, String domain) {
        Integer effectRow = hostInfoDao.updateHostQRCode(qrCodePath, domain);
        return effectRow > 0;
    }

    @Override
    public String getDomainQRCodePath(String domain) {
        return hostInfoDao.selectHostQRCodePath(domain);
    }

    @Override
    public Integer getDomainNeedApprove(String domain) {
        return hostInfoDao.selectNeedApproveByHost(domain);
    }


    @Override
    public boolean isToCDomainWithoutNull(String domain) {
        Integer hostType = hostInfoDao.selectHostType(domain);
        if (hostType == null || hostType == 0) {
            return false;
        } else {
            return true;
        }
    }
}
