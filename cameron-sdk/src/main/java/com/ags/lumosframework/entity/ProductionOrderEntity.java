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
@Table(name = "PRODUCT_ORDER")
public class ProductionOrderEntity extends BaseEntity {

    private static final long serialVersionUID = -72796946363095876L;

    public static final String PRODUCT_ORDER_ID = "productOrderId";
    public static final String PRODUCT_ID = "productId";
    public static final String PRODUCT_VERSION_ID = "productVersionId";
    public static final String PRODUCT_DESC = "productDesc";
    public static final String PRODUCT_QTY = "productQty";
    public static final String ROUTING_GROUP = "routingGroup";
    public static final String INNER_GROUP_NO = "innerGroupNo";
    //add
    public static final String CUSTOMER_CODE = "customerCode";//客户
    public static final String SALES_ORDER = "salesOrder";//销售订单号
    public static final String SALES_ORDER_ITEM = "salesOrderItem";//销售订单项目号
    public static final String PAINT_SPECIFICATION = "paintSpecification";//喷漆标准
    public static final String COMMENTS = "comments";
    public static final String WORKSHOP = "workshop";
    //add superior order
    public static final String SUPERIOR_ORDER = "superiorOrder";//母工单

    public static final String NEWEST_BOM_VERSION = "newestBomVersion";//最新bom版本号
    public static final String BOM_CHECK_FLAG = "bomCheckFlag";//是否需要审核 flase
    public static final String BOM_LOCK_FLAG = "bomLockFlag";//是否锁定 flase

    public static final String PULL_MAT_FLAG = "pullMatFlag";//是否发料

    @Column(name = "PRODUCT_ORDER_ID", length = 80)
    private String productOrderId;

    @Column(name = "PRODUCT_ID", length = 80)
    private String productId;

    @Column(name = "PRODUCT_VERSION_ID", length = 80)
    private String productVersionId;

    @Column(name = "PRODUCT_DESC", length = 80)
    private String productDesc;

    @Column(name = "PRODUCT_QTY")
    private int productQty;

    @Column(name = "ROUTING_GROUP", length = 80)
    private String routingGroup;

    @Column(name = "INNER_GROUP_NO", length = 80)
    private String innerGroupNo;

    @Column(name = "CUSTOMER_CODE", length = 255)
    private String customerCode;

    @Column(name = "SALES_ORDER", length = 255)
    private String salesOrder;

    @Column(name = "SALES_ORDER_ITEM", length = 255)
    private String salesOrderItem;

    @Column(name = "PAINT_SPECIFICATION", length = 255)
    private String paintSpecification;

    @Column(name = "COMMENTS", length = 1024)
    private String comments;

    @Column(name = "WORKSHOP", length = 80)
    private String workshop;

    @Column(name = "SUPERIOR_ORDER", length = 80)
    private String superiorOrder;


    @Column(name = "NEWEST_BOM_VERSION", length = 80)
    private String newestBomVersion;

    @Column(name = "PULL_MAT_FLAG")
    private boolean pullMatFlag;

    @Column(name = "BOM_CHECK_FLAG")
    private boolean bomCheckFlag;

    @Column(name = "BOM_LOCK_FLAG")
    private boolean bomLockFlag;


}
