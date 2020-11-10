package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.SparePartInspectionRulerEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class SparePartInspectionRuler extends ObjectBaseImpl<SparePartInspectionRulerEntity> {
    public SparePartInspectionRuler(SparePartInspectionRulerEntity entity) {
        super(entity);
    }

    @Override
    public String getName() {
        return null;
    }

    public String getPartNo(){
       return this.getInternalObject().getPartNo();
    }
    public void setPartNo(String partNo){
        this.getInternalObject().setPartNo(partNo);
    }

    public String getPartRev(){
        return this.getInternalObject().getPartRev();
    }

    public void setPartRev(String partRev){
        this.getInternalObject().setPartRev(partRev);
    }

    public String getItemName(){
        return this.getInternalObject().getItemName();
    }

    public void setItemName(String itemName){
        this.getInternalObject().setItemName(itemName);
    }

    public double getMaxValue(){
        return this.getInternalObject().getMaxValue();
    }

    public void setMaxValue(double maxValue){
        this.getInternalObject().setMaxValue(maxValue);
    }

    public double getMinValue(){
        return this.getInternalObject().getMinValue();
    }

    public void setMinValue(double minValue){
        this.getInternalObject().setMinValue(minValue);
    }

    public String getCommentValue(){
        return this.getInternalObject().getCommentValue();
    }

    public void setCommentValue(String commentValue){
        this.getInternalObject().setCommentValue(commentValue);
    }
}
