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
@Table(name = "UNIQUE_DIMENSION_INSPECTION_ITEMS")
/**
 * 
 * @author william_huang
 *保存Unique来料的尺寸检验的每一个SN的检验项的检验结果
 */
public class UniqueDimensionInspectionEntity extends BaseEntity {

	private static final long serialVersionUID = 8389045961543946437L;

	public static final String PO_NO = "poNo";
	public static final String MATERIAL_NO = "materialNo";
	public static final String MATERIAL_REV = "materialRev";
	public static final String SERIAL_SN = "serialSN";
	public static final String DRAW_NO = "drawNo";
	public static final String DRAW_REV = "drawRev";
	public static final String INSPECTION_ITEM="inspectionItem";
	public static final String INSPECTION_VALUE="inspectionValue";
	public static final String INSPECTION_RESULT="inspectionResult";
	public static final String GAUGE_TYPE="gaugeType";
	public static final String GAUGE_SN = "gaugeSN";
	
	@Column(name="PO_NO",length=80)
	private String poNo;
	
	@Column(name="MATERIAL_NO",length = 80)
	private String materialNo;
	
	@Column(name="SERIAL_SN" ,length=80)
	private String serialSN;
	
	@Column(name="MATERIAL_REV" ,length=80)
	private String materialRev;
	
	@Column(name="DRAW_NO",length=80)
	private String drawNo;
	
	@Column(name="DRAW_REV",length=80)
	private String drawRev;
	
	@Column(name="INSPECTION_ITEM",length=80)
	private String inspectionItem;
	
	@Column(name="INSPECTION_VALUE",length=80)
	private String inspectionValue;
	
	@Column(name="INSPECTION_RESULT",length =80)
	private String inspectionResult;
	
	@Column(name="GAUGE_TYPE",length=80)
	private String gaugeType;
	
	@Column(name="GAUGE_SN",length=80)
	private String gaugeSN;
}
