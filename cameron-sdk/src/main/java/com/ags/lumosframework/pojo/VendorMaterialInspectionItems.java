package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.VendorMaterialInspectionItemsEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class VendorMaterialInspectionItems extends ObjectBaseImpl<VendorMaterialInspectionItemsEntity> {
    public VendorMaterialInspectionItems(VendorMaterialInspectionItemsEntity entity) {
        super(entity);
    }

    public VendorMaterialInspectionItems() {
        super(null);
    }

    @Override
    public String getName() {
        return null;
    }


    public Long getMaterialId(){
        return this.getInternalObject().getMaterialId();
    }

    public void setMaterialId(long materialId){
        this.getInternalObject().setMaterialId(materialId);
    }

    public String getInspectionItemName(){
        return this.getInternalObject().getInspectionItemName();
    }

    public void setInspectionItemName(String inspectionItemName){
        this.getInternalObject().setInspectionItemName(inspectionItemName);
    }

    public String getInspectionItemType(){
        return this.getInternalObject().getInspectionItemType();
    }

    public void setInspectionItemType(String inspectionItemType){
        this.getInternalObject().setInspectionItemType(inspectionItemType);
    }

    public double getMaxValue(){
        return this.getInternalObject().getInspectionItemMaxValue();
    }

    public void setMaxValue(double maxValue){
        this.getInternalObject().setInspectionItemMaxValue(maxValue);
    }

    public double getMinValue(){
        return this.getInternalObject().getInspectionItemMinValue();
    }

    public void setMinValue(double minValue){
        this.getInternalObject().setInspectionItemMinValue(minValue);
    }

    public double getStandardValue(){
        return this.getInternalObject().getInspectionItemStandardValue();
    }

    public void setStandardValue(double standardValue){
        this.getInternalObject().setInspectionItemStandardValue(standardValue);
    }

    public String getValue(){
        return this.getInternalObject().getInspectionItemValue();
    }

    public void setValue(String standardValue){
        this.getInternalObject().setInspectionItemValue(standardValue);
    }


}

