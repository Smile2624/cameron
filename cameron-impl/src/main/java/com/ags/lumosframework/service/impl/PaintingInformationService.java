package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.PaintingInformationEntity;
import com.ags.lumosframework.handler.IPaintingInformationHandler;
import com.ags.lumosframework.impl.handler.DBHandler;
import com.ags.lumosframework.pojo.PaintingInformation;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IPaintingInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class PaintingInformationService extends AbstractBaseDomainObjectService<PaintingInformation,PaintingInformationEntity> implements IPaintingInformationService {
	
	@Autowired
	private IPaintingInformationHandler paintingInformationHandler;
	
	@Autowired
	private DBHandler dbHandler;
	
	@Override
	protected IBaseEntityHandler<PaintingInformationEntity> getEntityHandler() {
		return paintingInformationHandler;
	}
	
	@Override
    public List<PaintingInformation>  getBySn(String workOrderSN) {
        EntityFilter filter = createFilter();
        if (workOrderSN != null && !"".equals(workOrderSN)) {
            filter.fieldEqualTo(PaintingInformationEntity.WO_SN, workOrderSN);
        }
        filter.fieldIsNull(PaintingInformationEntity.QC_CONFIRMER, true);
        filter.fieldIsNull(PaintingInformationEntity.OP_USER, false);
        return listByFilter(filter);
    }

	@Override
	public PaintingInformation getByOrderNo(String orderNo) {

        EntityFilter filter = createFilter();
        if (orderNo != null && !"".equals(orderNo)) {
            filter.fieldEqualTo(PaintingInformationEntity.WO_SN, orderNo);
        }
		return getByFilter(filter);
	}

	@Override
	public List<String> getAllButPaintOrder() {
		// TODO Auto-generated method stub
		String stringSql = "select po.PRODUCT_ORDER_ID from product_order po where po.PRODUCT_ORDER_ID "
				+ " not in (select pi.wo_sn from painting_information pi )";
		return (List<String>)dbHandler.listBySql(stringSql, null);
	}
	
}
