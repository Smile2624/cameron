package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.ReceivingInspectionEntity;
import com.ags.lumosframework.handler.IReceivingInspectionHandler;
import com.ags.lumosframework.pojo.ReceivingInspection;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IReceivingInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class ReceivingInspectionService extends AbstractBaseDomainObjectService<ReceivingInspection,ReceivingInspectionEntity> implements IReceivingInspectionService {

	@Autowired
	private IReceivingInspectionHandler handler;
	@Override
	protected IBaseEntityHandler<ReceivingInspectionEntity> getEntityHandler() {
		return handler;
	}
	@Override
	public ReceivingInspection getBySapLotNo(String sapLotNo) {
		// TODO Auto-generated method stub
		EntityFilter filter = this.createFilter();
		filter.fieldEqualTo(ReceivingInspectionEntity.SAP_INSPECTION_NO, sapLotNo);
		return getByFilter(filter);
	}

}
