package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.InspectionValueEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class InspectionValue extends ObjectBaseImpl<InspectionValueEntity>{

	public InspectionValue(InspectionValueEntity entity) {
		super(entity);
	}

	private static final long serialVersionUID = 7034067408297089827L;

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
	public String getProduct_specification() {
		return this.getInternalObject().getProductSpecification();
	}
	public void setProduct_specification(String product_specification) {
		this.getInternalObject().setProductSpecification(product_specification);
	}
	public String getAppearance_desc() {
		return this.getInternalObject().getAppearanceDesc();
	}
	public void setAppearance_desc(String appearance_desc) {
		this.getInternalObject().setAppearanceDesc(appearance_desc);
	}
	public double getMin_value() {
		return this.getInternalObject().getMinValue();
	}
	public void setMin_value(double min_value) {
		this.getInternalObject().setMinValue(min_value);
	}
	public double getMax_value() {
		return this.getInternalObject().getMaxValue();
	}
	public void setMax_value(double max_value) {
		this.getInternalObject().setMaxValue(max_value);
	}

	
	
}
