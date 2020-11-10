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
@Table(name="INSPECTION_TYPE")
public class InspectionTypeEntity extends BaseEntity{

	private static final long serialVersionUID = 5950997093662066587L;

	public static final String INSPECTION_NAME = "inspectionName";
	public static final String INSPECTION_DESC = "inspectionDesc";
	@Column(name="INSPECTION_NAME" , length=255)
	private String inspectionName;
	
	@Column(name="INSPECTION_DESC" , length=1024)
	private String inspectionDesc;
}
