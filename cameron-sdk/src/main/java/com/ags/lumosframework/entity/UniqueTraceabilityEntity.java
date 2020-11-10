package com.ags.lumosframework.entity;

import com.ags.lumosframework.sdk.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * 
 * @author william_huang
 * 记录UNIQUE来料的追溯信息
 */

@Getter
@Setter
@Entity
@Table(name = "UNIQUE_TRACEABILITY_RECORD")
public class UniqueTraceabilityEntity extends BaseEntity{

	private static final long serialVersionUID = -5851580940593521007L;

	public static final String MATERIAL_NO = "materialNo";
	public static final String MATERIAL_REV = "materialRev";
	public static final String MATERIAL_DESC = "materialDesc";
	public static final String HL_LOT_NO ="hlLotNo";
	public static final String HEAT_NO ="heatNo";
	public static final String CUST_PO ="custNo";//客户采购单号+序列号+sn
	public static final String PURCHASING_NO ="purchasingNo";//po+item+sn
	public static final String QUANTITY ="quantity";
	public static final String IF_QCN ="ifQCN";
	public static final String COMMENT ="comment";
	public static final String WAREHOUSE_CONFIRMER ="warehouseConfirmer";
	public static final String QA_CONFIRMER ="qaConfirmer";
	public static final String QC_CONFIRMER ="qcConfirmer";
	public static final String WH_DATE = "whDate";
	public static final String QA_DATE = "qaDate";
	public static final String QC_DATE = "qcDate";
	public static final String CUST_ORDER = "custOrder";//客户内部工单号
	public static final String PURCHASING_ORDER = "purchasingOrder";//po
	
	@Column(name="MATERIAL_NO",length=80)
	private String materialNo;
	
	@Column(name="MATERIAL_REV",length=80)
	private String materialRev;
	
	@Column(name="MATERIAL_DESC",length=255)
	private String materialDesc;
	
	@Column(name="CUST_PO",length=80)
	private String custNo;
	
	@Column(name="HL_LOT_NO",length=80)
	private String hlLotNo;
	
	@Column(name="PURCHASING_NO",length=80)
	private String purchasingNo;
	
	@Column(name="QUANTITY")
	private int quantity;
	
	@Column(name="IF_QCN",length=80)
	private String ifQCN;
	
	@Column(name="COMMENT",length=255)
	private String comment;
	
	@Column(name="WAREHOUSE_CONFIRMER",length=80)
	private String warehouseConfirmer;
	
	@Column(name="QA_CONFIRMER",length=80)
	private String qaConfirmer;
	
	@Column(name="QC_CONFIRMER",length=80)
	private String qcConfirmer;
	
	@Column(name="HEAT_NO",length=80)
	private String heatNo;
	
	@Column(name="WH_DATE",length=80)
	private String whDate;
	
	@Column(name="QA_DATE",length=80)
	private String qaDate;
	
	@Column(name="QC_DATE",length=80)
	private String qcDate;
	
	@Column(name="CUST_ORDER",length=80)
	private String custOrder;
	
	@Column(name="PURCHASING_ORDER",length=80)
	private String purchasingOrder;
}
