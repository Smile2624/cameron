package com.ags.lumosframework.entity;

import com.ags.lumosframework.sdk.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "CERTIFICATE_CONFORMANCE")
public class CertificateOfConformanceEntity extends BaseEntity {
	
	private static final long serialVersionUID = -3685918458277914291L;
	
	public static final String ORDER_NO = "orderNo";//工单号
	public static final String LOGO = "logo";//模板类型 有无会标
	public static final String CERTIFICATE_NUMBER = "certificateNumber";//合格证号
    public static final String CUSTOMER = "customer";//客户
    public static final String CUSTOMER_PO_NUMBER = "customerPONumber";//客户订单号
    public static final String DATE = "date";//日期
    public static final String SALES_ORDER_NUMBER = "salesOrderNumber";//销售订单号
    public static final String INTERNAL_TRACKING_NUMBER = "internalTrackingNumber";//内部跟踪号
    public static final String ITEM = "item";//项目
    public static final String NOTES = "notes";//备注
    public static final String LICENSE_NUMBER = "licenseNumber";//会标编号
    public static final String QUALITY_REPRESENTATIVE = "qualityRepresentative";//质量代表
    
    @Column(name = "ORDER_NO", length = 80)
    private String orderNo;
    
    @Column(name = "LOGO", length = 80)
    private String logo;
    
    @Column(name = "CERTIFICATE_NUMBER", length = 80)
    private String certificateNumber;

    @Column(name = "CUSTOMER", length = 80)
    private String customer;

    @Column(name = "CUSTOMER_PO_NUMBER", length = 80)
    private String customerPONumber;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "SALES_ORDER_NUMBER", length = 80)
    private String salesOrderNumber;

    @Column(name = "INTERNAL_TRACKING_NUMBER", length = 80)
    private String internalTrackingNumber;

    @Column(name = "ITEM", length = 80)
    private String item;

    @Column(name = "NOTES", length = 80)
    private String notes;

    @Column(name = "LICENSE_NUMBER", length = 80)
    private String licenseNumber;

    @Column(name = "QUALITY_REPRESENTATIVE", length = 80)
    private String qualityRepresentative;

    
}
