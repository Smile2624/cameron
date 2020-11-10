package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.PaintingSpecification;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IPaintingSpecificationService extends IBaseDomainObjectService<PaintingSpecification> {
	
	PaintingSpecification getBySpecificationFile(String paintingSpecificationFileValue);
	
	List<PaintingSpecification> getAll();
}
