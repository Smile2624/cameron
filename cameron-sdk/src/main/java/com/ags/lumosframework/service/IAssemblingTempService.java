package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.AssemblingTemp;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

public interface IAssemblingTempService extends IBaseDomainObjectService<AssemblingTemp> {

    AssemblingTemp getByOrderNo(String orderNo);

}
