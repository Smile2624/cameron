package com.ags.lumosframework.entity;

import com.ags.lumosframework.sdk.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 保存供应商零件信息
 */
@Setter
@Getter
@Entity
@Table(name = "VENDOR_MATERIAL_INFO")
public class VendorMaterialEntity extends BaseEntity {
    public static final String MATERIAL_NO="materialNo";
    public static final String MATERIAL_REV="materialRev";
    public static final String MATERIAL_DESC="materialDesc";

    @Column(name = "MATERIAL_NO" ,length = 80)
    private String materialNo;

    @Column(name = "MATERIAL_REV" ,length = 25)
    private String materialRev;

    @Column(name = "MATERIAL_DESC" ,length = 255)
    private String materialDesc;

}
