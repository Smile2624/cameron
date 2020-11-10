package com.ags.lumosframework.service;


import com.ags.lumosframework.pojo.InspectionType;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IInspectionTypeService extends IBaseDomainObjectService<InspectionType>{

	InspectionType getByName(String name);
	
	List<String> getAllInspectionType();
}
