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
@Table(name="APPEARANCE_INSTRUMENTATION_RESULT")
public class AppearanceInstrumentationResultEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7292661629171897882L;
	
	public static final String ORDER_NO = "orderNo";//工单
	public static final String SN = "sn";//产品unit
	public static final String PAINTING_THICKNESS_RESULT = "paintingThicknessResult";//喷漆厚度测量结果
	public static final String VISUAL_EXAMINATION_DESC= "visualExaminationDesc";//外观检测说明
	public static final  String GAGEINFO = "gageinfo";
	
	@Column(name="ORDER_NO",length=80)
	private String orderNo;
	
	@Column(name="SN" ,length=80)
	private String sn;
	
	@Column(name="PAINTING_THICKNESS_RESULT")
	private float paintingThicknessResult;
	
	@Column(name="VISUAL_EXAMINATION_DESC" ,length=255)
	private String visualExaminationDesc;

	@Column(name="GAGEINFO",length = 80)
	private String gageinfo;
	
}
