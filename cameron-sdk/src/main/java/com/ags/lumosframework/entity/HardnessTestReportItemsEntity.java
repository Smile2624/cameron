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
@Table(name = "HARDNESS_TEST_REPORT_ITEMS")
public class HardnessTestReportItemsEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1821935380881790427L;
	
	public static final String PURCHASE_ORDER = "purchaseOrder";//采购单号
    public static final String SAP_BATCH_NO = "SAPBatchNo";//sap检验批次号
    public static final String PURCHASE_ORDER_SUBITEM = "purchaseOrderSubitem";//采购订单子项
    public static final String PART_NO = "partNo";//零件号
    public static final String PART_NO_REV = "partNoRev";//零件版本
//    public static final String TEMP = "temp";//试验温蒂
    public static final String SERIAL_NO = "serialNo";//序列号
    public static final String HEAT_NO = "heatNo";//熔炼炉号
    public static final String HT_LOT_NO = "HTLotNo";//热处理炉号
    public static final String ACTUAL_HARDNESS_VALUE = "actualHardnessValue";//实际硬度值
    public static final String RESULT = "result";//测试结果

    @Column(name = "PURCHASE_ORDER", length = 80)
    private String purchaseOrder;

    @Column(name = "SAP_BATCH_NO", length = 80)
    private String SAPBatchNo;

    @Column(name = "PURCHASE_ORDER_SUBITEM",length = 80)
    private String purchaseOrderSubitem;

    @Column(name = "PART_NO",length = 80)
    private String partNo;
    
    @Column(name = "PART_NO_REV",length = 80)
    private String partNoRev;

//    @Column(name = "TEMP")
//    private float temp;

    @Column(name = "SERIAL_NO",length = 80)
    private String serialNo;
    
    @Column(name = "HEAT_NO",length = 80)
    private String heatNo;
    
    @Column(name = "HT_LOT_NO",length = 80)
    private String HTLotNo;

    @Column(name = "ACTUAL_HARDNESS_VALUE")
    private float actualHardnessValue;

    @Column(name = "RESULT",length = 80)
    private String result;
}
