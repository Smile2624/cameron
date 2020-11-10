package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.InspectionValueEntity;
import com.ags.lumosframework.handler.IInspectionValueHandler;
import com.ags.lumosframework.pojo.InspectionValue;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IInspectionValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class InspectionValueService extends AbstractBaseDomainObjectService<InspectionValue, InspectionValueEntity> implements IInspectionValueService{

	@Autowired
	private IInspectionValueHandler inspectionValueHandler;
	
	@Override
	protected IBaseEntityHandler<InspectionValueEntity> getEntityHandler() {
		return inspectionValueHandler;
	}

	@Override
	public InspectionValue getByNameAndSpecification(String name, String Specification) {
		EntityFilter filter = this.createFilter();
		if(name != null && !"".equals(name)) {
			filter.fieldEqualTo(InspectionValueEntity.INSPECTION_NAME, name);
		}
		if(Specification != null && !"".equals(Specification)) {
			filter.fieldEqualTo(InspectionValueEntity.PRODUCT_SPECIFICATION, Specification);
		}
		return this.getByFilter(filter);
	}

	@Override
	public List<InspectionValue> ListByName(String name) {
		EntityFilter filter = this.createFilter();
		if(name != null && !"".equals(name)) {
			filter.fieldEqualTo(InspectionValueEntity.INSPECTION_NAME, name);
		}
		return listByFilter(filter);
	}

}
