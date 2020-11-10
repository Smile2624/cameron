package com.ags.lumosframework.entity;

import com.ags.lumosframework.sdk.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Setter
@Getter
@Entity
@Table(name="RECEIVING_INSPECTION_REPORT")
public class ReceivingInspectionEntity extends BaseEntity {

	private static final long serialVersionUID = -2993212127917374206L;

	public static final String PURCHASING_ORDER="purchasingOrder";
	public static final String PURCHASING_ORDER_ITEM="purchasingOrderItem";
	public static final String SAP_INSPECTION_NO="sapInspectionNo";
	
	public static final String VISUAL_RESULT="visualResult";
	public static final String HARDNESS_RESULT="hardnessResult";
	public static final String DIMENSION_RESULT="dimensionResult";
	public static final String TRACEABILITY_RESULT="traceabilityResult";
	public static final String DOCUMENT_RESULT="documentResult";
	public static final String PACKING_RESULT="packingResult";
	public static final String CERTIFICATION_RESULT="certificationResult";
	public static final String OTHERS_RESULT="otherResult";
	public static final String COMMENT="comment";
	public static final String QC_CONFIRM_DATE= "qcConfirmDate";
	public static final String QA_CONFIRM_DATE= "qaConfirmDate";
	public static final String QC_CHECKER= "qcChecker";
	public static final String QA_CHECKER= "qaChecker";
	public static final String API_SPEC = "apiSpec";
	public static final String PSL = "psl"; 
	public static final String SN = "sn"; 
	
	@Column(name="PURCHASING_ORDER",length=80)
	private String purchasingOrder;
	
	@Column(name="PURCHASING_ORDER_ITEM",length=80)
	private String purchasingOrderItem;
	
	@Column(name="SAP_INSPECTION_NO",length=80)
	private String sapInspectionNo;
	
	@Column(name="VISUAL_RESULT",length=80)
	private String visualResult;
	
	@Column(name="HARDNESS_RESULT",length=80)
	private String hardnessResult;
	
	@Column(name="DIMENSION_RESULT",length=80)
	private String dimensionResult;
	
	@Column(name="TRACEABILITY_RESULT",length=80)
	private String traceabilityResult;
	
	@Column(name="DOCUMENT_RESULT",length=80)
	private String documentResult;
	
	@Column(name="PACKING_RESULT",length=80)
	private String packingResult;
	
	@Column(name="CERTIFICATION_RESULT",length=80)
	private String certificationResult;
	
	@Column(name="OTHERS_RESULT",length=80)
	private String otherResult;
	
	@Column(name="COMMENT",length=255)
	private String comment;
	
	@Column(name="QC_CONFIRM_DATE",length=80)
	private String qcConfirmDate;
	
	@Column(name="QA_CONFIRM_DATE",length=80)
	private String qaConfirmDate;
	
	@Column(name="API_SPEC",length=80)
	private String apiSpec;
	
	@Column(name="PSL",length=80)
	private String psl;
	
	@Column(name="QC_CHECKER",length=80)
	private String qcChecker;
	@Column(name="QA_CHECKER",length=80)
	private String qaChecker;
	@Column(name="SN",length=80)
	private String sn;
}
