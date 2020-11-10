package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.CertificateOfConformanceEntity;
import com.ags.lumosframework.handler.ICertificateOfConformanceHandler;
import com.ags.lumosframework.impl.handler.DBHandler;
import com.ags.lumosframework.pojo.CertificateOfConformance;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.ICertificateOfConformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class CertificateOfConformanceService extends AbstractBaseDomainObjectService<CertificateOfConformance, CertificateOfConformanceEntity> implements ICertificateOfConformanceService {

    @Autowired
    private ICertificateOfConformanceHandler certificateOfConformanceHandler;
    @Autowired
    private DBHandler dbHandler;

    @Override
    protected IBaseEntityHandler<CertificateOfConformanceEntity> getEntityHandler() {
        return certificateOfConformanceHandler;
    }

    @Override
    public CertificateOfConformance getByOrder(String workOrderSN) {
        EntityFilter filter = createFilter();
        if (workOrderSN != null && !"".equals(workOrderSN)) {
            filter.fieldEqualTo(CertificateOfConformanceEntity.ORDER_NO, workOrderSN);
        }

        return getByFilter(filter);
    }

    @Override
    public CertificateOfConformance getByCertificateNo(String cocNo) {
        EntityFilter filter = createFilter();
        if (cocNo != null && !"".equals(cocNo)) {
            filter.fieldEqualTo(CertificateOfConformanceEntity.CERTIFICATE_NUMBER, cocNo);
        }

        return getByFilter(filter);
    }


    @Override
    public int getCertificateNumberNext(String prefix) {
        int reNum = 1;
        String sql = "SELECT cc.certificate_number FROM certificate_conformance cc " +
                "WHERE cc.certificate_number like'" + prefix + "%' " +
                "order by cc.certificate_number desc";
        List<?> list = dbHandler.listBySql(sql, null);
        if (list != null && list.size() > 0) {
            reNum = Integer.parseInt(list.get(0).toString().split("-")[2]) + 1;
        }
        return reNum;
    }

}
