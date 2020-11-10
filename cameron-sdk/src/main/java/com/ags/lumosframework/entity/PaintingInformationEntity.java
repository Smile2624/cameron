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
@Table(name = "PAINTING_INFORMATION")
public class PaintingInformationEntity extends BaseEntity {

    public static final String WO_SN = "workOrderSN";//等同产品订单号
    public static final String IS_CLEAR = "isClear";//是否清洗
    public static final String IS_PROTECT = "isProtect";//是否保护
    public static final String PRIMER_AIR_TEMP = "primerAirTemp";//底漆空气湿度    PRIMER  primer
    public static final String PRIMER_SURFACE_TEMP = "primerSurfaceTemp";//底漆表面温度
    public static final String PRIMER_HUMIDITY = "primerHumidity";//底漆湿度
    public static final String PRIMER_DEW_POINT = "primerDewPoint";//底漆漏点温度
    public static final String PRIMER_PAINT_APPLIED = "primerPaintApplied";//底漆油漆型号
    public static final String PRIMER_DRY_FILM_THICKNESS = "primerDryFilmThickness";//底漆干膜厚度要求
    public static final String PRIMER_DRY_AIR_TEMP = "primerDryAirTemp";//底漆干燥空气温度
    public static final String PRIMER_DRY_HUMIDITY = "primerDryHumidity";//底漆干燥湿度
    public static final String PRIMER_DRY_METHOD = "primerDryMethod";//底漆干燥方式
    public static final String PRIMER_DRY_TIME = "primerDryTime";//底漆干燥时间
    public static final String FINAL_COAT_AIR_TEMP = "finalCoatAirTemp";//面漆空气温度 finalCoat FINAL_COAT
    public static final String FINAL_COAT_SURFACE_TEMP = "finalCoatSurfaceTemp";//面漆表面温度
    public static final String FINAL_COAT_HUMIDITY = "finalCoatHumidity";//面漆湿度
    public static final String FINAL_COAT_DEW_POINT = "finalCoatDewPoint";//面漆漏点温度
    public static final String FINAL_COAT_PAINT_APPLIED = "finalCoatPaintApplied";//面漆油漆型号
    public static final String FINAL_COAT_COLOR = "finalCoatColor";//面漆颜色
    public static final String FINAL_COAT_TOTAL_FILM_THICKNESS = "finalCoatTotalDryFilmThickness";//面漆总干膜厚度要求
    public static final String FINAL_COAT_DRY_AIR_TEMP = "finalCoatDryAirTemp";//面漆干燥空气湿度
    public static final String FINAL_COAT_DRY_HUMIDITY = "finalCoatDryHumidity";//面漆干燥湿度
    public static final String FINAL_COAT_DRY_METHOD = "finalCoatDryMethod";//面漆干燥方式
    public static final String FINAL_COAT_DRY_TIME = "finalCoatDryTime";//面漆干燥时间
    public static final String VISUAL_INSPECTION = "visualInspection";//外观检查
    public static final String VISUAL_TOTAL_DRY_FILM_THICKNESS = "visualTotalDryFilmThickness";//外观总干膜厚度
    public static final String VISUAL_GAGE_NO = "visualGageNo";//量具编号
    public static final String COMMENTS = "comments";//结论
    public static final String INTERMEDIATE = "intermediate";//结论
    //add
    public static final String OP_USER = "opUser";
    public static final String QC_CONFIRMER = "qcConfirmer";
    /**
     * 35个字段
     */
    private static final long serialVersionUID = 1L;
    @Column(name = "WO_SN", length = 80)
    private String workOrderSN;


    @Column(name = "IS_CLEAR")
    private boolean isClear;

    @Column(name = "IS_PROTECT")
    private boolean isProtect;

    @Column(name = "PRIMER_AIR_TEMP")
    private Float primerAirTemp;

    @Column(name = "PRIMER_SURFACE_TEMP")
    private Float primerSurfaceTemp;

    @Column(name = "PRIMER_HUMIDITY")
    private Float primerHumidity;

    @Column(name = "PRIMER_DEW_POINT")
    private Float primerDewPoint;

    @Column(name = "PRIMER_PAINT_APPLIED", length = 80)
    private String primerPaintApplied;

    @Column(name = "PRIMER_DRY_FILM_THICKNESS", length = 80)
    private String primerDryFilmThickness;

    @Column(name = "PRIMER_DRY_AIR_TEMP")
    private Float primerDryAirTemp;

    @Column(name = "PRIMER_DRY_HUMIDITY")
    private Float primerDryHumidity;

    @Column(name = "PRIMER_DRY_METHOD", length = 80)
    private String primerDryMethod;

    @Column(name = "PRIMER_DRY_TIME")
    private Float primerDryTime;

    @Column(name = "FINAL_COAT_AIR_TEMP")
    private Float finalCoatAirTemp;

    @Column(name = "FINAL_COAT_SURFACE_TEMP")
    private Float finalCoatSurfaceTemp;

    @Column(name = "FINAL_COAT_HUMIDITY")
    private Float finalCoatHumidity;

    @Column(name = "FINAL_COAT_DEW_POINT")
    private Float finalCoatDewPoint;

    @Column(name = "FINAL_COAT_PAINT_APPLIED", length = 80)
    private String finalCoatPaintApplied;

    @Column(name = "FINAL_COAT_COLOR", length = 80)
    private String finalCoatColor;

    @Column(name = "FINAL_COAT_TOTAL_FILM_THICKNESS", length = 80)
    private String finalCoatTotalDryFilmThickness;

    @Column(name = "FINAL_COAT_DRY_AIR_TEMP")
    private Float finalCoatDryAirTemp;

    @Column(name = "FINAL_COAT_DRY_HUMIDITY")
    private Float finalCoatDryHumidity;

    @Column(name = "FINAL_COAT_DRY_METHOD", length = 80)
    private String finalCoatDryMethod;

    @Column(name = "FINAL_COAT_DRY_TIME")
    private Float finalCoatDryTime;

    @Column(name = "VISUAL_INSPECTION", length = 80)
    private String visualInspection;

    @Column(name = "VISUAL_TOTAL_DRY_FILM_THICKNESS",length = 80)
    private String visualTotalDryFilmThickness;

    @Column(name = "VISUAL_GAGE_NO", length = 80)
    private String visualGageNo;

    @Column(name = "COMMENTS", length = 80)
    private String comments;

    @Column(name = "INTERMEDIATE")
    private Float intermediate;

    @Column(name = "OP_USER", length = 80)
    private String opUser;

    @Column(name = "QC_CONFIRMER", length = 80)
    private String qcConfirmer;
}
