package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.InspectionTypeEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class InspectionType extends ObjectBaseImpl<InspectionTypeEntity>{


	private static final long serialVersionUID = 8179856757517671517L;

	public InspectionType(InspectionTypeEntity entity) {
		super(entity);
	}
	
	@Override
	public String getName() {
		return null;
	}

	public String getInspection_name() {
		return this.getInternalObject().getInspectionName();
	}
	
	public void setInspection_name(String inspection_name) {
		this.getInternalObject().setInspectionName(inspection_name);
	}
	
	public String getInspection_desc() {
		return this.getInternalObject().getInspectionDesc();
	}
	
	public void setInspection_desc(String inspection_desc) {
		this.getInternalObject().setInspectionDesc(inspection_desc);
	}
}
