package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.ReceivingInspection;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

public interface IReceivingInspectionService extends IBaseDomainObjectService<ReceivingInspection>{
	
	ReceivingInspection getBySapLotNo(String sapLotNo);
}
