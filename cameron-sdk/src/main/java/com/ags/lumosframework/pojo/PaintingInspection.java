package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.PaintingInspectionEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class PaintingInspection extends ObjectBaseImpl<PaintingInspectionEntity> {
	
	private static final long serialVersionUID = -8810304216371769594L;

	public PaintingInspection() {
		super(null);
	}
	
	public PaintingInspection(PaintingInspectionEntity entity) {
		super(entity);
	}

	@Override
	public String getName() {
		return null;
	}
	
	public String getWorkOrderSN() {
		return this.getInternalObject().getWorkOrderSN();
	}
	public void setWorkOrderSN(String workOrderSN) {
		this.getInternalObject().setWorkOrderSN(workOrderSN);
	}
	
	public String getResult() {
		return this.getInternalObject().getResult();
	}
	public void setResult(String result) {
		this.getInternalObject().setResult(result);
	}
}
