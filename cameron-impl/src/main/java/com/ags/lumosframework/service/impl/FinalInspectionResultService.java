package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.FinalInspectionResultEntity;
import com.ags.lumosframework.handler.IFinalInspectionResultHandler;
import com.ags.lumosframework.pojo.FinalInspectionResult;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.filter.dql.EntityQuery;
import com.ags.lumosframework.sdk.base.filter.dql.MatchMode;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IFinalInspectionResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class FinalInspectionResultService extends AbstractBaseDomainObjectService<FinalInspectionResult, FinalInspectionResultEntity> implements IFinalInspectionResultService {

    @Autowired
    private IFinalInspectionResultHandler finalInspectionResultHandler;

    @Override
    protected IBaseEntityHandler<FinalInspectionResultEntity> getEntityHandler() {
        return finalInspectionResultHandler;
    }

    @Override
    public List<FinalInspectionResult> getFinalInspectionResultByOrderNo(String orderNo) {
        EntityFilter filter = createFilter();
        if (orderNo != null && !"".equals(orderNo)) {
            filter.fieldEqualTo(FinalInspectionResultEntity.ORDER_NO, orderNo);
        }
        return listByFilter(filter);
    }

    @Override
    public List<FinalInspectionResult> getFinalInspectionResultByOrderNo(String orderNo, String type) {
        EntityFilter filter = createFilter();
        if (orderNo != null && !"".equals(orderNo)) {
            filter.fieldEqualTo(FinalInspectionResultEntity.ORDER_NO, orderNo);
        }
        if (type != null && !"".equals(type)) {
            if ("QA".equals(type)) {
                filter.fieldIsNull(FinalInspectionResultEntity.QA_CONFIRM_DATE, false);
                filter.fieldIsNull(FinalInspectionResultEntity.QA_CONFIRM_USER, false);
            } else if ("QC".equals(type)) {
                filter.fieldIsNull(FinalInspectionResultEntity.QC_CONFIRM_DATE, false);
                filter.fieldIsNull(FinalInspectionResultEntity.QC_CONFIRM_USER, false);
            }
        }
        return listByFilter(filter);
    }

    @Override
    public FinalInspectionResult getByOrderNo(String orderNo) {
        EntityFilter filter = createFilter();
        if (orderNo != null && !"".equals(orderNo)) {
            filter.fieldEqualTo(FinalInspectionResultEntity.ORDER_NO, orderNo);
        }
        return getByFilter(filter);
    }

    @Override
    public List<FinalInspectionResult> getByReportNo(String prefix) {
        EntityFilter filter = createFilter();
        if (prefix instanceof String) {
            EntityQuery newEQ = filter.getEntityQuery();
            newEQ.add(filter.like(FinalInspectionResultEntity.REPORT_NO, false, prefix, MatchMode.START));
            filter.setEntityQuery(newEQ);
        }
        filter.orderBy(FinalInspectionResultEntity.REPORT_NO, true);
        return listByFilter(filter);
    }
}
