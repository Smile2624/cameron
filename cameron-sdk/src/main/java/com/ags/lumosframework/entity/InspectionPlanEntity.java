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
@Table(name="INSPECTION_PLAN")
public class InspectionPlanEntity extends BaseEntity {

    public static final String PRODUCT_NO="productNo";
    public static final String PRODUCT_DESC ="productDesc";
    public static final String INS_PLAN = "insPlan";
    public static final String Q_CODE="qCode";
    public static final String CHEMICAL_ANALYSIS="chemicalAnalysis";
    public static final String PRE_HEAT_DIMENSION="preHeatDimension";
    public static final String FORG_HEAT_CONTROL="forgHeatControl";
    public static final String MECHANICAL_TEST="mechanicalTest";
    public static final String VOLUM_NDE="volumNde";
    public static final String TRACE_MARK="traceMark";
    public static final String SUR_NDE="surNde";
    public static final String PART_HARDNESS="partHardness";
    public static final String VISUAL_EXAM="visualExam";
    public static final String WELD_OVERLAY="weldOverlay";
    public static final String WELD_PREP_NDE="weldPrepNde";
    public static final String FINAL_NDE="finalNde";
    public static final String DIMENSION_INSPECT="dimensionInpection";
    public static final String COAT_PAINT="coatPaint";
    public static final String COC_ELASTOMER="cocElastomer";
    public static final String COC_CAMERON="cocCameron";

    @Column(name = "PRODUCT_NO",length = 80)
    private String productNo;

    @Column(name = "PRODUCT_DESC",length = 80)
    private String productDesc;

    @Column(name = "INS_PLAN",length = 80)
    private String insPlan;

    @Column(name = "Q_CODE",length = 80)
    private String qCode;

    @Column(name = "CHEMICAL_ANALYSIS",length = 80)
    private String chemicalAnalysis;

    @Column(name = "PRE_HEAT_DIMENSION",length = 80)
    private String preHeatDimension;

    @Column(name = "FORG_HEAT_CONTROL",length = 80)
    private String forgHeatControl;

    @Column(name = "MECHANICAL_TEST",length = 80)
    private String mechanicalTest;

    @Column(name = "VOLUM_NDE",length = 80)
    private String volumNde;

    @Column(name = "TRACE_MARK",length = 80)
    private String traceMark;

    @Column(name = "SUR_NDE",length = 80)
    private String surNde;

    @Column(name = "PART_HARDNESS",length = 80)
    private String partHardness;

    @Column(name = "VISUAL_EXAM",length = 80)
    private String visualExam;

    @Column(name = "WELD_OVERLAY",length = 80)
    private String weldOverlay;

    @Column(name = "WELD_PREP_NDE",length = 80)
    private String weldPrepNde;

    @Column(name = "FINAL_NDE",length = 80)
    private String finalNde;

    @Column(name = "DIMENSION_INSPECT",length = 80)
    private String dimensionInpection;

    @Column(name = "COAT_PAINT",length = 80)
    private String coatPaint;

    @Column(name = "COC_ELASTOMER",length = 80)
    private String cocElastomer;

    @Column(name = "COC_CAMERON",length = 80)
    private String cocCameron;
}
