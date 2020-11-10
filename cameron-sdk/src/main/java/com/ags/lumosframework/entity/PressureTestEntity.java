//
// Decompiled by Procyon v0.5.36
//

package com.ags.lumosframework.entity;

import com.ags.lumosframework.sdk.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "PRESSURE_TEST")
public class PressureTestEntity extends BaseEntity {
    private static final long serialVersionUID = 3191124650587046976L;
    public static final String PRODUCT_SN = "productSN";
    public static final String WORK_ORDER = "workOrder";
    public static final String PART_NO = "partNo";
    public static final String PART_REV = "partRev";

    public static final String STATION_NAME = "stationName";//-
    public static final String STATION_EQU_NO = "stationEpuNo";//-
    //通径规编号和尺寸
    public static final String GAGE_NO_SIZE = "gageNoSize";
    //压力传感器编号
    public static final String PROCEDURE_NO = "procedureNo";
    //带压开启
    public static final String BLOW_DOWN_TEST = "blowDownTest";
    //通径测试
    public static final String DRIFT_TEST = "driftTest";
    public static final String DRIFT_TEST_BY = "driftTestBy";
    public static final String DRIFT_TEST_DATE = "driftTestDate";

    public static final String TEST_TYPE = "testType";
    public static final String TEST_BY = "testBy";
    public static final String TEST_DATE = "testDate";
    public static final String TEST_RESULT = "testResult";//最终结果 OK/NG
    public static final String TEST_TABLE_NAME = "testTableName";
    //扭矩值
    public static final String TORQUE_VALUE1 = "torqueValue1";
    public static final String TORQUE_VALUE2 = "torqueValue2";
    public static final String TORQUE_VALUE3 = "torqueValue3";
    public static final String TORQUE_VALUE4 = "torqueValue4";
    public static final String TORQUE_DRIFT_SAVED = "torqueDriftSaved";//-

    public static final String TORQUE_SENSOR_NO = "torqueSensorNo";//Changed by Cameron: 加入阀门带压打开时扭矩传感器的记录

    //fm-本体
    public static final String FM_OPEN_BODY_FLAG = "fmOpenBodyFlag";
    public static final String FM_OPEN_BODY_STATION_NAME = "fmOpenBodyStationName";
    public static final String FM_OPEN_BODY_STATION_NO = "fmOpenBodyStationNo";
    public static final String FM_OPEN_BODY_RECORD = "fmOpenBodyRecord";
    public static final String FM_OPEN_P1 = "fmOpenP1";//压力
    public static final String FM_OPEN_P2 = "fmOpenP2";
    public static final String FM_OPEN_P3 = "fmOpenP3";
    public static final String FM_OPEN_T1 = "fmOpenT1";//时间
    public static final String FM_OPEN_T2 = "fmOpenT2";
    public static final String FM_OPEN_T3 = "fmOpenT3";
    public static final String FM_OPEN_RESULT = "fmOpenResult";//结果
    public static final String FM_OPEN_USER = "fmOpenUser";
    public static final String FM_OPEN_DATE = "fmOpenDate";
    //fm-下游
    public static final String FM_DOWN_FLAG = "fmDownFlag";
    public static final String FM_DOWN_STATION_NAME = "fmDownStationName";
    public static final String FM_DOWN_STATION_NO = "fmDownStationNo";
    public static final String FM_DOWN_RECORD = "fmDownRecord";
    public static final String FM_DOWN_P1 = "fmDownP1";//压力
    public static final String FM_DOWN_P2 = "fmDownP2";
    public static final String FM_DOWN_P3 = "fmDownP3";
    public static final String FM_DOWN_T1 = "fmDownT1";//时间
    public static final String FM_DOWN_T2 = "fmDownT2";
    public static final String FM_DOWN_T3 = "fmDownT3";
    public static final String FM_DOWN_RESULT = "fmDownResult";//结果
    public static final String FM_DOWN_USER = "fmDownUser";
    public static final String FM_DOWN_DATE = "fmDownDate";
    //fm-上游
    public static final String FM_UP_FLAG = "fmUpFlag";
    public static final String FM_UP_STATION_NAME = "fmUpStationName";
    public static final String FM_UP_STATION_NO = "fmUpStationNo";
    public static final String FM_UP_RECORD = "fmUpRecord";
    public static final String FM_UP_P1 = "fmUpP1";//压力
    public static final String FM_UP_P2 = "fmUpP2";
    public static final String FM_UP_P3 = "fmUpP3";
    public static final String FM_UP_T1 = "fmUpT1";//时间
    public static final String FM_UP_T2 = "fmUpT2";
    public static final String FM_UP_T3 = "fmUpT3";
    public static final String FM_UP_RESULT = "fmUpResult";//结果
    public static final String FM_UP_USER = "fmUpUser";
    public static final String FM_UP_DATE = "fmUpDate";


    //jk-本体
    public static final String JK_OPEN_BODY_FLAG = "jkOpenBodyFlag";
    public static final String JK_OPEN_BODY_STATION_NAME = "jkOpenBodyStationName";
    public static final String JK_OPEN_BODY_STATION_NO = "jkOpenBodyStationNo";
    public static final String JK_OPEN_BODY_RECORD = "jkOpenBodyRecord";
    public static final String JK_OPEN_P1 = "jkOpenP1";//压力
    public static final String JK_OPEN_P2 = "jkOpenP2";
    public static final String JK_OPEN_P3 = "jkOpenP3";
    public static final String JK_OPEN_T1 = "jkOpenT1";//时间
    public static final String JK_OPEN_T2 = "jkOpenT2";
    public static final String JK_OPEN_T3 = "jkOpenT3";
    public static final String JK_OPEN_R1 = "jkOpenR1";//结果
    public static final String JK_OPEN_R2 = "jkOpenR2";
    public static final String JK_OPEN_R3 = "jkOpenR3";
    public static final String JK_OPEN_USER = "jkOpenUser";
    public static final String JK_OPEN_DATE = "jkOpenDate";
    //jk-转换接头本体
    public static final String JK_CROSSOVER_BODY_FLAG = "jkCrossoverBodyFlag";
    public static final String JK_CROSSOVER_BODY_TOP_STATION_NAME = "jkCrossoverBodyTopStationName";
    public static final String JK_CROSSOVER_BODY_TOP_STATION_NO = "jkCrossoverBodyTopStationNo";
    public static final String JK_CROSSOVER_BODY_TOP_RECORD = "jkCrossoverBodyTopRecord";
    public static final String JK_CROSSOVER_BODY_BTM_STATION_NAME = "jkCrossoverBodyBtmStationName";
    public static final String JK_CROSSOVER_BODY_BTM_STATION_NO = "jkCrossoverBodyBtmStationNo";
    public static final String JK_CROSSOVER_BODY_BTM_RECORD = "jkCrossoverBodyBtmRecord";
    public static final String JK_CROSSOVER_BODY_TOP_P1 = "jkCrossoverBodyTopP1";
    public static final String JK_CROSSOVER_BODY_TOP_P2 = "jkCrossoverBodyTopP2";
    public static final String JK_CROSSOVER_BODY_TOP_P3 = "jkCrossoverBodyTopP3";
    public static final String JK_CROSSOVER_BODY_TOP_T1 = "jkCrossoverBodyTopT1";
    public static final String JK_CROSSOVER_BODY_TOP_T2 = "jkCrossoverBodyTopT2";
    public static final String JK_CROSSOVER_BODY_TOP_T3 = "jkCrossoverBodyTopT3";
    public static final String JK_CROSSOVER_BODY_TOP_R1 = "jkCrossoverBodyTopR1";
    public static final String JK_CROSSOVER_BODY_TOP_R2 = "jkCrossoverBodyTopR2";
    public static final String JK_CROSSOVER_BODY_TOP_R3 = "jkCrossoverBodyTopR3";

    public static final String JK_CROSSOVER_BODY_BTM_P1 = "jkCrossoverBodyBtmP1";
    public static final String JK_CROSSOVER_BODY_BTM_P2 = "jkCrossoverBodyBtmP2";
    public static final String JK_CROSSOVER_BODY_BTM_P3 = "jkCrossoverBodyBtmP3";
    public static final String JK_CROSSOVER_BODY_BTM_T1 = "jkCrossoverBodyBtmT1";
    public static final String JK_CROSSOVER_BODY_BTM_T2 = "jkCrossoverBodyBtmT2";
    public static final String JK_CROSSOVER_BODY_BTM_T3 = "jkCrossoverBodyBtmT3";
    public static final String JK_CROSSOVER_BODY_BTM_R1 = "jkCrossoverBodyBtmR1";
    public static final String JK_CROSSOVER_BODY_BTM_R2 = "jkCrossoverBodyBtmR2";
    public static final String JK_CROSSOVER_BODY_BTM_R3 = "jkCrossoverBodyBtmR3";
    public static final String JK_CROSSOVER_BODY_USER = "jkCrossoverBodyUser";
    public static final String JK_CROSSOVER_BODY_DATE = "jkCrossoverBodyDate";
    //jk-采油树
    public static final String JK_CHRISTMAS_TREE_FLAG = "jkChristmasTreeFlag";
    public static final String JK_CHRISTMAS_TREE_STATION_NAME = "jkChristmasTreeStationName";
    public static final String JK_CHRISTMAS_TREE_STATION_NO = "jkChristmasTreeStationNo";
    public static final String JK_CHRISTMAS_TREE_RECORD = "jkChristmasTreeRecord";
    public static final String JK_CHRISTMAS_TREE_P1 = "jkChristmasTreeP1";//压力
    public static final String JK_CHRISTMAS_TREE_P2 = "jkChristmasTreeP2";
    public static final String JK_CHRISTMAS_TREE_P3 = "jkChristmasTreeP3";
    public static final String JK_CHRISTMAS_TREE_T1 = "jkChristmasTreeT1";//时间
    public static final String JK_CHRISTMAS_TREE_T2 = "jkChristmasTreeT2";
    public static final String JK_CHRISTMAS_TREE_T3 = "jkChristmasTreeT3";
    public static final String JK_CHRISTMAS_TREE_R1 = "jkChristmasTreeR1";//结果
    public static final String JK_CHRISTMAS_TREE_R2 = "jkChristmasTreeR2";
    public static final String JK_CHRISTMAS_TREE_R3 = "jkChristmasTreeR3";
    public static final String JK_CHRISTMAS_TREE_USER = "jkChristmasTreeUser";
    public static final String JK_CHRISTMAS_TREE_DATE = "jkChristmasTreeDate";
    //jk-井口头组件
    public static final String JK_WELLHEAD_ASSEMBLY_FLAG = "jkWellheadAssemblyFlag";
    public static final String JK_WELLHEAD_ASSEMBLY_STATION_NAME = "jkWellheadAssemblyStationName";
    public static final String JK_WELLHEAD_ASSEMBLY_STATION_NO = "jkWellheadAssemblyStationNo";
    public static final String JK_WELLHEAD_ASSEMBLY_RECORD = "jkWellheadAssemblyRecord";
    public static final String JK_WELLHEAD_P1 = "jkWellheadP1";//压力
    public static final String JK_WELLHEAD_P2 = "jkWellheadP2";
    public static final String JK_WELLHEAD_P3 = "jkWellheadP3";
    public static final String JK_WELLHEAD_T1 = "jkWellheadT1";//时间
    public static final String JK_WELLHEAD_T2 = "jkWellheadT2";
    public static final String JK_WELLHEAD_T3 = "jkWellheadT3";
    public static final String JK_WELLHEAD_R1 = "jkWellheadR1";//结果
    public static final String JK_WELLHEAD_R2 = "jkWellheadR2";
    public static final String JK_WELLHEAD_R3 = "jkWellheadR3";
    public static final String JK_WELLHEAD_USER = "jkWellheadUser";
    public static final String JK_WELLHEAD_DATE = "jkWellheadDate";
    //jk-转换接头组件
    public static final String JK_CROSSOVER_ASSEMBLY_FLAG = "jkCrossoverAssemblyFlag";
    public static final String JK_CROSSOVER_ASSEMBLY_TOP_STATION_NAME = "jkCrossoverAssemblyTopStationName";
    public static final String JK_CROSSOVER_ASSEMBLY_TOP_STATION_NO = "jkCrossoverAssemblyTopStationNo";
    public static final String JK_CROSSOVER_ASSEMBLY_TOP_RECORD = "jkCrossoverAssemblyTopRecord";
    public static final String JK_CROSSOVER_ASSEMBLY_BTM_STATION_NAME = "jkCrossoverAssemblyBtmStationName";
    public static final String JK_CROSSOVER_ASSEMBLY_BTM_STATION_NO = "jkCrossoverAssemblyBtmStationNo";
    public static final String JK_CROSSOVER_ASSEMBLY_BTM_RECORD = "jkCrossoverAssemblyBtmRecord";
    public static final String JK_CROSSOVER_ASSEMBLY_TOP_P1 = "jkCrossoverAssemblyTopP1";
    public static final String JK_CROSSOVER_ASSEMBLY_TOP_P2 = "jkCrossoverAssemblyTopP2";
    public static final String JK_CROSSOVER_ASSEMBLY_TOP_P3 = "jkCrossoverAssemblyTopP3";
    public static final String JK_CROSSOVER_ASSEMBLY_TOP_T1 = "jkCrossoverAssemblyTopT1";
    public static final String JK_CROSSOVER_ASSEMBLY_TOP_T2 = "jkCrossoverAssemblyTopT2";
    public static final String JK_CROSSOVER_ASSEMBLY_TOP_T3 = "jkCrossoverAssemblyTopT3";
    public static final String JK_CROSSOVER_ASSEMBLY_TOP_R1 = "jkCrossoverAssemblyTopR1";
    public static final String JK_CROSSOVER_ASSEMBLY_TOP_R2 = "jkCrossoverAssemblyTopR2";
    public static final String JK_CROSSOVER_ASSEMBLY_TOP_R3 = "jkCrossoverAssemblyTopR3";

    public static final String JK_CROSSOVER_ASSEMBLY_BTM_P1 = "jkCrossoverAssemblyBtmP1";
    public static final String JK_CROSSOVER_ASSEMBLY_BTM_P2 = "jkCrossoverAssemblyBtmP2";
    public static final String JK_CROSSOVER_ASSEMBLY_BTM_P3 = "jkCrossoverAssemblyBtmP3";
    public static final String JK_CROSSOVER_ASSEMBLY_BTM_T1 = "jkCrossoverAssemblyBtmT1";
    public static final String JK_CROSSOVER_ASSEMBLY_BTM_T2 = "jkCrossoverAssemblyBtmT2";
    public static final String JK_CROSSOVER_ASSEMBLY_BTM_T3 = "jkCrossoverAssemblyBtmT3";
    public static final String JK_CROSSOVER_ASSEMBLY_BTM_R1 = "jkCrossoverAssemblyBtmR1";
    public static final String JK_CROSSOVER_ASSEMBLY_BTM_R2 = "jkCrossoverAssemblyBtmR2";
    public static final String JK_CROSSOVER_ASSEMBLY_BTM_R3 = "jkCrossoverAssemblyBtmR3";
    public static final String JK_CROSSOVER_ASSEMBLY_USER = "jkCrossoverAssemblyUser";
    public static final String JK_CROSSOVER_ASSEMBLY_DATE = "jkCrossoverAssemblyDate";

    @Column(name = "PRODUCT_SN", length = 80)
    private String productSN;

    @Column(name = "PART_NO", length = 80)
    private String partNo;

    @Column(name = "PART_REV", length = 80)
    private String partRev;

    @Column(name = "STATION_NAME", length = 80)
    private String stationName;

    @Column(name = "STATION_EQU_NO", length = 80)
    private String stationEpuNo;

    @Column(name = "GAGE_NO_SIZE", length = 80)
    private String gageNoSize;

    @Column(name = "PROCEDURE_NO", length = 80)
    private String procedureNo;

    @Column(name = "BLOW_DOWN_TEST")
    private boolean blowDownTest;

    @Column(name = "DRIFT_TEST")
    private boolean driftTest;

    @Column(name = "DRIFT_TEST_BY", length = 80)
    private String driftTestBy;

    @Column(name = "TEST_BY", length = 80)
    private String testBy;

    @Column(name = "TEST_DATE", length = 80)
    private String testDate;

    @Column(name = "WORK_ORDER", length = 80)
    private String workOrder;

    @Column(name = "TEST_TABLE_NAME", length = 80)
    private String testTableName;

    @Column(name = "TEST_RESULT", length = 80)
    private String testResult;

    @Column(name = "TEST_TYPE", length = 80)
    private String testType;

    @Column(name = "TORQUE_DRIFT_SAVED")
    private boolean torqueDriftSaved;

    @Column(name = "TORQUE_VALUE1", length = 80)
    private String torqueValue1;

    @Column(name = "TORQUE_VALUE2", length = 80)
    private String torqueValue2;

    @Column(name = "TORQUE_VALUE3", length = 80)
    private String torqueValue3;

    @Column(name = "TORQUE_VALUE4", length = 80)
    private String torqueValue4;

    @Column(name = "TORQUE_SENSOR_NO", length = 80)
    private String torqueSensorNo;

    //fm-本体
    @Column(name = "FM_OPEN_BODY_FLAG")
    private boolean fmOpenBodyFlag;
    @Column(name = "FM_OPEN_BODY_STATION_NAME", length = 80)
    private String fmOpenBodyStationName;
    @Column(name = "FM_OPEN_BODY_STATION_NO", length = 80)
    private String fmOpenBodyStationNo;
    @Column(name = "FM_OPEN_BODY_RECORD", length = 80)
    private String fmOpenBodyRecord;
    @Column(name = "FM_OPEN_P1")
    private double fmOpenP1;
    @Column(name = "FM_OPEN_P2")
    private double fmOpenP2;
    @Column(name = "FM_OPEN_P3")
    private double fmOpenP3;
    @Column(name = "FM_OPEN_T1")
    private double fmOpenT1;
    @Column(name = "FM_OPEN_T2")
    private double fmOpenT2;
    @Column(name = "FM_OPEN_T3")
    private double fmOpenT3;
    @Column(name = "FM_OPEN_RESULT", length = 80)
    private String fmOpenResult;
    @Column(name = "FM_OPEN_USER", length = 80)
    private String fmOpenUser;
    @Column(name = "FM_OPEN_DATE", length = 80)
    private String fmOpenDate;
    //fm-下游
    @Column(name = "FM_DOWN_FLAG")
    private boolean fmDownFlag;
    @Column(name = "FM_DOWN_STATION_NAME", length = 80)
    private String fmDownStationName;
    @Column(name = "FM_DOWN_STATION_NO", length = 80)
    private String fmDownStationNo;
    @Column(name = "FM_DOWN_RECORD", length = 80)
    private String fmDownRecord;
    @Column(name = "FM_DOWN_P1")
    private double fmDownP1;
    @Column(name = "FM_DOWN_P2")
    private double fmDownP2;
    @Column(name = "FM_DOWN_P3")
    private double fmDownP3;
    @Column(name = "FM_DOWN_T1")
    private double fmDownT1;
    @Column(name = "FM_DOWN_T2")
    private double fmDownT2;
    @Column(name = "FM_DOWN_T3")
    private double fmDownT3;
    @Column(name = "FM_DOWN_RESULT", length = 80)
    private String fmDownResult;
    @Column(name = "FM_DOWN_USER", length = 80)
    private String fmDownUser;
    @Column(name = "FM_DOWN_DATE", length = 80)
    private String fmDownDate;
    //fm-上游
    @Column(name = "FM_UP_FLAG")
    private boolean fmUpFlag;
    @Column(name = "FM_UP_STATION_NAME", length = 80)
    private String fmUpStationName;
    @Column(name = "FM_UP_STATION_NO", length = 80)
    private String fmUpStationNo;
    @Column(name = "FM_UP_RECORD", length = 80)
    private String fmUpRecord;
    @Column(name = "FM_UP_P1")
    private double fmUpP1;
    @Column(name = "FM_UP_P2")
    private double fmUpP2;
    @Column(name = "FM_UP_P3")
    private double fmUpP3;
    @Column(name = "FM_UP_T1")
    private double fmUpT1;
    @Column(name = "FM_UP_T2")
    private double fmUpT2;
    @Column(name = "FM_UP_T3")
    private double fmUpT3;
    @Column(name = "FM_UP_RESULT", length = 80)
    private String fmUpResult;
    @Column(name = "FM_UP_USER", length = 80)
    private String fmUpUser;
    @Column(name = "FM_UP_DATE", length = 80)
    private String fmUpDate;

    //jk-本体
    @Column(name = "JK_OPEN_BODY_FLAG")
    private boolean jkOpenBodyFlag;
    @Column(name = "JK_OPEN_BODY_STATION_NAME", length = 80)
    private String jkOpenBodyStationName;
    @Column(name = "JK_OPEN_BODY_STATION_NO", length = 80)
    private String jkOpenBodyStationNo;
    @Column(name = "JK_OPEN_BODY_RECORD", length = 80)
    private String jkOpenBodyRecord;
    @Column(name = "JK_OPEN_P1")
    private double jkOpenP1;
    @Column(name = "JK_OPEN_P2")
    private double jkOpenP2;
    @Column(name = "JK_OPEN_P3")
    private double jkOpenP3;
    @Column(name = "JK_OPEN_T1")
    private double jkOpenT1;
    @Column(name = "JK_OPEN_T2")
    private double jkOpenT2;
    @Column(name = "JK_OPEN_T3")
    private double jkOpenT3;
    @Column(name = "JK_OPEN_R1", length = 80)
    private String jkOpenR1;
    @Column(name = "JK_OPEN_R2", length = 80)
    private String jkOpenR2;
    @Column(name = "JK_OPEN_R3", length = 80)
    private String jkOpenR3;
    @Column(name = "JK_OPEN_USER", length = 80)
    private String jkOpenUser;
    @Column(name = "JK_OPEN_DATE", length = 80)
    private String jkOpenDate;

    //jk-转换接头本体
    @Column(name = "JK_CROSSOVER_BODY_FLAG")
    private boolean jkCrossoverBodyFlag;
    @Column(name = "JK_CROSSOVER_BODY_TOP_STATION_NAME", length = 80)
    private String jkCrossoverBodyTopStationName;
    @Column(name = "JK_CROSSOVER_BODY_TOP_STATION_NO", length = 80)
    private String jkCrossoverBodyTopStationNo;
    @Column(name = "JK_CROSSOVER_BODY_TOP_RECORD", length = 80)
    private String jkCrossoverBodyTopRecord;
    @Column(name = "JK_CROSSOVER_BODY_BTM_STATION_NAME", length = 80)
    private String jkCrossoverBodyBtmStationName;
    @Column(name = "JK_CROSSOVER_BODY_BTM_STATION_NO", length = 80)
    private String jkCrossoverBodyBtmStationNo;
    @Column(name = "JK_CROSSOVER_BODY_BTM_RECORD", length = 80)
    private String jkCrossoverBodyBtmRecord;

    @Column(name = "JK_CROSSOVER_BODY_TOP_P1")
    private double jkCrossoverBodyTopP1;
    @Column(name = "JK_CROSSOVER_BODY_TOP_P2")
    private double jkCrossoverBodyTopP2;
    @Column(name = "JK_CROSSOVER_BODY_TOP_P3")
    private double jkCrossoverBodyTopP3;
    @Column(name = "JK_CROSSOVER_BODY_TOP_T1")
    private double jkCrossoverBodyTopT1;
    @Column(name = "JK_CROSSOVER_BODY_TOP_T2")
    private double jkCrossoverBodyTopT2;
    @Column(name = "JK_CROSSOVER_BODY_TOP_T3")
    private double jkCrossoverBodyTopT3;
    @Column(name = "JK_CROSSOVER_BODY_TOP_R1", length = 80)
    private String jkCrossoverBodyTopR1;
    @Column(name = "JK_CROSSOVER_BODY_TOP_R2", length = 80)
    private String jkCrossoverBodyTopR2;
    @Column(name = "JK_CROSSOVER_BODY_TOP_R3", length = 80)
    private String jkCrossoverBodyTopR3;

    @Column(name = "JK_CROSSOVER_BODY_BTM_P1")
    private double jkCrossoverBodyBtmP1;
    @Column(name = "JK_CROSSOVER_BODY_BTM_P2")
    private double jkCrossoverBodyBtmP2;
    @Column(name = "JK_CROSSOVER_BODY_BTM_P3")
    private double jkCrossoverBodyBtmP3;
    @Column(name = "JK_CROSSOVER_BODY_BTM_T1")
    private double jkCrossoverBodyBtmT1;
    @Column(name = "JK_CROSSOVER_BODY_BTM_T2")
    private double jkCrossoverBodyBtmT2;
    @Column(name = "JK_CROSSOVER_BODY_BTM_T3")
    private double jkCrossoverBodyBtmT3;
    @Column(name = "JK_CROSSOVER_BODY_BTM_R1", length = 80)
    private String jkCrossoverBodyBtmR1;
    @Column(name = "JK_CROSSOVER_BODY_BTM_R2", length = 80)
    private String jkCrossoverBodyBtmR2;
    @Column(name = "JK_CROSSOVER_BODY_BTM_R3", length = 80)
    private String jkCrossoverBodyBtmR3;

    @Column(name = "JK_CROSSOVER_BODY_USER", length = 80)
    private String jkCrossoverBodyUser;
    @Column(name = "JK_CROSSOVER_BODY_DATE", length = 80)
    private String jkCrossoverBodyDate;
    //jk-采油树
    @Column(name = "JK_CHRISTMAS_TREE_FLAG")
    private boolean jkChristmasTreeFlag;
    @Column(name = "JK_CHRISTMAS_TREE_STATION_NAME", length = 80)
    private String jkChristmasTreeStationName;
    @Column(name = "JK_CHRISTMAS_TREE_STATION_NO", length = 80)
    private String jkChristmasTreeStationNo;
    @Column(name = "JK_CHRISTMAS_TREE_RECORD", length = 80)
    private String jkChristmasTreeRecord;
    @Column(name = "JK_CHRISTMAS_TREE_P1")
    private double jkChristmasTreeP1;
    @Column(name = "JK_CHRISTMAS_TREE_P2")
    private double jkChristmasTreeP2;
    @Column(name = "JK_CHRISTMAS_TREE_P3")
    private double jkChristmasTreeP3;
    @Column(name = "JK_CHRISTMAS_TREE_T1")
    private double jkChristmasTreeT1;
    @Column(name = "JK_CHRISTMAS_TREE_T2")
    private double jkChristmasTreeT2;
    @Column(name = "JK_CHRISTMAS_TREE_T3")
    private double jkChristmasTreeT3;
    @Column(name = "JK_CHRISTMAS_TREE_R1", length = 80)
    private String jkChristmasTreeR1;
    @Column(name = "JK_CHRISTMAS_TREE_R2", length = 80)
    private String jkChristmasTreeR2;
    @Column(name = "JK_CHRISTMAS_TREE_R3", length = 80)
    private String jkChristmasTreeR3;
    @Column(name = "JK_CHRISTMAS_TREE_USER", length = 80)
    private String jkChristmasTreeUser;
    @Column(name = "JK_CHRISTMAS_TREE_DATE", length = 80)
    private String jkChristmasTreeDate;
    //jk-井口头组件
    @Column(name = "JK_WELLHEAD_ASSEMBLY_FLAG")
    private boolean jkWellheadAssemblyFlag;
    @Column(name = "JK_WELLHEAD_ASSEMBLY_STATION_NAME", length = 80)
    private String jkWellheadAssemblyStationName;
    @Column(name = "JK_WELLHEAD_ASSEMBLY_STATION_NO", length = 80)
    private String jkWellheadAssemblyStationNo;
    @Column(name = "JK_WELLHEAD_ASSEMBLY_RECORD", length = 80)
    private String jkWellheadAssemblyRecord;
    @Column(name = "JK_WELLHEAD_P1")
    private double jkWellheadP1;
    @Column(name = "JK_WELLHEAD_P2")
    private double jkWellheadP2;
    @Column(name = "JK_WELLHEAD_P3")
    private double jkWellheadP3;
    @Column(name = "JK_WELLHEAD_T1")
    private double jkWellheadT1;
    @Column(name = "JK_WELLHEAD_T2")
    private double jkWellheadT2;
    @Column(name = "JK_WELLHEAD_T3")
    private double jkWellheadT3;
    @Column(name = "JK_WELLHEAD_R1", length = 80)
    private String jkWellheadR1;
    @Column(name = "JK_WELLHEAD_R2", length = 80)
    private String jkWellheadR2;
    @Column(name = "JK_WELLHEAD_R3", length = 80)
    private String jkWellheadR3;
    @Column(name = "JK_WELLHEAD_USER", length = 80)
    private String jkWellheadUser;
    @Column(name = "JK_WELLHEAD_DATE", length = 80)
    private String jkWellheadDate;
    //jk-转换接头组件
    @Column(name = "JK_CROSSOVER_ASSEMBLY_FLAG")
    private boolean jkCrossoverAssemblyFlag;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_TOP_STATION_NAME", length = 80)
    private String jkCrossoverAssemblyTopStationName;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_TOP_STATION_NO", length = 80)
    private String jkCrossoverAssemblyTopStationNo;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_TOP_RECORD", length = 80)
    private String jkCrossoverAssemblyTopRecord;

    @Column(name = "JK_CROSSOVER_ASSEMBLY_BTM_STATION_NAME", length = 80)
    private String jkCrossoverAssemblyBtmStationName;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_BTM_STATION_NO", length = 80)
    private String jkCrossoverAssemblyBtmStationNo;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_BTM_RECORD", length = 80)
    private String jkCrossoverAssemblyBtmRecord;

    @Column(name = "JK_CROSSOVER_ASSEMBLY_TOP_P1")
    private double jkCrossoverAssemblyTopP1;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_TOP_P2")
    private double jkCrossoverAssemblyTopP2;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_TOP_P3")
    private double jkCrossoverAssemblyTopP3;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_TOP_T1")
    private double jkCrossoverAssemblyTopT1;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_TOP_T2")
    private double jkCrossoverAssemblyTopT2;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_TOP_T3")
    private double jkCrossoverAssemblyTopT3;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_TOP_R1", length = 80)
    private String jkCrossoverAssemblyTopR1;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_TOP_R2", length = 80)
    private String jkCrossoverAssemblyTopR2;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_TOP_R3", length = 80)
    private String jkCrossoverAssemblyTopR3;

    @Column(name = "JK_CROSSOVER_ASSEMBLY_BTM_P1")
    private double jkCrossoverAssemblyBtmP1;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_BTM_P2")
    private double jkCrossoverAssemblyBtmP2;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_BTM_P3")
    private double jkCrossoverAssemblyBtmP3;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_BTM_T1")
    private double jkCrossoverAssemblyBtmT1;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_BTM_T2")
    private double jkCrossoverAssemblyBtmT2;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_BTM_T3")
    private double jkCrossoverAssemblyBtmT3;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_BTM_R1", length = 80)
    private String jkCrossoverAssemblyBtmR1;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_BTM_R2", length = 80)
    private String jkCrossoverAssemblyBtmR2;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_BTM_R3", length = 80)
    private String jkCrossoverAssemblyBtmR3;

    @Column(name = "JK_CROSSOVER_ASSEMBLY_USER", length = 80)
    private String jkCrossoverAssemblyUser;
    @Column(name = "JK_CROSSOVER_ASSEMBLY_DATE", length = 80)
    private String jkCrossoverAssemblyDate;

    @Column(name = "DRIFT_TEST_DATE", length = 80)
    private String driftTestDate;


}