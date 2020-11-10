package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.HardnessTestReportItems;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IHardnessTestReportItemsService extends IBaseDomainObjectService<HardnessTestReportItems> {
	
	List<HardnessTestReportItems> getByNoBatchSubitem(String purchaseOrder,String SAPBatchNo,String purchaseOrderSubitem);
	
	void deleteRecords(String sapLotNo);
	
	HardnessTestReportItems getByPartNoSerialNo(String serialNo);
}
