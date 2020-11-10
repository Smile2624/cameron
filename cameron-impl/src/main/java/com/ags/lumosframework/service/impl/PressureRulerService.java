package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.PressureRulerEntity;
import com.ags.lumosframework.handler.IPressureRulerHandler;
import com.ags.lumosframework.pojo.PressureRuler;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IPressureRulerService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class PressureRulerService extends AbstractBaseDomainObjectService<PressureRuler, PressureRulerEntity> implements IPressureRulerService{

	@Autowired
	private IPressureRulerHandler handler;
	
	@Override
	protected IBaseEntityHandler<PressureRulerEntity> getEntityHandler() {
		return handler;
	}

	@Override
	public List<PressureRuler> getAllByProductNo(String productNo) {
		EntityFilter filter = createFilter();
		if(!Strings.isNullOrEmpty(productNo)){
			filter.fieldContains(PressureRulerEntity.PRODUCT_NO, productNo);
		}
		filter.orderBy(PressureRulerEntity.PRODUCT_NO, false);
		return listByFilter(filter);
	}

	@Override
	public List<PressureRuler> getByRulerNo(String rulerNo) {
		EntityFilter filter = createFilter();
		filter.fieldEqualTo(PressureRulerEntity.RULER_NO, rulerNo);
		return listByFilter(filter);
	}

	@Override
	public PressureRuler getByProductNoAndPressureType(String productNo, String pressureType) {
		EntityFilter filter = createFilter();
		filter.fieldEqualTo(PressureRulerEntity.PRODUCT_NO, productNo);
		filter.fieldEqualTo(PressureRulerEntity.PRESSURE_TYPE, pressureType);
		return getByFilter(filter);
	}

	@Override
	public List<PressureRuler> getByProductNo(String productNo) {
		EntityFilter filter = createFilter();
		filter.fieldEqualTo(PressureRulerEntity.PRODUCT_NO, productNo);
		return listByFilter(filter);
	}

}
