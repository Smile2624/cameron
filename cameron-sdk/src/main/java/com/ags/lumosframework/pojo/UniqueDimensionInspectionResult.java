package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.UniqueDimensionInspectionResultEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class UniqueDimensionInspectionResult extends ObjectBaseImpl<UniqueDimensionInspectionResultEntity>{

	private static final long serialVersionUID = -2130788763554124836L;

	public UniqueDimensionInspectionResult(UniqueDimensionInspectionResultEntity entity) {
		super(entity);
	}

	public UniqueDimensionInspectionResult( ) {
		super(null);
	}

	@Override
	public String getName() {
		return null;
	}

	public String getInspector() {
		return this.getInternalObject().getInspector();
	}

	public void setInspector(String inspector) {
		this.getInternalObject().setInspector(inspector);
	}

	public String getInspectDate() {
		return this.getInternalObject().getInspector();
	}

	public void setInspectDate(String inspectDate) {
		this.getInternalObject().setInspectDate(inspectDate);
	}

	public int getInspectionQuantity() {
		return this.getInternalObject().getInspectionQuantity();
	}

	public void setInspectionQuantity(int inspectionQuantity) {
		this.getInternalObject().setInspectionQuantity(inspectionQuantity);
	}

	public int getActiveQuantity() {
		return this.getInternalObject().getActiveQuantity();
	}

	public void setActiveQuantity(int activeQuantity) {
		this.getInternalObject().setActiveQuantity(activeQuantity);
	}

	public String getMaterialRev() {
		return this.getInternalObject().getMaterialRev();
	}

	public void setMaterialRev(String materialRev) {
		this.getInternalObject().setMaterialRev(materialRev);
	}

	public String getPoNo() {
		return this.getInternalObject().getPoNo();
	}

	public void setPoNo(String poNo) {
		this.getInternalObject().setPoNo(poNo);
	}

	public String getMaterialNo() {
		return this.getInternalObject().getMaterialNo();
	}

	public void setMaterialNo(String materialNo) {
		this.getInternalObject().setMaterialNo(materialNo);
	}

	public String getAppearanceIns() {
		return this.getInternalObject().getAppearanceIns();
	}

	public void setAppearanceIns(String appearanceIns) {
		this.getInternalObject().setAppearanceIns(appearanceIns);
	}
	
	public String getVendor() {
		return this.getInternalObject().getVendor();
	}
	
	public void setVendor(String vendor) {
		this.getInternalObject().setVendor(vendor);
	}
	
	public boolean getPass() {
		return this.getInternalObject().isPass();
	}
	
	public void setPass(boolean pass) {
		this.getInternalObject().setPass(pass);
	}
}
