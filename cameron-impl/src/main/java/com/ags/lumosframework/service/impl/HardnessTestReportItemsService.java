package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.HardnessTestReportItemsEntity;
import com.ags.lumosframework.handler.IHardnessTestReportItemsHandler;
import com.ags.lumosframework.pojo.HardnessTestReportItems;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IHardnessTestReportItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class HardnessTestReportItemsService extends AbstractBaseDomainObjectService<HardnessTestReportItems,HardnessTestReportItemsEntity> implements IHardnessTestReportItemsService {
	
	@Autowired
	private IHardnessTestReportItemsHandler hardnessTestReportItemsHandler;
	
	@Override
	protected IBaseEntityHandler<HardnessTestReportItemsEntity> getEntityHandler() {
		return hardnessTestReportItemsHandler;
	}
	
	@Override
	public List<HardnessTestReportItems> getByNoBatchSubitem(String purchaseOrder,String SAPBatchNo,String purchaseOrderSubitem){
		EntityFilter filter = createFilter();
        if (purchaseOrder != null && !"".equals(purchaseOrder)){
            filter.fieldEqualTo(HardnessTestReportItemsEntity.PURCHASE_ORDER, purchaseOrder);
        }
        if (SAPBatchNo != null && !"".equals(SAPBatchNo)){
            filter.fieldEqualTo(HardnessTestReportItemsEntity.SAP_BATCH_NO, SAPBatchNo);
        }
        if (purchaseOrderSubitem != null && !"".equals(purchaseOrderSubitem)){
            filter.fieldEqualTo(HardnessTestReportItemsEntity.PURCHASE_ORDER_SUBITEM, purchaseOrderSubitem);
        }
        
        return listByFilter(filter);
	}

	@Override
	public void deleteRecords(String sapLotNo) {
		List<Long> listIDs = new ArrayList<>();
		EntityFilter filter = createFilter();
		filter.fieldEqualTo(HardnessTestReportItemsEntity.SAP_BATCH_NO, sapLotNo);
		List<HardnessTestReportItems> list = listByFilter(filter);
		for(HardnessTestReportItems instance : list) {
			listIDs.add(instance.getId());
		}
		deleteByIds(listIDs);
	}

	@Override
	public HardnessTestReportItems getByPartNoSerialNo(String serialNo) {
		EntityFilter filter = createFilter();
        if (serialNo != null && !"".equals(serialNo)){
            filter.fieldEqualTo(HardnessTestReportItemsEntity.SERIAL_NO, serialNo);
        }
        return getByFilter(filter);
	}
	
	
}
