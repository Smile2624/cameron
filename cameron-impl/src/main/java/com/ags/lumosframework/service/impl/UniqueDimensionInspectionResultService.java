package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.UniqueDimensionInspectionResultEntity;
import com.ags.lumosframework.handler.IUniqueDimensionInspectionResultHandler;
import com.ags.lumosframework.pojo.UniqueDimensionInspectionResult;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IUniqueDimensionInspectionResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
@Service
@Primary
public class UniqueDimensionInspectionResultService extends AbstractBaseDomainObjectService<UniqueDimensionInspectionResult, UniqueDimensionInspectionResultEntity> implements IUniqueDimensionInspectionResultService {

	@Autowired
	private IUniqueDimensionInspectionResultHandler handler;
	@Override
	protected IBaseEntityHandler<UniqueDimensionInspectionResultEntity> getEntityHandler() {
		return handler;
	}
	@Override
	public UniqueDimensionInspectionResult getByPo(String po) {

		EntityFilter filter = createFilter();
		filter.fieldEqualTo(UniqueDimensionInspectionResultEntity.PO_NO, po);
		return getByFilter(filter);
	}

}
