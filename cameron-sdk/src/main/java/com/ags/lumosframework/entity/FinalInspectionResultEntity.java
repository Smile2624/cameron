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
@Table(name = "FINAL_INSPECTION_RESULT")
public class FinalInspectionResultEntity extends BaseEntity {

    public static final String ORDER_NO = "orderNo";//工单
    public static final String CONSUMER = "consumer";//客户
    public static final String SALE_ORDER_NO = "saleOrderNo";//销售单号
    public static final String ROUTE_PACKAGE_RESULT = "routePackageResult";//工作线路包
    public static final String ASSEMBLING_RECORDER_RESULT = "assemblingRecorderResult";//装配记录
    public static final String PRESSURE_TEST_REPORT_RESULT = "pressureTestReportResult";//压力试验
    public static final String MTR_METRIAL_REPORT_RESULT = "MTRMetrialReportResult";//MTR材料报告
    public static final String DIMENSIONAL_REPORT_RESULT = "dimensionalReportResult";//尺寸报告
    public static final String HARDNESS_REPORT_RESULT = "hardnessReportResult";//硬度报告
    public static final String VISUAL_EXAMINATION_RESULT = "visualExaminationResult";//外观检测
    public static final String FUNCTION_INSPECTION_RESULT = "functionInspectionResult";//功能检验
    public static final String MONOGRAMMING_STATUS_RESULT = "monogrammingStatusResult";//会标是否应用且正确
    public static final String COMMENTS_RESULT = "commentsResult";//="其他
    public static final String QC_CONFIRM_USER = "qcConfirmUser";
    public static final String QC_CONFIRM_DATE = "qcConfirmDate";
    public static final String QA_CONFIRM_USER = "qaConfirmUser";
    public static final String QA_CONFIRM_DATE = "qaConfirmDate";
    public static final String REPORT_NO = "reportNo";//报告编号
    /**
     *
     */
    private static final long serialVersionUID = 9126209013586199968L;
    //终检结果 qc qa 都保存在此表
    @Column(name = "ORDER_NO", length = 80)
    private String orderNo;

    @Column(name = "CONSUMER", length = 80)
    private String consumer;

    @Column(name = "SALE_ORDER_NO", length = 80)
    private String saleOrderNo;

    @Column(name = "ROUTE_PACKAGE_RESULT", length = 80)
    private String routePackageResult;

    @Column(name = "ASSEMBLING_RECORDER_RESULT", length = 80)
    private String assemblingRecorderResult;

    @Column(name = "PRESSURE_TEST_REPORT_RESULT", length = 80)
    private String pressureTestReportResult;

    @Column(name = "MTR_METRIAL_REPORT_RESULT", length = 80)
    private String MTRMetrialReportResult;

    @Column(name = "DIMENSIONAL_REPORT_RESULT", length = 80)
    private String dimensionalReportResult;

    @Column(name = "HARDNESS_REPORT_RESULT", length = 80)
    private String hardnessReportResult;

    @Column(name = "VISUAL_EXAMINATION_RESULT", length = 80)
    private String visualExaminationResult;

    @Column(name = "FUNCTION_INSPECTION_RESULT", length = 80)
    private String functionInspectionResult;

    @Column(name = "MONOGRAMMING_STATUS_RESULT", length = 80)
    private String monogrammingStatusResult;

    @Column(name = "COMMENTS_RESULT", length = 80)
    private String commentsResult;

    @Column(name = "QC_CONFIRM_USER", length = 80)
    private String qcConfirmUser;

    @Column(name = "QC_CONFIRM_DATE")
    private Date qcConfirmDate;

    @Column(name = "QA_CONFIRM_USER", length = 80)
    private String qaConfirmUser;

    @Column(name = "QA_CONFIRM_DATE")
    private Date qaConfirmDate;

    @Column(name = "REPORT_NO")
    private String reportNo;
}
