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
@Table(name = "DIMENSION_INSPECTION_RESULT")
public class DimensionInspectionResultEntity extends BaseEntity{

	private static final long serialVersionUID = -4652626955382042671L;
	
	public static final String PURCHASING_NO ="purchasingNo";//采购单号
	public static final String ORDER_ITEM = "orderItem";//订单子项
	public static final String SAP_INSPECTION_NO = "sapInspectionNo";//sap抽检号
	public static final String MATRRIAL_NO="materialNo";//零件号
	public static final String MATERIAL_REV = "materialRev";//零件版本
	public static final String INSPECTION_VALUE = "inspectionValue";//实际检验值
	public static final String IS_PASS = "isPass";//结果是否符合标准
	public static final String GAGE_INFO = "gageInfo";//量具信息
	public static final String MATERIAL_SN ="materialSN";//零件SN
	//add
	public static final String INSPECTION_NAME = "inspectionName";//检测项名称
	@Column(name="PURCHASING_NO",length=80)
	private String purchasingNo;
	
	@Column(name="ORDER_ITEM",length=80)
	private String orderItem;
	
	@Column(name="SAP_INSPECTION_NO",length=80)
	private String sapInspectionNo;
	
	@Column(name="MATRRIAL_NO",length=80)
	private String materialNo;
	
	@Column(name="MATERIAL_REV",length=80)
	private String materialRev;
	
	@Column(name="INSPECTION_VALUE",length=80)
	private String inspectionValue;
	
	@Column(name="IS_PASS")
	private boolean isPass;
	
	@Column(name="GAGE_INFO",length=80)
	private String gageInfo;
	
	@Column(name="MATERIAL_SN",length=80)
	private String materialSN;
	
	@Column(name="INSPECTION_NAME",length=255)
	private String inspectionName;
}
