package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.FinalInspectionResultEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

import java.util.Date;

public class FinalInspectionResult extends ObjectBaseImpl<FinalInspectionResultEntity> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7285266095585437106L;
	
	public FinalInspectionResult(FinalInspectionResultEntity entity) {
		super(entity);
	}
	
	public FinalInspectionResult() {
		super(null);
	}

	@Override
	public String getName() {
		return null;
	}
	
	public String getOrderNo() {
		return this.getInternalObject().getOrderNo();
	}
	
	public void setOrderNo(String orderNo) {
		this.getInternalObject().setOrderNo(orderNo);
	}
	
	public String getConsumer() {
		return this.getInternalObject().getConsumer();
	}
	
	public void setConsumer(String consumer) {
		this.getInternalObject().setConsumer(consumer);
	}
	
	public String getSaleOrderNo() {
		return this.getInternalObject().getSaleOrderNo();
	}
	
	public void setSaleOrderNo(String saleOrderNo) {
		this.getInternalObject().setSaleOrderNo(saleOrderNo);
	}
	
	public String getRoutePackageResult() {
		return this.getInternalObject().getRoutePackageResult();
	}
	
	public void setRoutePackageResult(String routePackageResult) {
		this.getInternalObject().setRoutePackageResult(routePackageResult);
	}
	
	public String getAssemblingRecorderResult() {
		return this.getInternalObject().getAssemblingRecorderResult();
	}
	
	public void setAssemblingRecorderResult(String assemblingRecorderResult) {
		this.getInternalObject().setAssemblingRecorderResult(assemblingRecorderResult);
	}
	
	public String getPressureTestReportResult() {
		return this.getInternalObject().getPressureTestReportResult();
	}
	
	public void setPressureTestReportResult(String pressureTestReportResult) {
		this.getInternalObject().setPressureTestReportResult(pressureTestReportResult);
	}
	
	public String getMTRMetrialReportResult() {
		return this.getInternalObject().getMTRMetrialReportResult();
	}
	
	public void setMTRMetrialReportResult(String MTRMetrialReportResult) {
		this.getInternalObject().setMTRMetrialReportResult(MTRMetrialReportResult);
	}
	
	public String getDimensionalReportResult() {
		return this.getInternalObject().getDimensionalReportResult();
	}
	
	public void setDimensionalReportResult(String dimensionalReportResult) {
		this.getInternalObject().setDimensionalReportResult(dimensionalReportResult);
	}
	
	public String getHardnessReportResult() {
		return this.getInternalObject().getHardnessReportResult();
	}
	
	public void setHardnessReportResult(String hardnessReportResult) {
		this.getInternalObject().setHardnessReportResult(hardnessReportResult);
	}
	
	public String getVisualExaminationResult() {
		return this.getInternalObject().getVisualExaminationResult();
	}
	
	public void setVisualExaminationResult(String visualExaminationResult) {
		this.getInternalObject().setVisualExaminationResult(visualExaminationResult);
	}
	
	public String getFunctionInspectionResult() {
		return this.getInternalObject().getFunctionInspectionResult();
	}
	
	public void setFunctionInspectionResult(String functionInspectionResult) {
		this.getInternalObject().setFunctionInspectionResult(functionInspectionResult);
	}
	
	public String getMonogrammingStatusResult() {
		return this.getInternalObject().getMonogrammingStatusResult();
	}
	
	public void setMonogrammingStatusResult(String monogrammingStatusResult) {
		this.getInternalObject().setMonogrammingStatusResult(monogrammingStatusResult);
	}
	
	public String getCommentsResult() {
		return this.getInternalObject().getCommentsResult();
	}
	
	public void setCommentsResult(String commentsResult) {
		this.getInternalObject().setCommentsResult(commentsResult);
	}
	
	public String getQcConfirmUser() {
		return this.getInternalObject().getQcConfirmUser();
	}
	
	public void setQcConfirmUser(String qcConfirmUser) {
		this.getInternalObject().setQcConfirmUser(qcConfirmUser);
	}
	
	public Date getQcConfirmDate() {
		return this.getInternalObject().getQcConfirmDate();
	}
	
	public void setQcConfirmDate(Date qcConfirmDate) {
		this.getInternalObject().setQcConfirmDate(qcConfirmDate);
	}
	
	public String getQaConfirmUser() {
		return this.getInternalObject().getQaConfirmUser();
	}
	
	public void setQaConfirmUser(String qaConfirmUser) {
		this.getInternalObject().setQaConfirmUser(qaConfirmUser);
	}
	
	public Date getQaConfirmDate() {
		return this.getInternalObject().getQaConfirmDate();
	}
	
	public void setQaConfirmDate(Date qaConfirmDate) {
		this.getInternalObject().setQaConfirmDate(qaConfirmDate);
	}

	public String getReportNo() {
		return this.getInternalObject().getReportNo();
	}

	public void setReportNo(String reportNo) {
		this.getInternalObject().setReportNo(reportNo);
	}
}
