package com.ags.lumosframework.pojo;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.AssemblingEntity;
import com.ags.lumosframework.entity.IssueMaterialListEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;
import com.ags.lumosframework.service.IProductionOrderService;

public class IssueMaterialList extends ObjectBaseImpl<IssueMaterialListEntity> {
    private static final long serialVersionUID = 284077604483655047L;

    public IssueMaterialList() {
        super(null);
    }

    public IssueMaterialList(IssueMaterialListEntity entity) {
        super(entity);
    }

    @Override
    public String getName() {
        return null;
    }

   public String getOrderNo(){
        return this.getInternalObject().getOrderNo();
   }
   public void setOrderNo(String orderNo){
        this.getInternalObject().setOrderNo(orderNo);
   }

    public String getMaterialNo(){
        return this.getInternalObject().getMaterialNo();
    }
    public void setMaterialNo(String materialNo){
        this.getInternalObject().setMaterialNo(materialNo);
    }

    public String getMaterialDesc(){
        return this.getInternalObject().getMaterialDesc();
    }
    public void setMaterialDesc(String materialDesc){
        this.getInternalObject().setMaterialDesc(materialDesc);
    }

    public double getRequirementQuantity(){
        return this.getInternalObject().getRequirementQuantity();
    }
    public void setRequirementQuantity(double requirementQuantity){
        this.getInternalObject().setRequirementQuantity(requirementQuantity);
    }

    public double getQuantityWithdrawn(){
        return this.getInternalObject().getQuantityWithdrawn();
    }
    public void setQuantityWithdrawn(double quantityWithdrawn){
        this.getInternalObject().setQuantityWithdrawn(quantityWithdrawn);
    }

    public double getShortage(){
        return this.getInternalObject().getShortage();
    }
    public void setShortage(double quantityWithdrawn){
        this.getInternalObject().setShortage(quantityWithdrawn);
    }

    public String getProdStorage(){
        return this.getInternalObject().getProdStorage();
    }
    public void setProdStorage(String prodStorage){
        this.getInternalObject().setProdStorage(prodStorage);
    }

    public String getStatus(){return this.getInternalObject().getStatus();}
    public void setStatus(String status){this.getInternalObject().setStatus(status);}
}
