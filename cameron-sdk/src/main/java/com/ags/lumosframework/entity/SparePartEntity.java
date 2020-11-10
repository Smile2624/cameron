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
@Table(name = "SPARE_PART")
public class SparePartEntity extends BaseEntity {

    private static final long serialVersionUID = -5087499710688654722L;

    public static final String SPARE_PART_NO = "sPartNo";//零件号
    public static final String SPARE_PART_REV = "sPartRev";//零件版本号
    public static final String SPARE_PART_DEC = "sPartDec";//零件描述
    public static final String QA_PLAN_REV = "qaPalnRev";//质量计划版本
    public static final String QA_PLAN = "qaPaln";//质量计划版本
    public static final String DRAW_NO = "drawNo";//图纸编号
    public static final String PSL_LEVEL_STAND = "pslLevelStand";//PSL产品等级规范
    public static final String API_STAND = "apiStand";//API标准
    public static final String HARDNESS_FILE = "hardnessFile";//硬度文件名
    public static final String PLM_REV = "plmRev";//PLM Rev
    public static final String PART_REV = "partRev";//Part Rev
    public static final String LONG_TEXT = "longText";////追溯类型
    //bruce 添加追溯类型到零件维护界面
    public static final String IS_RETROSPECT = "isRetrospect";//是否追溯
    public static final String RETROSPECT_TYPE = "retrospectType";//追溯类型
    public static final String IS_REVIEWED = "isReviewed";
    //JY 添加D-note, coating, welding, DWG rev, MS rev
    public static final String D_NOTE = "dNote";
    public static final String D_NOTE_REV = "dNoteRev";
    public static final String COATING = "coating";
    public static final String COATING_REV = "coatingRev";
    public static final String WELDING = "welding";
    public static final String WELDING_REV = "weldingRev";
    public static final String DRAW_REV = "drawRev";
    public static final String HARDNESS_REV = "hardnessRev";


    @Column(name = "SPARE_PART_NO", length = 80)
    private String sPartNo;

    @Column(name = "SPARE_PART_REV", length = 80)
    private String sPartRev;

    @Column(name = "SPARE_PART_DEC", length = 80)
    private String sPartDec;

    @Column(name = "QA_PLAN_REV", length = 80)
    private String qaPalnRev;

    @Column(name = "QA_PLAN", length = 80)
    private String qaPaln;

    @Column(name = "DRAW_NO", length = 80)
    private String drawNo;

    @Column(name = "PSL_LEVEL_STAND", length = 80)
    private String pslLevelStand;

    @Column(name = "API_STAND", length = 80)
    private String apiStand;

    @Column(name = "HARDNESS_FILE", length = 80)
    private String hardnessFile;

    @Column(name = "PLM_REV", length = 80)
    private String plmRev;

    @Column(name = "PART_REV", length = 80)
    private String partRev;

    @Column(name = "LONG_TEXT", columnDefinition = "longtext")
    private String longText;

    @Column(name = "IS_RETROSPECT")
    private boolean isRetrospect;

    @Column(name = "RETROSPECT_TYPE", length = 80)
    private String retrospectType;

    @Column(name = "IS_REVIEWED")
    private boolean isReviewed;

    //JY 添加D-note, coating, welding, DWG rev, MS rev
    @Column(name = "D_NOTE", length = 80)
    private String dNote;

    @Column(name = "D_NOTE_REV", length = 80)
    private String dNoteRev;

    @Column(name = "COATING", length = 80)
    private String coating;

    @Column(name = "COATING_REV", length = 80)
    private String coatingRev;

    @Column(name = "WELDING", length = 80)
    private String welding;

    @Column(name = "WELDING_REV", length = 80)
    private String weldingRev;

    @Column(name = "DRAW_REV", length = 80)
    private String drawRev;

    @Column(name = "HARDNESS_REV", length = 80)
    private String hardnessRev;
}
