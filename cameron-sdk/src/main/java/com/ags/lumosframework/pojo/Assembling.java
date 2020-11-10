package com.ags.lumosframework.pojo;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.AssemblingEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;
import com.ags.lumosframework.service.IProductionOrderService;

public class Assembling extends ObjectBaseImpl<AssemblingEntity> {
    private static final long serialVersionUID = 284077604483655047L;

    public Assembling() {
        super(null);
    }

    public Assembling(AssemblingEntity entity) {
        super(entity);
    }

    @Override
    public String getName() {
        return null;
    }

    public String getSnBatch() {
        return getInternalObject().getSnBatch();
    }

    public void setSnBatch(String snBatch) {
        getInternalObject().setSnBatch(snBatch);
    }

    public String getOrderNo() {
        return getInternalObject().getOrderNo();
    }

    public void setOrderNo(String orderNo) {
        getInternalObject().setOrderNo(orderNo);
    }

    public String getPartNo() {
        return getInternalObject().getPartNo();
    }

    public void setPartNo(String partNo) {
        getInternalObject().setPartNo(partNo);
    }

    public String getPartDesc() {
        return getInternalObject().getPartDesc();
    }

    public void setPartDesc(String partDesc) {
        getInternalObject().setPartDesc(partDesc);
    }

    public String getPlmRev() {
        return getInternalObject().getPlmRev();
    }

    public void setPlmRev(String plmRev) {
        getInternalObject().setPlmRev(plmRev);
    }

    public String getPartRev() {
        return getInternalObject().getPartRev();
    }

    public void setPartRev(String partRev) {
        getInternalObject().setPartRev(partRev);
    }

    public String getSerialNo() {
        return getInternalObject().getSerialNo();
    }

    public void setSerialNo(String serialNo) {
        getInternalObject().setSerialNo(serialNo);
    }

    public int getBatchQty() {
        return getInternalObject().getBatchQty();
    }

    public void setBatchQty(int batchQty) {
        getInternalObject().setBatchQty(batchQty);
    }

    public String getBatch() {
        return getInternalObject().getBatch();
    }

    public void setBatch(String batch) {
        getInternalObject().setBatch(batch);
    }

    public String getHeatNoLot() {
        return getInternalObject().getHeatNoLot();
    }

    public void setHeatNoLot(String heatNoLot) {
        getInternalObject().setHeatNoLot(heatNoLot);
    }

    public String getHardness() {
        return getInternalObject().getHardness();
    }

    public void setHardness(String hardness) {
        getInternalObject().setHardness(hardness);
    }

    public String getMatType() {
        return getInternalObject().getMatType();
    }

    public void setMatType(String matType) {
        getInternalObject().setMatType(matType);
    }

    public String getCOrD() {
        return getInternalObject().getCOrD();
    }

    public void setCOrD(String cOrD) {
        getInternalObject().setCOrD(cOrD);
    }

    public String getEOrD() {
        return getInternalObject().getEOrD();
    }

    public void setEOrD(String eOrD) {
        getInternalObject().setEOrD(eOrD);
    }

    public String getQcCheck() {
        return getInternalObject().getQcCheck();
    }

    public void setQcCheck(String qcCheck) {
        getInternalObject().setQcCheck(qcCheck);
    }

    public String getRetrospectType() {
        return getInternalObject().getRetrospectType();
    }

    public void setRetrospectType(String retrospectType) {
        getInternalObject().setRetrospectType(retrospectType);
    }

    //根据订单号获取生产订单
    public ProductionOrder getProductionOrder() {
        IProductionOrderService orderService = BeanManager.getService(IProductionOrderService.class);

        return orderService.getByNo(getOrderNo());
    }

}
