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
@Table(name = "BOM_NAME_AUTHORITY")
public class BomNameAuthorityEntity extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6676423255233003948L;
	
	public static final String AUTHORITY_TYPE = "authorityType";//规范类型
	public static final String NAME_EIGENVALUE = "nameEigenvalue";//名称特征值
	
	//
	@Column(name="AUTHORITY_TYPE",length=80)
	private String authorityType;
	
	@Column(name="NAME_EIGENVALUE" ,length=80)
	private String nameEigenvalue;
}
