package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.InspectionPlanEntity;
import com.ags.lumosframework.handler.IInspectionPlanHandler;
import com.ags.lumosframework.pojo.InspectionPlan;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IInspectionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class InspectionPlanService extends AbstractBaseDomainObjectService<InspectionPlan, InspectionPlanEntity> implements IInspectionPlanService {

    @Autowired
    private IInspectionPlanHandler handler;
    @Override
    protected IBaseEntityHandler<InspectionPlanEntity> getEntityHandler() {
        return handler;
    }

    @Override
    public InspectionPlan getByPartNo(String name) {
        EntityFilter filter = createFilter();
        filter.fieldEqualTo(InspectionPlanEntity.PRODUCT_NO,name);
        return getByFilter(filter);
    }
}
