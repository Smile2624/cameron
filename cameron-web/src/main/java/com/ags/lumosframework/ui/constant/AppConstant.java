package com.ags.lumosframework.ui.constant;

public class AppConstant {


    public static final String RESULT_OK = "OK";
    public static final String RESULT_NG = "NG";
    //终检检测项
    public static final String WORK_ROUTING = "工作路线包";
    public static final String ASSEMBLING_REPORT = "装配记录";
    public static final String PRESSURE_TEST_REPORT = "压力试验";
    public static final String MTR_METRIAL_REPORT = "MTR材料报告";
    public static final String DIMENSIONAL_REPORT = "尺寸报告";
    public static final String HARDNESS_REPORT = "硬度报告";
    public static final String VISUAL_EXAMINATION = "外观检测";
    public static final String FUNCTION_INSPECTION = "功能检验";
    public static final String MONOGRAMMING_STATUS = "会标是否应用且正确";
    public static final String COMMENTS = "其他";

    //追溯类型
//	public static final String RETROSPECT_TYPE_BATCH="QP";
//	public static final String RETROSPECT_TYPE_PIECE="IQP";

    public static final String RETROSPECT_TYPE_BATCH = "batch";
    public static final String RETROSPECT_TYPE_PIECE = "singleton";

    //工厂代码
    public static final String FACTORY_CODE = "A419";

    //扭矩测量标准
    public static final String TORQUE_RULER = "10-16NM";

    //来料检验 尺寸检验  抽检比率
    public static final double INSPECTION_RATIO = 0.3;

    //语音检测
    public static int PORT = 10001;
    public static final String PREFIXPLAYTEXT = "[PT]";//播放语音
    public static final String PREFIXSTARTRECORD = "[BR]";//识别语音
    public static final String PREFIXPLATTEXTEND = "[OC]";//语音播放完成
    public static final String PREFIXKEYBOARDINPUT = "[KB]";//语音播放完成
    public static final String PREFIXENDRECORD = "[ER]";//语音播放完成
    public static final String PREFIXRECORDRESULT = "[OR]";//语音识别返回信息
    public static final String PREFIXRECORDRESULTEND = "[ES]";//语音识别结束
    public static final String INSPECTIONDONE = "[DO]";
    public static final String IFSCANIN = "[IC]";//当录入的数据不在标准范围时，发送是否录入指令
    public static final String PALYEXCEPTION = "[OE]";
    //零件追溯要求
    public static final String RETROSPECTREQUIRE = "Q_QUALITY";//追溯要求
    public static final String RETROSPECTSINGLE = "Q1";//单件
    public static final String RETROSPECTBATCH = "Q1B";//批次


    //硬度试验程序 固定
    public static final String HARDNESS_TEST_SPECIFICATION = "X-008065 / 08";
    public static final String MEASURING_TYPE = "TYPE“B”";


    //系统配置类型
    public static final String CA_CONFIRM_ROUTING_MIN = "CA_CONFIRM_ROUTING_MIN";//工序确认时间间隔
    public static final String CA_CONFIRM_REPORT_SAVE_PATH = "CA_CONFIRM_REPORT_SAVE_PATH";//报告存放根路径
    public static final String CA_CONFIRM_PRESSTEST_SQLITE_FM = "CA_CONFIRM_PRESSTEST_SQLITE_FM";//压力数据存放路径-sqlite阀门
    public static final String CA_CONFIRM_PRESSTEST_SQLITE_JK = "CA_CONFIRM_PRESSTEST_SQLITE_JK";//压力数据存放路径-sqlite井口
    public static final String CA_CONFIRM_ACCESS_JK = "CA_CONFIRM_ACCESS_JK";//压力数据存放路径-access井口
    public static final String CA_CONFIRM_ACCESS_FM = "CA_CONFIRM_ACCESS_FM";//压力数据存放路径-access阀门
    public static final String CA_CONFIRM_PRESSTEST_LABVIEW = "CA_CONFIRM_PRESSTEST_LABVIEW";//压力数据存放路径-labview

    //资格证
    public static final String COC_HAVE_LOGO = "HAVELOGO";//有会标
    public static final String COC_NO_LOGO = "NOLOGO";


    //按照报告种类分为来料跟产品两种报告，来料包括硬度检验，尺寸检验，进货。产品包括装配，喷漆，压力，终检 合格证
    public static final String MATERIAL_PREFIX = "\\MaterialInspection\\";
    public static final String DIMENSION_REPORT = "Dimension\\";
    public static final String HARDNESS_INSPECTION_REPORT = "Hardness\\";
    public static final String VISUAL_REPORT = "Visual\\";
    public static final String RECEIVING_REPORT = "Receiving\\";
    public static final String ACME="ACME\\";

    public static final String PRODUCTION_PREFIX = "\\Production\\";
    public static final String ASSEMBLY_REPORT = "Assembly\\";
    public static final String PAINT_REPORT = "Paint\\";
    public static final String PRESSURE_REPORT = "Prussure\\";
    public static final String FINAL_REPORT = "Final\\";
    public static final String COC = "Coc\\";
    public static final String GX_RECORD = "GxRecord\\";
    public static final String LEVP="LEVP\\";


    public static final String PDF = "\\Pdf\\";
    public static final String VENDOR = "\\vendor\\";

    //模板路径
    public static final String DOC_XML_FILE_PATH = "D:\\CameronQualityFiles\\DOCS";
    public static final String STANDARD_FILE_PATH = "D:\\CameronQualityFiles\\STANDARDFILE\\";

    //压力设备测压类型
    public static final String PRESSURE_SQLITE = "SQLite";
    public static final String PRESSURE_ACCESS = "Access";
    public static final String PRESSURE_LABVIEW = "LabView";
    
    //来料检验时，QA查看文件包，清单，合格证的的pdf文件存放路径
    public static final String MATERIAL_INSPECTION_FILE_PATH = "C:\\CameronQualityFiles\\MATERIAL";

//发料zhungtai
    public static final String STATUS_Y="Y";
    public static final String STATUS_N="N";
}
