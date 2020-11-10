package com.ags.lumosframework.entity;

import com.ags.lumosframework.sdk.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 保存供应商零件检验项的信息 INSPECTION_ITEM_NAME + INSPECTION_ITEM_TYPE
 * 1.当检验项的标准为阀阈值时，需要输入最大值和最小值I NSPECTION_ITEM_MAX_VALUE + INSPECTION_ITEM_MIN_VALUE
 * 2.当检验项的标准为标准值时，需要输入标准值--数字 INSPECTION_ITEM_STANDARD_VALUE
 * 3.当检验项的标准为其他信息，需要输入描述性文字或者关键字等信息 INSPECTION_ITEM_VALUE
 */
@Setter
@Getter
@Entity
@Table(name = "VENDOR_MATERIAL_INSPECTION_ITEMS")
public class VendorMaterialInspectionItemsEntity extends BaseEntity {
    public static final String MATERIAL_ID ="materialId";//保存零件的ID，查询时做关联
    public static final String INSPECTION_ITEM_NAME="inspectionItemName";
    public static final String INSPECTION_ITEM_TYPE="inspectionItemType";
    public static final String INSPECTION_ITEM_MAX_VALUE="inspectionItemMaxValue";
    public static final String INSPECTION_ITEM_MIN_VALUE="inspectionItemMinValue";
    public static final String INSPECTION_ITEM_STANDARD_VALUE="inspectionItemStandardValue";
    public static final String INSPECTION_ITEM_VALUE="inspectionItemValue";

    @Column(name = "MATERIAL_ID" ,length = 30)
    private long materialId;

    @Column(name="INSPECTION_ITEM_NAME",length = 80)
    private String inspectionItemName;

    @Column(name="INSPECTION_ITEM_TYPE",length = 80)
    private String inspectionItemType;

    @Column(name="INSPECTION_ITEM_MAX_VALUE")
    private double inspectionItemMaxValue;

    @Column(name="INSPECTION_ITEM_MIN_VALUE")
    private double inspectionItemMinValue;

    @Column(name="INSPECTION_ITEM_STANDARD_VALUE")
    private double inspectionItemStandardValue;

    @Column(name="INSPECTION_ITEM_VALUE",length = 1024)
    private String inspectionItemValue;


}
