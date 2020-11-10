package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.UniqueDimensionInspectionEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class UniqueDimensionInspection extends ObjectBaseImpl<UniqueDimensionInspectionEntity>{

	private static final long serialVersionUID = 3595302103761953654L;

	public UniqueDimensionInspection(UniqueDimensionInspectionEntity entity) {
		super(entity);
	}

	public UniqueDimensionInspection() {
		super(null);
	}
	@Override
	public String getName() {
		return null;
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

	public String getMaterialRev() {
		return this.getInternalObject().getMaterialRev();
	}

	public void setMaterialRev(String materialRev) {
		this.getInternalObject().setMaterialRev(materialRev);
	}

	public String getDrawNo() {
		return this.getInternalObject().getDrawNo();
	}

	public void setDrawNo(String drawNo) {
		this.getInternalObject().setDrawNo(drawNo);
	}

	public String getDrawRev() {
		return this.getInternalObject().getDrawRev();
	}

	public void setDrawRev(String drawRev) {
		this.getInternalObject().setDrawRev(drawRev);
	}

	public String getInspectionItem() {
		return this.getInternalObject().getInspectionItem();
	}

	public void setInspectionItem(String inspectionItem) {
		this.getInternalObject().setInspectionItem(inspectionItem);
	}

	public String getInspectionValue() {
		return this.getInternalObject().getInspectionValue();
	}

	public void setInspectionValue(String inspectionValue) {
		this.getInternalObject().setInspectionValue(inspectionValue);
	}

	public String getInspectionResult() {
		return this.getInternalObject().getInspectionResult();
	}

	public void setInspectionResult(String inspectionResult) {
		this.getInternalObject().setInspectionResult(inspectionResult);
	}

	public String getSerialSN() {
		return this.getInternalObject().getSerialSN();
	}

	public void setSerialSN(String serialSN) {
		this.getInternalObject().setSerialSN(serialSN);
	}

	public String getGaugeType() {
		return this.getInternalObject().getGaugeType();
	}

	public void setGaugeType(String gaugeType) {
		this.getInternalObject().setGaugeType(gaugeType);
	}

	public String getGaugeSN() {
		return this.getInternalObject().getGaugeSN();
	}

	public void setGaugeSN(String gaugeSN) {
		this.getInternalObject().setGaugeSN(gaugeSN);
	}
	
}
