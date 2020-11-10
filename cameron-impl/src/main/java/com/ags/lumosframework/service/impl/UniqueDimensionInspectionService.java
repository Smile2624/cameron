package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.UniqueDimensionInspectionEntity;
import com.ags.lumosframework.handler.IUniqueDimensionInspectionHandler;
import com.ags.lumosframework.pojo.UniqueDimensionInspection;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IUniqueDimensionInspectionService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Primary
public class UniqueDimensionInspectionService extends AbstractBaseDomainObjectService<UniqueDimensionInspection, UniqueDimensionInspectionEntity> implements IUniqueDimensionInspectionService {

	@Autowired
	private IUniqueDimensionInspectionHandler handler;
	@Override
	protected IBaseEntityHandler<UniqueDimensionInspectionEntity> getEntityHandler() {
		return handler;
	}
	@Override
	public List<UniqueDimensionInspection> getByPoAndMaterailInfo(String po, String materialNo, String materialRev) {
		EntityFilter filter = this.createFilter();
		if(!Strings.isNullOrEmpty(po)) {
			filter.fieldEqualTo(UniqueDimensionInspectionEntity.PO_NO, po);
		}
		if(!Strings.isNullOrEmpty(materialNo)) {
			filter.fieldEqualTo(UniqueDimensionInspectionEntity.MATERIAL_NO, materialNo);
		}
		if(!Strings.isNullOrEmpty(materialRev)) {
			filter.fieldEqualTo(UniqueDimensionInspectionEntity.MATERIAL_REV, materialRev);
		}
		filter.orderBy(UniqueDimensionInspectionEntity.SERIAL_SN, false);
		return listByFilter(filter);
	}
	@Override
	public List<UniqueDimensionInspection> getAll(String po) {
		EntityFilter filter = this.createFilter();
		if(!Strings.isNullOrEmpty(po)) {
			filter.fieldEqualTo(UniqueDimensionInspectionEntity.PO_NO, po);
		}
		filter.orderBy(UniqueDimensionInspectionEntity.PO_NO, false);
		return listByFilter(filter);
	}

}
