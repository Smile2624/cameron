package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.DimensionInspectionDialogResultEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class DimensionInspectionDialogResult extends ObjectBaseImpl<DimensionInspectionDialogResultEntity> {
	
	private static final long serialVersionUID = 630495634657190545L;
	
	public DimensionInspectionDialogResult() {
		super(null);
	}
	
	public DimensionInspectionDialogResult(DimensionInspectionDialogResultEntity entity) {
		super(entity);
	}

	@Override
	public String getName() {
		return null;
	}
	
	public String getOrderSn() {
        return getInternalObject().getOrderSn();
    }
    public void setOrderSn(String Ordersn) {
        getInternalObject().setOrderSn(Ordersn);
    }
    
    public String getSn() {
        return getInternalObject().getSn();
    }
    public void setSn(String sn) {
        getInternalObject().setSn(sn);
    }
	
	public boolean getIsPass() {
        return getInternalObject().isPass();
    }
    public void setIsPass(boolean isPass) {
        getInternalObject().setPass(isPass);
    }
    
    public String getResult() {
        return getInternalObject().getResult();
    }
    public void setResult(String result) {
        getInternalObject().setResult(result);
    }
    
    public String getInspectionItem() {
    	return this.getInternalObject().getInspectionItem();
    }
    
    public void setInspectionItem(String inspectionItem) {
    	this.getInternalObject().setInspectionItem(inspectionItem);
    }
}
