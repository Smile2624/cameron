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
@Table(name = "HARDNESS_TEST_REPORT")
public class HardnessTestReportEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5671810236630523676L;
	
	public static final String PURCHASE_ORDER = "purchaseOrder";//采购单号
    public static final String SAP_BATCH_NO = "SAPBatchNo";//sap检验批次号
    public static final String PURCHASE_ORDER_SUBITEM = "purchaseOrderSubitem";//采购订单子项
    public static final String PART_QUANTITY = "partQuantity";//零件数目
    public static final String SERIAL_NUMBERS = "serialNumbers";//序列号范围
    public static final String QUANTITY_ACCEPTED = "quantityAccepted";//接受数量
    public static final String QUANTITY_REJECTED = "quantityRejected";//拒绝数量
    public static final String TESTED_FINISHED = "testedFinished";//测试是否完成
    public static final String TEMP = "temp";//试验温蒂
    public static final String MATERIAL_MS = "materialMS";//材料规范

    @Column(name = "PURCHASE_ORDER", length = 80)
    private String purchaseOrder;

    @Column(name = "SAP_BATCH_NO", length = 80)
    private String SAPBatchNo;

    @Column(name = "PURCHASE_ORDER_SUBITEM",length = 80)
    private String purchaseOrderSubitem;
    
    @Column(name = "PART_QUANTITY")
    private int partQuantity;

    @Column(name = "SERIAL_NUMBERS",length = 255)
    private String serialNumbers;
    
    @Column(name = "QUANTITY_ACCEPTED")
    private int quantityAccepted;

    @Column(name = "QUANTITY_REJECTED")
    private int quantityRejected;

    @Column(name = "TESTED_FINISHED")
    private boolean testedFinished;
    
    @Column(name = "TEMP",length=80)
    private String temp;

    @Column(name = "MATERIAL_MS",length=80)
    private String materialMS;
}
