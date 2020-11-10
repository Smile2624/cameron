package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.OrderHistoryEntity;
import com.ags.lumosframework.entity.ProductRoutingEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

import java.util.Date;

public class OrderHistory extends ObjectBaseImpl<OrderHistoryEntity>{

	private static final long serialVersionUID = 2792164781270400233L;
	public OrderHistory(OrderHistoryEntity entity) {
		super(entity);
	}

	public OrderHistory() {
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
	
	public String getOperationNo() {
		return this.getInternalObject().getOperationNo();
	}
	
	public void setOperationNo(String operationNo) {
		this.getInternalObject().setOperationNo(operationNo);
	}
	
	public String getOperationDesc() {
		return this.getInternalObject().getOperationDesc();
	}
	
	public void setOperationDesc(String operationDesc) {
		this.getInternalObject().setOperationDesc(operationDesc);
	}
	
	public String getOperationAttention() {
		return this.getInternalObject().getOperationAttention();
	}
	
	public void setOperationAttention(String operationAttention) {
		this.getInternalObject().setOperationAttention(operationAttention);
	}
	
	public String getConfirmBy() {
		return this.getInternalObject().getConfirmBy();
	}
	
	public void setConfirmBy(String confirmBy) {
		this.getInternalObject().setConfirmBy(confirmBy);
	}
	
	public String getReConfirmBy() {
		return this.getInternalObject().getReConfirmBy();
	}
	
	public void setReConfirmBy(String reConfirmBy) {
		this.getInternalObject().setReConfirmBy(reConfirmBy);
	}
	
	public Date getConfirmDate() {
		return this.getInternalObject().getConfirmDate();
	}
	
	public void setConfirmDate(Date confirmDate) {
		this.getInternalObject().setConfirmDate(confirmDate);
	}
	
	
	public Date getReConfirmDate() {
		return this.getInternalObject().getReConfirmDate();
	}
	
	public void setReConfirmDate(Date reConfirmDate) {
		this.getInternalObject().setReConfirmDate(reConfirmDate);
	}

	public boolean isReconfirmNeeded() {
		return (this.getInternalObject()).isReconfirmNeeded();
	}

	public void setReconfirmNeeded(boolean reconfirmNeeded) {
		(this.getInternalObject()).setReconfirmNeeded(reconfirmNeeded);
	}
	public boolean getDeleteFlag() {
		return this.getInternalObject().isDeleteFlag();
	}

	public void setDeleteFlag(boolean deleteFlag) {
		this.getInternalObject().setDeleteFlag(deleteFlag);
	}

}
