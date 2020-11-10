package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.Levp;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

public interface ILevpService extends IBaseDomainObjectService<Levp> {
    Levp getByOrderNo(String productionOrderNo);
}
