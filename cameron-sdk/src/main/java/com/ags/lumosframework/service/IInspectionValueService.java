package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.InspectionValue;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IInspectionValueService extends IBaseDomainObjectService<InspectionValue>{

	InspectionValue getByNameAndSpecification(String name , String Specification);
	
	List<InspectionValue> ListByName(String name);
}
