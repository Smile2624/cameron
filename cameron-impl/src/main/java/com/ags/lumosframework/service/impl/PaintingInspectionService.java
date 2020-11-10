package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.PaintingInspectionEntity;
import com.ags.lumosframework.handler.IPaintingInspectionHandler;
import com.ags.lumosframework.pojo.PaintingInspection;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IPaintingInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class PaintingInspectionService extends AbstractBaseDomainObjectService<PaintingInspection,PaintingInspectionEntity> implements IPaintingInspectionService {

	@Autowired
	private IPaintingInspectionHandler paintingInspectionHandler;
	
	@Override
	protected IBaseEntityHandler<PaintingInspectionEntity> getEntityHandler() {
		return paintingInspectionHandler;
	}

	@Override
	public PaintingInspection getByNo(String orderNo) {
		EntityFilter filter = createFilter();
		if(orderNo!=null && !orderNo.equals("")) {
			filter.fieldEqualTo(PaintingInspectionEntity.WO_SN, orderNo);
		}
		return getByFilter(filter);
	}

}
