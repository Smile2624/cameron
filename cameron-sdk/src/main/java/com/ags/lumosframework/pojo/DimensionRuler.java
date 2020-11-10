package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.DimensionRulerEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class DimensionRuler extends ObjectBaseImpl<DimensionRulerEntity>{

	private static final long serialVersionUID = 4516000005443768119L;
	
	public DimensionRuler(DimensionRulerEntity entity) {
		super(entity);
	}
	
	public DimensionRuler() {
		super(null);
	}

	@Override
	public String getName() {
		return null;
	}
	
	public String getMaterialNo() {
		return this.getInternalObject().getMaterialNo();
	}
	
	public void setMaterialNo(String materialNo) {
		this.getInternalObject().setMaterialNo(materialNo);
	}
	
	public String getMaterialRev() {
		return this.getInternalObject().getMaterialRev();
	}
	
	public void setMaterialRev(String materialRev) {
		this.getInternalObject().setMaterialRev(materialRev);
	}
	
	public String getInspectionItemName() {
		return this.getInternalObject().getInspectionItemName();
	}
	
	public void setInspectionItemName(String inspectionItemName) {
		this.getInternalObject().setInspectionItemName(inspectionItemName);
	}
	
	public String getInspectionItemType() {
		return this.getInternalObject().getInspectionItemType();
	}
	
	public void setInspectionItemType(String inspectionItemType) {
		this.getInternalObject().setInspectionItemType(inspectionItemType);
	}
	
	public double getMaxValue() {
		return this.getInternalObject().getMaxValue();
	}
	
	public void setMaxValue(double maxValue) {
		this.getInternalObject().setMaxValue(maxValue);
	}
	
	public double getMinValue() {
		return this.getInternalObject().getMinValue();
	}
	
	public void setMinValue(double minValue) {
		this.getInternalObject().setMinValue(minValue);
	}
}
