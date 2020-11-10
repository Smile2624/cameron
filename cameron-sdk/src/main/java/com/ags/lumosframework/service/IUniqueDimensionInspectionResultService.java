package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.UniqueDimensionInspectionResult;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

public interface IUniqueDimensionInspectionResultService extends IBaseDomainObjectService<UniqueDimensionInspectionResult>{

	UniqueDimensionInspectionResult getByPo(String po);
}
