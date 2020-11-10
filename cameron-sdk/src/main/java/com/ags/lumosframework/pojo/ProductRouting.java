package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.ProductRoutingEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class ProductRouting extends ObjectBaseImpl<ProductRoutingEntity> {


    /**
     *
     */
    private static final long serialVersionUID = 3577751721504960580L;

    @Override
    public String getName() {
        return null;
    }

    public ProductRouting() {
        super(null);
    }

    public ProductRouting(ProductRoutingEntity entity) {
        super(entity);
    }


    public String getProductId() {
        return this.getInternalObject().getProductId();
    }

    public void setProductId(String productId) {
        this.getInternalObject().setProductId(productId);
    }

    public String getProductDesc() {
        return this.getInternalObject().getProductDesc();
    }

    public void setProductDesc(String productDesc) {
        this.getInternalObject().setProductDesc(productDesc);
    }

    public String getOprationNo() {
        return this.getInternalObject().getOprationNo();
    }

    public void setOprationNo(String oprationNo) {
        this.getInternalObject().setOprationNo(oprationNo);
    }

    public String getOprationDesc() {
        return this.getInternalObject().getOprationDesc();
    }

    public void setOprationDesc(String oprationDesc) {
        this.getInternalObject().setOprationDesc(oprationDesc);
    }

    public String getRoutingGroup() {
        return this.getInternalObject().getRoutingGroup();
    }

    public void setRoutingGroup(String routingGroup) {
        this.getInternalObject().setRoutingGroup(routingGroup);
    }

    public String getInnerGroupNo() {
        return this.getInternalObject().getInnerGroupNo();
    }

    public void setInnerGroupNo(String innerGroupNo) {
        this.getInternalObject().setInnerGroupNo(innerGroupNo);
    }

    public String getRoutingDesc() {
        return this.getInternalObject().getRoutingDesc();
    }

    public void setRoutingDesc(String routingDesc) {
        this.getInternalObject().setRoutingDesc(routingDesc);
    }

    public String getAttention() {
        return this.getInternalObject().getAttention();
    }

    public void setAttention(String attention) {
        this.getInternalObject().setAttention(attention);
    }

    public int getInnerGroupNoInt() {
        return this.getInternalObject().getInnerGroupNoInt();
    }

    public void setInnerGroupNoInt(int innerGroupNoInt) {
        this.getInternalObject().setInnerGroupNoInt(innerGroupNoInt);
    }

    public String getCheckStatus() {
        return ((ProductRoutingEntity) this.getInternalObject()).getCheckStatus();
    }

    public void setCheckStatus(String checkStatus) {
        ((ProductRoutingEntity) this.getInternalObject()).setCheckStatus(checkStatus);
    }

    public boolean isReconfirmNeeded() {
        return ((ProductRoutingEntity) this.getInternalObject()).isReconfirmNeeded();
    }

    public void setReconfirmNeeded(boolean reconfirmNeeded) {
        ((ProductRoutingEntity) this.getInternalObject()).setReconfirmNeeded(reconfirmNeeded);
    }

}
