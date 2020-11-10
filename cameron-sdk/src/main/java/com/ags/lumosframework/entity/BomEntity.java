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
@Table(name = "BOM")
public class BomEntity extends BaseEntity {

    private static final long serialVersionUID = -1075231968105630452L;

    public static final String PRODUCT_NO = "productNo";//产品号/BOM编号 component number(0)
    public static final String PRODUCT_REV = "productRev";//产品版本号/BOM版本号 Revision Level(0)

    public static final String ITEM_NO = "itemNo";//Item Number /Document Type
    public static final String PART_NO = "partNo";//零件号 component number/document
    public static final String PART_REV = "partRev";//零件版本号 Revision Level/Document version
    public static final String PART_DES = "partDes";//零件描述 Object Description
    public static final String PART_QUANTITY = "partQuantity";//零件数量 Comp. Qty (BUn)
    public static final String IS_RETROSPECT = "isRetrospect";//是否追溯
    public static final String RETROSPECT_TYPE = "retrospectType";//追溯类型
    public static final String LONG_TEXT = "longText";//长文本 Long Text

    public static final String QA_PLAN_REV = "qaPalnRev";//质量计划版本
    public static final String QA_PLAN = "qaPaln";//质量计划版本
    public static final String DRAW_NO = "drawNo";//图纸编号
    public static final String PRESSURE_TEST = "pressureTest";//压力试验程序
    public static final String PRESSURE_TEST_REV = "pressureTestRev";//压力试验程序版本

    //add 2020-07
    public static final String ITEM_CATEGORY = "itemCategory";//Item category
    public static final String EXPLOSION_LEVEL = "explosionLevel";//Explosion level
    public static final String SORT_STRING = "sortString";//Sort String
    public static final String LEGACY_REV = "legacyRev";//Legacy rev
    public static final String BASE_UNIT = "baseUnit";//Base Unit of Measure
    public static final String PHANTOM_ITEM = "phantomItem";//Phantom item

    @Column(name = "PRODUCT_NO", length = 80)
    private String productNo;

    @Column(name = "PRODUCT_REV", length = 80)
    private String productRev;

    @Column(name = "ITEM_NO", length = 80)
    private String itemNo;

    @Column(name = "PART_NO", length = 80)
    private String partNo;

    @Column(name = "PART_REV", length = 80)
    private String partRev;

    @Column(name = "PART_DES", length = 80)
    private String partDes;

    @Column(name = "PART_QUANTITY")
    private int partQuantity;

    @Column(name = "IS_RETROSPECT")
    private boolean isRetrospect;

    @Column(name = "RETROSPECT_TYPE", length = 80)
    private String retrospectType;

    @Column(name = "LONG_TEXT", columnDefinition = "longtext")
    private String longText;

    @Column(name = "QA_PLAN_REV", length = 80)
    private String qaPalnRev;

    @Column(name = "QA_PLAN", length = 80)
    private String qaPaln;

    @Column(name = "DRAW_NO", length = 80)
    private String drawNo;

    @Column(name = "PRESSURE_TEST", length = 80)
    private String pressureTest;

    @Column(name = "PRESSURE_TEST_REV", length = 80)
    private String pressureTestRev;

    @Column(name = "ITEM_CATEGORY", length = 80)
    private String itemCategory;

    @Column(name = "EXPLOSION_LEVEL", length = 80)
    private String explosionLevel;

    @Column(name = "SORT_STRING", length = 80)
    private String sortString;

    @Column(name = "LEGACY_REV", length = 80)
    private String legacyRev;

    @Column(name = "BASE_UNIT", length = 80)
    private String baseUnit;

    @Column(name = "PHANTOM_ITEM", length = 80)
    private String phantomItem;

}
