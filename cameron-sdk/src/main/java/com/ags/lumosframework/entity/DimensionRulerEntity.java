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
@Table(name = "DIMENSION_RULER")
public class DimensionRulerEntity extends BaseEntity{

	private static final long serialVersionUID = 4251220034983314431L;

	public static final String MATRRIAL_NO="materialNo";//零件号
	public static final String MATERIAL_REV = "materialRev";//零件版本
	public static final String INSPECTION_ITEM_NAME = "inspectionItemName";//检验项名称
	public static final String INSPECTION_ITEM_TYPE = "inspectionItemType";//检验项值类型
	public static final String MAX_VALUE = "maxValue";
	public static final String MIN_VALUE = "minValue";
	
	@Column(name="MATRRIAL_NO",length=255)
	private String materialNo;
	
	@Column(name="MATERIAL_REV",length=80)
	private String materialRev;
	
	@Column(name="INSPECTION_ITEM_NAME",length=255)
	private String inspectionItemName;
	
	@Column(name="INSPECTION_ITEM_TYPE",length=255)
	private String inspectionItemType;
	
	@Column(name="MAX_VALUE")
	private double maxValue;
	
	@Column(name="MIN_VALUE")
	private double minValue;
}
