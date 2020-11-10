package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.HardnessTestReportEntity;
import com.ags.lumosframework.handler.IHardnessTestReportHandler;
import com.ags.lumosframework.pojo.HardnessTestReport;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IHardnessTestReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Primary
public class HardnessTestReportService extends AbstractBaseDomainObjectService<HardnessTestReport,HardnessTestReportEntity> implements IHardnessTestReportService {
	
	@Autowired
	private IHardnessTestReportHandler hardnessTestReportHandler;

	@Override
	protected IBaseEntityHandler<HardnessTestReportEntity> getEntityHandler() {
		return hardnessTestReportHandler;
	}
	
	@Override
	public HardnessTestReport getByNoBatchSubitem(String purchasingOrder,String sapInspectionLot,String purchasingItemNo){
		EntityFilter filter = createFilter();
        if (purchasingOrder != null && !"".equals(purchasingOrder)) {
            filter.fieldEqualTo(HardnessTestReportEntity.PURCHASE_ORDER, purchasingOrder);
        }
        if (sapInspectionLot != null && !"".equals(sapInspectionLot)) {
            filter.fieldEqualTo(HardnessTestReportEntity.SAP_BATCH_NO, sapInspectionLot);
        }
        if (purchasingItemNo != null && !"".equals(purchasingItemNo)) {
            filter.fieldEqualTo(HardnessTestReportEntity.PURCHASE_ORDER_SUBITEM, purchasingItemNo);
        }
        return getByFilter(filter);
	}

	@Override
	public String getInspectionTime(String sapLotNo) {
		EntityFilter filter = createFilter();
		filter.fieldEqualTo(HardnessTestReportEntity.SAP_BATCH_NO, sapLotNo);
		HardnessTestReport instance = getByFilter(filter);
		return instance.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
	}

    @Override
    public List<HardnessTestReport> getByPurchasingOrder(String purchasingOrder) {
        EntityFilter filter = createFilter();
        if (purchasingOrder != null && !"".equals(purchasingOrder)) {
            filter.fieldEqualTo(HardnessTestReportEntity.PURCHASE_ORDER, purchasingOrder);
        }
        return listByFilter(filter);
    }

    @Override
	public void deleteBysapLot(String sapLotNo) {
		EntityFilter filter = createFilter();
		filter.fieldEqualTo(HardnessTestReportEntity.SAP_BATCH_NO, sapLotNo);
		HardnessTestReport instance = getByFilter(filter);
		delete(instance);
	}
	
	
}
