package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.InspectionTypeEntity;
import com.ags.lumosframework.handler.IInspectionTypeHandler;
import com.ags.lumosframework.pojo.InspectionType;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IInspectionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class InspectionTypeService extends AbstractBaseDomainObjectService<InspectionType, InspectionTypeEntity> implements IInspectionTypeService{

	@Autowired
	IInspectionTypeHandler inspectionTypeHandler;
	
	@Override
	protected IBaseEntityHandler<InspectionTypeEntity> getEntityHandler() {
		return inspectionTypeHandler;
	}

	@Override
	public InspectionType getByName(String name) {
		EntityFilter filter = this.createFilter();
		filter.fieldEqualTo(InspectionTypeEntity.INSPECTION_NAME, name);
		return   this.getByFilter(filter);
	}

	@Override
	public List<String> getAllInspectionType() {
		List<String> listRlt = new ArrayList<>();
		EntityFilter filter = this.createFilter();
		filter.isNotNull(InspectionTypeEntity.INSPECTION_NAME, true);
		List<InspectionType> list = this.listByFilter(filter);
		for(InspectionType inspectionType : list) {
			String inspectionName  = inspectionType.getInspection_name();
			listRlt.add(inspectionName);
		}
		return listRlt;
	}
}
