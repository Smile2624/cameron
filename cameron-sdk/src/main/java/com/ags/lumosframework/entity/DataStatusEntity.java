package com.ags.lumosframework.entity;

import com.ags.lumosframework.sdk.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name="DATA_STATUS")
public class DataStatusEntity extends BaseEntity {
    public static final String PRODUCT_NO="productNo";
    public static final String IS_BOM_CHECKED ="isBomChecked";
    public static final String IS_RTG_CHECKED = "isRtgChecked";
    public static final String IS_ALL_CHECKED="isAllChecked";

    @Column(name = "PRODUCT_NO" , length = 80)
    private String productNo;

    @Column(name= "IS_BOM_CHECKED")
    private boolean isBomChecked;

    @Column(name = "IS_RTG_CHECKED")
    private boolean isRtgChecked;

    @Column(name = "IS_ALL_CHECKED")
    private boolean isAllChecked;
}
