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
@Table(name="PAINTING_SPECIFICATION")
public class PaintingSpecificationEntity extends BaseEntity {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String PAINTING_SPECIFICATION_FILE ="paintingSpecificationFile";
	public static final String PRIMER = "primer";
	public static final String INTERMEDIATE = "intermediate";
	public static final String FINALS = "finals";
	public static final String COLOR = "color";
	public static final String HUMIDITY = "humidity";
	public static final String AVOVE_DEW_POINT = "aboveDewPoint";

	public static final String REV = "rev";//Changed by Cameron: 喷漆标准版本
	public static final String TOTAL = "total";//Changed by Cameron: 总干膜厚
	
	
	@Column(name="PAINTING_SPECIFICATION_FILE" , length=1024)
	private String paintingSpecificationFile;
	
	@Column(name="PRIMER" , length=80)
	private String primer;
	
	@Column(name="INTERMEDIATE" , length=80)
	private String intermediate;
	
	@Column(name="FINALS" ,length=80)
	private String finals;
	
	@Column(name="COLOR" , length=80)
	private String color;
	
	@Column(name="HUMIDITY" , length=80)
	private String humidity;
	
	@Column(name="AVOVE_DEW_POINT" , length=80)
	private String aboveDewPoint;

	@Column(name="REV" , length=80)
	private String rev;

	@Column(name="TOTAL" , length=80)
	private String total;
}
