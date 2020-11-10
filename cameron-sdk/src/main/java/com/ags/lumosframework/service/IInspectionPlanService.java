package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.InspectionPlan;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

public interface IInspectionPlanService extends IBaseDomainObjectService<InspectionPlan>{
    InspectionPlan getByPartNo(String name);
}