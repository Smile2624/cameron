package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.DimensionInspectionResultEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class DimensionInspectionResult extends ObjectBaseImpl<DimensionInspectionResultEntity>{

	private static final long serialVersionUID = 6445967077955366192L;

	public DimensionInspectionResult(DimensionInspectionResultEntity entity) {
		super(entity);
	}
	public DimensionInspectionResult() {
		super(null);
	}
	@Override
	public String getName() {
		return null;
	}

	public String getPurchasingNo(){
		return this.getInternalObject().getPurchasingNo();
	}
	
	public String getOrderItem(){
		return this.getInternalObject().getOrderItem();
	}
	public String getSapInspectionNo(){
		return this.getInternalObject().getSapInspectionNo();
	}
	public String getMaterialNo(){
		return this.getInternalObject().getMaterialNo();
	}
	public String getMaterialRev(){
		return this.getInternalObject().getMaterialRev();
	}
	public String getInspectionValue(){
		return this.getInternalObject().getInspectionValue();
	}
	public boolean getIsPass(){
		return this.getInternalObject().isPass();
	}
	public String getGageInfo(){
		return this.getInternalObject().getGageInfo();
	}
	public String getMaterialSN() {
		return this.getInternalObject().getMaterialSN();
	}
	public String getInspectionName() {
		return getInternalObject().getInspectionName();
	}
	
	public void setPurchasingNo(String purchasingNo) {
		this.getInternalObject().setPurchasingNo(purchasingNo);
	}
	public void setOrderItem(String orderItem) {
		this.getInternalObject().setOrderItem(orderItem);
	}
	public void setSapInspectionNo(String sapInspectionNo) {
		this.getInternalObject().setSapInspectionNo(sapInspectionNo);
	}
	public void setMaterialNo(String materialNo) {
		this.getInternalObject().setMaterialNo(materialNo);
	}
	public void setMaterialRev(String materialRev) {
		this.getInternalObject().setMaterialRev(materialRev);
	}
	public void setInspectionValue(String inspectionValue) {
		this.getInternalObject().setInspectionValue(inspectionValue);
	}
	public void setIsPass(boolean isPass) {
		this.getInternalObject().setPass(isPass);
	}
	public void setGageInfo(String gageInfo) {
		this.getInternalObject().setGageInfo(gageInfo);
	}
	public void setMaterialSN(String materialSN) {
		this.getInternalObject().setMaterialSN(materialSN);
	}
	public void setInspectionName(String inspectionName) {
		this.getInternalObject().setInspectionName(inspectionName);
	}
}
