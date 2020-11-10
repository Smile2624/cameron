package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.BomNameAuthority;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IBomNameAuthorityService extends IBaseDomainObjectService<BomNameAuthority> {
	BomNameAuthority getByTypeValue(String authorityType,String nameEigenvalue);
	
	List<BomNameAuthority> getByTypeValue(String authorityType,String nameEigenvalue,long id);
}
