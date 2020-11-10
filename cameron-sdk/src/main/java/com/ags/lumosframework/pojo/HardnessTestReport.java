package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.HardnessTestReportEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class HardnessTestReport extends ObjectBaseImpl<HardnessTestReportEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4330150837589793253L;
	
	public HardnessTestReport() {
		super(null);
	}
	
	public HardnessTestReport(HardnessTestReportEntity entity) {
		super(entity);
	}

	@Override
	public String getName() {
		return null;
	}
	
	public String getPurchaseOrder() {
        return getInternalObject().getPurchaseOrder();
    }

    public void setPurchaseOrder(String purchaseOrder) {
        getInternalObject().setPurchaseOrder(purchaseOrder);
    }

    public String getSAPBatchNo() {
        return getInternalObject().getSAPBatchNo();
    }

    public void setSAPBatchNo(String SAPBatchNo) {
        getInternalObject().setSAPBatchNo(SAPBatchNo);
    }

    public String getPurchaseOrderSubitem() {
        return getInternalObject().getPurchaseOrderSubitem();
    }

    public void setPurchaseOrderSubitem(String purchaseOrderSubitem) {
        getInternalObject().setPurchaseOrderSubitem(purchaseOrderSubitem);
    }
    
    public int getPartQuantity() {
        return getInternalObject().getPartQuantity();
    }

    public void setPartQuantity(int partQuantity) {
        getInternalObject().setPartQuantity(partQuantity);
    }

    public String getSerialNumbers() {
        return getInternalObject().getSerialNumbers();
    }

    public void setSerialNumbers(String serialNumbers) {
        getInternalObject().setSerialNumbers(serialNumbers);
    }
    
    public int getQuantityAccepted() {
        return getInternalObject().getQuantityAccepted();
    }

    public void setQuantityAccepted(int quantityAccepted) {
        getInternalObject().setQuantityAccepted(quantityAccepted);
    }

    public int getQuantityRejected() {
        return getInternalObject().getQuantityRejected();
    }

    public void setQuantityRejected(int quantityRejected) {
        getInternalObject().setQuantityRejected(quantityRejected);
    }

    public boolean getTestedFinished() {
        return getInternalObject().isTestedFinished();
    }

    public void setTestedFinished(boolean testedFinished) {
        getInternalObject().setTestedFinished(testedFinished);
    }
    
    public String getTemp() {
        return getInternalObject().getTemp();
    }

    public void setTemp(String temp) {
        getInternalObject().setTemp(temp);
    }
    
    public String getMaterialMS() {
        return getInternalObject().getMaterialMS();
    }

    public void setMaterialMS(String materialMS) {
        getInternalObject().setMaterialMS(materialMS);
    }

}
