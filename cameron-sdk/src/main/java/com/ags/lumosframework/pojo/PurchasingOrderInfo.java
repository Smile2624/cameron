package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.PurchasingOrderInfoEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class PurchasingOrderInfo extends ObjectBaseImpl<PurchasingOrderInfoEntity>{

	private static final long serialVersionUID = 5263623930137198613L;
	
	public PurchasingOrderInfo(PurchasingOrderInfoEntity entity) {
		super(entity);
	}
	public PurchasingOrderInfo() {
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
	public int getMaterialQuantity() {
		return this.getInternalObject().getMaterialQuantity();
	}
	
	public void setMaterialQuantity(int materialQuantity) {
		this.getInternalObject().setMaterialQuantity(materialQuantity);
	}
	public String getMaterialUnit() {
		return this.getInternalObject().getMaterialUnit();
	}
	
	public void setMaterialUnit(String materialUnit) {
		this.getInternalObject().setMaterialUnit(materialUnit);
	}
	public String getMaterialRev() {
		return this.getInternalObject().getMaterialRev();
	}
	
	public void setMaterialRev(String materialRev) {
		this.getInternalObject().setMaterialRev(materialRev);;
	}
	public String getPurchasingNo() {
		return this.getInternalObject().getPurchasingNo();
	}
	
	public void setPurchasingNo(String purchasingNo) {
		this.getInternalObject().setPurchasingNo(purchasingNo);
	}
	public String getPurchasingItemNo() {
		return this.getInternalObject().getPurchasingItemNo();
	}
	
	public void setPurchasingItemNo(String purchasingItemNo) {
		this.getInternalObject().setPurchasingItemNo(purchasingItemNo);
	}
	public String getVendorName() {
		return this.getInternalObject().getVendorName();
	}
	
	public void setVendorName(String vendorName) {
		this.getInternalObject().setVendorName(vendorName);
	}
	public String getMaterialDesc() {
		return this.getInternalObject().getMaterialDesc();
	}
	
	public void setMaterialDesc(String materialDesc) {
		this.getInternalObject().setMaterialDesc(materialDesc);
	}
	
	public String getSapInspectionLot() {
		return this.getInternalObject().getSapInspectionLot();
	}
	
	public void setSapInspectionLot(String sapInspectionLot) {
		this.getInternalObject().setSapInspectionLot(sapInspectionLot);
	}
	
	public boolean getDimensionChecked() {
		return this.getInternalObject().isDimensionChecked();
	}
	
	public void setDimensionChecked(boolean dimensionChecked) {
		this.getInternalObject().setDimensionChecked(dimensionChecked);
	}
	
	public boolean getHardnessChecked() {
		return this.getInternalObject().isHardnessChecked();
	}
	
	public void setHardnessChecked(boolean hardnessChecked) {
		this.getInternalObject().setHardnessChecked(hardnessChecked);
	}
	
	public String getDimensionCheckedRlt(){
		return this.getInternalObject().getDimensionCheckedRlt();
	}
	public void setDimensionCheckedRlt(String dimensionCheckedRlt) {
		this.getInternalObject().setDimensionCheckedRlt(dimensionCheckedRlt);
	}
	
	public String getHardnessCheckedRlt() {
		return this.getInternalObject().getHardnessCheckedRlt();
	}
	
	public void setHardnessCheckedRlt(String hardnessCheckedRlt) {
		this.getInternalObject().setHardnessCheckedRlt(hardnessCheckedRlt);
	}
	
	public int getInspectionQuantity() {
		return this.getInternalObject().getInspectionQuantity();
	}
	
	public void setInspectionQuantity(int inspectionQuantity) {
		this.getInternalObject().setInspectionQuantity(inspectionQuantity);
	}
	
	public String getCheckedSn() {
		return this.getInternalObject().getCheckedSn();
	}
	
	public void setCheckedSn(String checkedSn) {
		this.getInternalObject().setCheckedSn(checkedSn);
	}

	public boolean isVisualChecked() {
		return this.getInternalObject().isVisualChecked();
	}

	public void setVisualChecked(boolean visualChecked) {
		this.getInternalObject().setVisualChecked(visualChecked);
	}

	public String getVisualCheckedRlt(){
		return this.getInternalObject().getVisualCheckedRlt();
	}
	public void setVisualCheckedRlt(String visualCheckedRlt) {
		this.getInternalObject().setVisualCheckedRlt(visualCheckedRlt);
	}
}
