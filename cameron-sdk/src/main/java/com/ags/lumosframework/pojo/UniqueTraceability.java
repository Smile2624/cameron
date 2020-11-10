package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.UniqueTraceabilityEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class UniqueTraceability extends ObjectBaseImpl<UniqueTraceabilityEntity>{

	private static final long serialVersionUID = 1065728200811690025L;

	public UniqueTraceability(UniqueTraceabilityEntity entity) {
		super(entity);
	}
	
	public UniqueTraceability() {
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

	public String getMaterialDesc() {
		return this.getInternalObject().getMaterialDesc();
	}

	public void setMaterialDesc(String materialDesc) {
		this.getInternalObject().setMaterialDesc(materialDesc);
	}

	public String getCustNo() {
		return this.getInternalObject().getCustNo();
	}

	public void setCustNo(String custNo) {
		this.getInternalObject().setCustNo(custNo);
	}

	public String getHlLotNo() {
		return this.getInternalObject().getHlLotNo();
	}

	public void setHlLotNo(String hlLotNo) {
		this.getInternalObject().setHlLotNo(hlLotNo);
	}

	public String getPurchasingNo() {
		return this.getInternalObject().getPurchasingNo();
	}

	public void setPurchasingNo(String purchasingNo) {
		this.getInternalObject().setPurchasingNo(purchasingNo);
	}

	public int getQuantity() {
		return this.getInternalObject().getQuantity();
	}

	public void setQuantity(int quantity) {
		this.getInternalObject().setQuantity(quantity);
	}

	public String getIfQCN() {
		return this.getInternalObject().getIfQCN();
	}

	public void setIfQCN(String ifQCN) {
		this.getInternalObject().setIfQCN(ifQCN);
	}

	public String getComment() {
		return this.getInternalObject().getComment();
	}

	public void setComment(String comment) {
		this.getInternalObject().setComment(comment);
	}

	public String getWarehouseConfirmer() {
		return this.getInternalObject().getWarehouseConfirmer();
	}

	public void setWarehouseConfirmer(String warehouseConfirmer) {
		this.getInternalObject().setWarehouseConfirmer(warehouseConfirmer);
	}

	public String getQaConfirmer() {
		return this.getInternalObject().getQaConfirmer();
	}

	public void setQaConfirmer(String qaConfirmer) {
		this.getInternalObject().setQaConfirmer(qaConfirmer);
	}

	public String getQcConfirmer() {
		return this.getInternalObject().getQcConfirmer();
	}

	public void setQcConfirmer(String qcConfirmer) {
		this.getInternalObject().setQcConfirmer(qcConfirmer);
	}

	public String getHeatNo() {
		return this.getInternalObject().getHeatNo();
	}

	public void setHeatNo(String heatNo) {
		this.getInternalObject().setHeatNo(heatNo);
	}

	public String getWhDate() {
		return this.getInternalObject().getWhDate();
	}

	public void setWhDate(String whDate) {
		this.getInternalObject().setWhDate(whDate);
	}
	
	public String getQaDate() {
		return this.getInternalObject().getQaDate();
	}

	public void setQaDate(String qaDate) {
		this.getInternalObject().setQaDate(qaDate);
	}
	
	public String getQcDate() {
		return this.getInternalObject().getQcDate();
	}

	public void setQcDate(String qcDate) {
		this.getInternalObject().setQcDate(qcDate);
	}
	
	public String getCustOrder() {
		return this.getInternalObject().getCustOrder();
	}
	
	public void setCustOrder(String custOrder) {
		this.getInternalObject().setCustOrder(custOrder);
	}
	
	public String getPurchasingOrder() {
		return this.getInternalObject().getPurchasingOrder();
	}
	
	public void setPurchasingOrder(String purchasingOrder) {
		this.getInternalObject().setPurchasingOrder(purchasingOrder);
	}
}
