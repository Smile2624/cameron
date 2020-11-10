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
@Table(name = "PRESSURE_RULER")
public class PressureRulerEntity extends BaseEntity {

    private static final long serialVersionUID = -1471757271026810105L;

    //---old unused columns---
    public static final String RULER_NO = "rulerNo";//压力标准文档编号
    public static final String SUFFIX_NO = "suffixNo";//最后两位后缀
    public static final String INSPECTION_TIMES = "inspectionTimes";//测压次数
    public static final String UPPERSTREAM_INSPECTION_TIMES = "upperStreamInspectionTimes";
    public static final String UPPERSTREAM_TIME = "upperStreamTime";
    public static final String UPPERSTREAM_TIME1 = "upperStreamTime1";
    public static final String UPPERSTREAM_TIME2 = "upperStreamTime2";
    public static final String TIME = "time";
    public static final String TIME1 = "time1";
    public static final String TIME2 = "time2";
    public static final String DOWNSTREAM_INSPECTION_TIMES = "downStreamInspectionTimes";
    public static final String DOWNSTREAM_TIME = "downStreamTime";
    public static final String DOWNSTREAM_TIME1 = "downStreamTime1";
    public static final String DOWNSTREAM_TIME2 = "downStreamTime2";

    //-----new used column-----
    public static final String PRODUCT_NO = "productNo";//产品编号
    public static final String PRESSURE_TYPE = "pressureType";//测压种类
    public static final String TEST_PRESSURE_VALUE = "testPressureValue";//测试压力(psi)
    public static final String MAX_PRESSURE_VALUE = "maxPressureValue";//最大压力(psi)
    public static final String DIFFERENCE_PRESSURE_VALUE = "differencePressureValue";//压力差值(psi)
    public static final String FIRST_TIME = "firstTime";//第一次测压时间(min)
    public static final String SECOND_TIME = "secondTime";//第二次测压时间(min)
    public static final String THIRD_TIME = "thirdTime";//第三次测压时间(min)
    public static final String TORQUE_VALUE = "torqueValue";//带压扭矩值(Nm)
    //-----------new used columns -------
    @Column(name = "PRODUCT_NO", length = 80)
    private String productNo;

    @Column(name = "PRESSURE_TYPE", length = 80)
    private String pressureType;

    @Column(name = "TEST_PRESSURE_VALUE")
    private double testPressureValue;

    @Column(name = "MAX_PRESSURE_VALUE")
    private double maxPressureValue;

    @Column(name = "DIFFERENCE_PRESSURE_VALUE")
    private double differencePressureValue;

    @Column(name = "FIRST_TIME")
    private double firstTime;

    @Column(name = "SECOND_TIME")
    private double secondTime;

    @Column(name = "THIRD_TIME")
    private double thirdTime;

    @Column(name = "TORQUE_VALUE")//上游试验时间
    private double torqueValue;

    //---old unused columns---

    @Column(name = "RULER_NO", length = 80)
    private String rulerNo;

    @Column(name = "SUFFIX_NO", length = 80)
    private String suffixNo;

    @Column(name = "INSPECTION_TIMES")//本体试验次数
    private int inspectionTimes;

    @Column(name = "UPPER_INSPECTION_TIMES")//本体试验次数
    private int upperStreamInspectionTimes;

    @Column(name = "TIME")
    private double time;

    @Column(name = "TIME1")
    private double time1;

    @Column(name = "TIME2")
    private double time2;

    @Column(name = "UPPERTIME")//上游试验时间
    private double upperStreamTime;

    @Column(name = "UPPERTIME1")//上游试验时间
    private double upperStreamTime1;

    @Column(name = "UPPERTIME2")//上游试验时间
    private double upperStreamTime2;

    @Column(name = "DOWN_INSPECTION_TIMES")//下游试验次数
    private int downStreamInspectionTimes;

    @Column(name = "DOWNTIME")
    private double downStreamTime;

    @Column(name = "DOWNTIME1")
    private double downStreamTime1;

    @Column(name = "DOWNTIME2")
    private double downStreamTime2;


}
