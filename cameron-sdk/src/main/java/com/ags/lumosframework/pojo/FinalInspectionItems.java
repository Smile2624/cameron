package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.FinalInspectionItemsEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class FinalInspectionItems extends ObjectBaseImpl<FinalInspectionItemsEntity>{

	private static final long serialVersionUID = 516778446759143045L;

	
	public FinalInspectionItems(FinalInspectionItemsEntity entity) {
		super(entity);
	}

	public FinalInspectionItems() {
		super(null);
	}
	
	@Override
	public String getName() {
		return null;
	}

	public String getInspectionItemName() {
		return this.getInternalObject().getInspectionItemName();
	}
	
	public void setInspectionItemName(String inspectionItemName) {
		this.getInternalObject().setInspectionItemName(inspectionItemName);
	}
	
	public String getDefaultResult() {
		return this.getInternalObject().getDefaultResult();
	}
	
	public void setDefaultResult(String defaultResult) {
		this.getInternalObject().setDefaultResult(defaultResult);
	}
}
