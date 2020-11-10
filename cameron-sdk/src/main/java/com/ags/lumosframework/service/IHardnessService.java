package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.Hardness;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

public interface IHardnessService extends IBaseDomainObjectService<Hardness> {
	Hardness getByHardnessFile(String hardnessFile);
	Hardness getByRuleName(String rulerName);
}
