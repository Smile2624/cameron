package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.DimensionRuler;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IDimensionRulerService extends IBaseDomainObjectService<DimensionRuler> {
	List<DimensionRuler> getByNoRev(String materialNo,String materialRev);
	
	DimensionRuler getByNoRevItemName(String materialNo,String materialRev,String inspectionItemName);

	void deleteRulerList(String materialNo,String materialRev);

	//***********New delete line item************************************
	void deleteRulerListdetail(String materialNo, String inspectionItemName);
}
