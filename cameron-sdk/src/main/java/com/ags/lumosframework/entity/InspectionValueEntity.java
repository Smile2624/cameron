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
@Table(name="INSPECTION_VALUE")
public class InspectionValueEntity extends BaseEntity{

	private static final long serialVersionUID = 1L;
	
	public static final String INSPECTION_NAME ="inspectionName";
	public static final String PRODUCT_SPECIFICATION = "productSpecification";
	public static final String MIN_VALUE = "minValue";
	public static final String MAX_VALUE = "maxValue";
	public static final String APPEARANCE_DESC = "appearanceDesc";
	
	
	@Column(name="INSPECTION_NAME" , length=255)
	private String inspectionName;
	
	@Column(name="PRODUCT_SPECIFICATION" , length=255)
	private String productSpecification;
	
	@Column(name="MIN_VALUE")
	private double minValue;
	
	@Column(name="MAX_VALUE")
	private double maxValue;
	
	@Column(name="APPEARANCE_DESC" ,length=1024)
	private String appearanceDesc;
}
