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
@Table(name="UNIQUE_DIMENSION_RESULT")
/**
 * 
 * @author william_huang
 *保存UNIQUE每个采购单的检验结果，每个采购单只有一条记录
 */
public class UniqueDimensionInspectionResultEntity extends BaseEntity{

	private static final long serialVersionUID = 171488910107244856L;

	public static final String INSPECTOR ="inspector";//检验员
	public static final String INSPECT_DATE="inspectDate";//检验日期
	public static final String INSPECT_QUANTITY="inspectionQuantity";//数量
	public static final String ACTIVE_QUANTITY = "activeQuantity";//数量
	public static final String PO_NO = "poNo";//订单号
	public static final String MATERIAL_NO="materialNo";//零件号
	public static final String MATERIAL_REV = "materialRev";//两件版本
	public static final String APPEARANCE_INS = "appearanceIns";//外观检验
	public static final String VENDOR = "vendor";//外观检验
	public static final String PASS = "pass";//是否通过检验
	
	@Column(name="INSPECTOR",length=80)
	private String inspector;
	
	@Column(name="INSPECT_DATE",length=80)
	private String inspectDate;
	
	@Column(name="INSPECT_QUANTITY")
	private int inspectionQuantity;
	
	@Column(name="ACTIVE_QUANTITY")
	private int activeQuantity;
	
	@Column(name="PO_NO",length=80)
	private String poNo;
	
	@Column(name="MATERIAL_NO",length=80)
	private String materialNo;
	
	@Column(name="MATERIAL_REV",length=80)
	private String materialRev;
	
	@Column(name="APPEARANCE_INS",length=80)
	private String appearanceIns;
	
	@Column(name="VENDOR",length=80)
	private String vendor;
	
	@Column(name="PASS")
	private boolean pass;
}
