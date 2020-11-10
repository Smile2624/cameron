package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.UniqueDimensionInspection;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IUniqueDimensionInspectionService extends IBaseDomainObjectService<UniqueDimensionInspection>{

	List<UniqueDimensionInspection> getByPoAndMaterailInfo(String po,String materialNo,String materialRev);
	List<UniqueDimensionInspection> getAll(String po);
}
