package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.VendorMaterialEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;
import com.ags.lumosframework.sdk.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 保存供应商零件信息
 */

public class VendorMaterial extends ObjectBaseImpl<VendorMaterialEntity> {


    public VendorMaterial(VendorMaterialEntity entity) {
        super(entity);
    }

    public VendorMaterial(){
        super(null);
    }

    @Override
    public String getName() {
        return null;
    }

    //--------------------
    public String getMaterialNo(){
        return this.getInternalObject().getMaterialNo();
    }

    public void setMaterialNo(String materialNo){
        this.getInternalObject().setMaterialNo(materialNo);
    }

    public String getMaterialRev(){
        return this.getInternalObject().getMaterialRev();
    }

    public void setMaterialRev(String materialRev){
        this.getInternalObject().setMaterialRev(materialRev);
    }

    public String getMaterialDesc(){
        return this.getInternalObject().getMaterialDesc();
    }

    public void setMaterialDesc(String materialDesc){
        this.getInternalObject().setMaterialDesc(materialDesc);
    }
}
