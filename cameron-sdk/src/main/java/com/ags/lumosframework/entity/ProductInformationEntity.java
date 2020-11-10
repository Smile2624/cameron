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
@Table(name="PRODUCT_INFORMATION")
public class ProductInformationEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String PRODUCT_ID ="productId";
	public static final String PRODUCT_VERSION_ID = "productVersionId";
	public static final String PRODUCT_DESC = "productDesc";
	public static final String TEMPERATURE_RATING = "temperatureRating";
	public static final String MATERIAL_RATING = "materialRating";
	public static final String PSL_RATING = "PSLRating";
	public static final String PRESSURE_INSPECTION_PROCEDURE = "pressureInspectionProcedure";
	public static final String PRESSURE_INSPECTION_PROCEDURE_VERSION = "pressureInspectionProcedureVersion";
	public static final String PAINTING_SPECIFICATION_FILE = "paintingSpecificationFile";
	public static final String PAINTING_SPECIFICATION_FILE_REV = "paintingSpecificationFileRev";
	public static final String QULITY_PLAN = "qulityPlan";
	public static final String QULITY_PLAN_REV = "qulityPlanRev";
	public static final String IS_REVIEWED = "isReviewed";
	public static final String GAS_TEST = "gasTest";
	public static final String GAS_TEST_REV = "gasTestRev";
	public static final String BLOWDOWN_TORQUE = "blowdownTorque";
	public static final String LEVP = "levp";
	public static final String LEVP_REV = "levpRev";
	
	@Column(name="PRODUCT_ID" , length=80)
	private String productId;
	
	@Column(name="PRODUCT_VERSION_ID" , length=80)
	private String productVersionId;
	
	@Column(name="PRODUCT_DESC" ,length=1024)
	private String productDesc;
	
	@Column(name="TEMPERATURE_RATING" , length=80)
	private String temperatureRating;
	
	@Column(name="MATERIAL_RATING" , length=80)
	private String materialRating;
	
	@Column(name="PSL_RATING" , length=80)
	private String PSLRating;
	
	@Column(name="PRESSURE_INSPECTION_PROCEDURE" , length=80)
	private String pressureInspectionProcedure;
	
	@Column(name="PRESSURE_INSPECTION_PROCEDURE_VERSION" , length=80)
	private String pressureInspectionProcedureVersion;

	@Column(name="GAS_TEST" , length=80)
	private String gasTest;

	@Column(name="GAS_TEST_REV" , length=80)
	private String gasTestRev;
	
	@Column(name="PAINTING_SPECIFICATION_FILE" , length=1024)
	private String paintingSpecificationFile;
	
	@Column(name="PAINTING_SPECIFICATION_FILE_REV" , length=80)
	private String paintingSpecificationFileRev;
	
	@Column(name="QULITY_PLAN",length=255)
	private String qulityPlan;
	
	@Column(name="QULITY_PLAN_REV",length=80)
	private String qulityPlanRev;

	@Column(name="BLOWDOWN_TORQUE",length=80)
	private String blowdownTorque;

	@Column(name="LEVP" , length=80)
	private String levp;

	@Column(name="LEVP_REV" , length=80)
	private String levpRev;

	@Column(name="IS_REVIEWED")
	private boolean isReviewed;
}
