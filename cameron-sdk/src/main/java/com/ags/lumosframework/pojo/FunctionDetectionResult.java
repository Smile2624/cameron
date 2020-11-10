package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.FunctionDetectionResultEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class FunctionDetectionResult extends ObjectBaseImpl<FunctionDetectionResultEntity>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7876272062729722905L;

	public FunctionDetectionResult(FunctionDetectionResultEntity entity) {
		super(entity);
	}
	
	public FunctionDetectionResult() {
		super(null);
	}
	
	@Override
	public String getName() {
		return null;
	}
	
	public String getOrderNo() {
		return this.getInternalObject().getOrderNo();
	}
	
	public void setOrderNo(String orderNo) {
		this.getInternalObject().setOrderNo(orderNo);
	}
	
	public String getSn() {
		return this.getInternalObject().getSn();
	}
	
	public void setSn(String sn) {
		this.getInternalObject().setSn(sn);
	}
	
	public String getGageNO() {
		return this.getInternalObject().getGageNO();
	}
	
	public void setGageNO(String gageNO) {
		this.getInternalObject().setGageNO(gageNO);
	}
	
	public String getFunctionInspectionResult() {
		return this.getInternalObject().getFunctionInspectionResult();
	}
	
	public void setFunctionInspectionResult(String functionInspectionResult) {
		this.getInternalObject().setFunctionInspectionResult(functionInspectionResult);
	}
	
}
