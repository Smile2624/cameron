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
@Table(name="PURCHASING_ORDER")
public class PurchasingOrderInfoEntity extends BaseEntity{

	private static final long serialVersionUID = -985876495420240920L;
	
	public static final String SAP_INSPECTION_LOT="sapInspectionLot";
	public static final String MATERIAL_NO = "materialNo";
	public static final String MATERIAL_QUANTITY = "materialQuantity";
	public static final String MATERIAL_UNIT = "materialUnit";
	public static final String MATERIAL_DESC = "materialDesc";
	public static final String MATERIAL_REV = "materialRev";
	public static final String PURCHASING_NO = "purchasingNo";
	public static final String PURCHASING_ITEM_NO = "purchasingItemNo";
	public static final String VENDOR_NAME = "vendorName";
	
	//add
	public static final String DIMENSION_CHECKED = "dimensionChecked";//当前订单项是否已经进行尺寸检验
	public static final String HARDNESS_CHECKED = "hardnessChecked";//当前订单项是否已经进行硬度检验
	public static final String VISUAL_CHECKED = "visualChecked";
	public static final String DIMENSION_CHECKED_RLT = "dimensionCheckedRlt";//尺寸加盐结果
	public static final String HARDNESS_CHECKED_RLT = "hardnessCheckedRlt";//硬度检验结果
	public static final String VISUAL_CHECKED_RLT = "visualCheckedRlt";
	public static final String INSPECTION_QUANTITY = "inspectionQuantity";//设置检验数量，默认0
	public static final String CHECKED_SN = "checkedSn";
	
	@Column(name="SAP_INSPECTION_LOT",length=255)
	private String sapInspectionLot;
	
	@Column(name="MATERIAL_NO" ,length=255)
	private String materialNo;
	
	@Column(name="MATERIAL_QUANTITY" )
	private int materialQuantity;
	
	@Column(name="MATERIAL_UNIT" ,length=255)
	private String materialUnit;
	
	@Column(name="MATERIAL_DESC" ,length=1024)
	private String materialDesc;
	
	@Column(name="MATERIAL_REV" ,length=80)
	private String materialRev;
	
	@Column(name="PURCHASING_NO" ,length=255)
	private String purchasingNo;
	
	@Column(name="PURCHASING_ITEM_NO" ,length=255)
	private String purchasingItemNo;
	
	@Column(name="VENDOR_NAME" ,length=1024)
	private String vendorName;

	@Column(name="DIMENSION_CHECKED")
	private boolean dimensionChecked;
	
	@Column(name="HARDNESS_CHECKED")
	private boolean hardnessChecked;

	@Column(name="VISUAL_CHECKED")
	private boolean visualChecked;
	
	@Column(name="DIMENSION_CHECKED_RLT")
	private String dimensionCheckedRlt;
	
	@Column(name="HARDNESS_CHECKED_RLT")
	private String hardnessCheckedRlt;

	@Column(name="VISUAL_CHECKED_RLT")
	private String visualCheckedRlt;
	
	@Column(name="INSPECTION_QUANTITY")
	private int inspectionQuantity;
	
	@Column(name="CHECKED_SN",length=500)
	private String checkedSn;
}
