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
@Table(name="FUNCTION_DETECTION_RESULT")
public class FunctionDetectionResultEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9011062890663942550L;
	
	public static final String ORDER_NO = "orderNo";//工单
	public static final String SN = "sn";//产品unit
	public static final String GAGE_NO = "gageNO";//量具编号
	public static final String FUNCTION_INSPECTION_RESULT= "functionInspectionResult";//功能检测结果
	
	@Column(name="ORDER_NO",length=80)
	private String orderNo;
	
	@Column(name="SN" ,length=80)
	private String sn;
	
	@Column(name="GAGE_NO",length=80)
	private String gageNO;
	
	@Column(name="FUNCTION_INSPECTION_RESULT" ,length=80)
	private String functionInspectionResult;
}
