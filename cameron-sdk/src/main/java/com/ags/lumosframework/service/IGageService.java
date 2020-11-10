package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.Gage;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

public interface IGageService extends IBaseDomainObjectService<Gage> {
	Gage getByGageNo(String gageNo);
}
