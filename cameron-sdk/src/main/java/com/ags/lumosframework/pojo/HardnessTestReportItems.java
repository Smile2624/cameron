package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.HardnessTestReportItemsEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class HardnessTestReportItems extends ObjectBaseImpl<HardnessTestReportItemsEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1621376957211370941L;
	
	public HardnessTestReportItems() {
		super(null);
	}

	public HardnessTestReportItems(HardnessTestReportItemsEntity entity) {
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

    public String getPartNo() {
        return getInternalObject().getPartNo();
    }

    public void setPartNo(String partNo) {
        getInternalObject().setPartNo(partNo);
    }
    
    public String getPartNoRev() {
        return getInternalObject().getPartNoRev();
    }

    public void setPartNoRev(String partNoRev) {
        getInternalObject().setPartNoRev(partNoRev);
    }

//    public float getTemp() {
//        return getInternalObject().getTemp();
//    }
//
//    public void setTemp(float temp) {
//        getInternalObject().setTemp(temp);
//    }

    public String getSerialNo() {
        return getInternalObject().getSerialNo();
    }

    public void setSerialNo(String serialNo) {
        getInternalObject().setSerialNo(serialNo);
    }
    
    public String getHeatNo() {
        return getInternalObject().getHeatNo();
    }

    public void setHeatNo(String heatNo) {
        getInternalObject().setHeatNo(heatNo);
    }
    
    public String getHTLotNo() {
        return getInternalObject().getHTLotNo();
    }

    public void setHTLotNo(String HTLotNo) {
        getInternalObject().setHTLotNo(HTLotNo);
    }

    public float getActualHardnessValue() {
        return getInternalObject().getActualHardnessValue();
    }

    public void setActualHardnessValue(float actualHardnessValue) {
        getInternalObject().setActualHardnessValue(actualHardnessValue);
    }

    public String getResult() {
        return getInternalObject().getResult();
    }

    public void setResult(String result) {
        getInternalObject().setResult(result);
    }
	
}
