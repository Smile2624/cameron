package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.PressureTest;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IPressureTestService extends IBaseDomainObjectService<PressureTest> {
    PressureTest getPressureTestByProductSN(String productSN);
    
    List<PressureTest> getPressureTestByOrder(String orderNo);
}
