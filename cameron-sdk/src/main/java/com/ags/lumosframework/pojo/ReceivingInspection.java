package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.ReceivingInspectionEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class ReceivingInspection extends ObjectBaseImpl<ReceivingInspectionEntity>{

	private static final long serialVersionUID = 4861505241905810209L;
	
	public ReceivingInspection(ReceivingInspectionEntity entity) {
		super(entity);
	}
	
	public String getQcChecker() {
		return this.getInternalObject().getQcChecker();
	}

	public void setQcChecker(String qcChecker) {
		this.getInternalObject().setQcChecker(qcChecker);
	}

	public String getQaChecker() {
		return this.getInternalObject().getQaChecker();
	}

	public void setQaChecker(String qaChecker) {
		this.getInternalObject().setQaChecker(qaChecker);
	}

	public ReceivingInspection() {
		super(null);
	}

	@Override
	public String getName() {
		return null;
	}

	public String getPurchasingOrder() {
		return this.getInternalObject().getPurchasingOrder();
	}

	public void setPurchasingOrder(String purchasingOrder) {
		this.getInternalObject().setPurchasingOrder(purchasingOrder);
	}

	public String getPurchasingOrderItem() {
		return this.getInternalObject().getPurchasingOrderItem();
	}

	public void setPurchasingOrderItem(String purchasingOrderItem) {
		this.getInternalObject().setPurchasingOrderItem(purchasingOrderItem);
	}

	public String getSapInspectionNo() {
		return this.getInternalObject().getSapInspectionNo();
	}

	public void setSapInspectionNo(String sapInspectionNo) {
		this.getInternalObject().setSapInspectionNo(sapInspectionNo);
	}

	public String getVisualResult() {
		return this.getInternalObject().getVisualResult();
	}

	public void setVisualResult(String visualResult) {
		this.getInternalObject().setVisualResult(visualResult);
	}

	public String getHardnessResult() {
		return this.getInternalObject().getHardnessResult();
	}

	public void setHardnessResult(String hardnessResult) {
		this.getInternalObject().setHardnessResult(hardnessResult);
	}

	public String getDimensionResult() {
		return this.getInternalObject().getDimensionResult();
	}

	public void setDimensionResult(String dimensionResult) {
		this.getInternalObject().setDimensionResult(dimensionResult);
	}

	public String getTraceabilityResult() {
		return this.getInternalObject().getTraceabilityResult();
	}

	public void setTraceabilityResult(String traceabilityResult) {
		this.getInternalObject().setTraceabilityResult(traceabilityResult);
	}

	public String getDocumentResult() {
		return this.getInternalObject().getDocumentResult();
	}

	public void setDocumentResult(String documentResult) {
		this.getInternalObject().setDocumentResult(documentResult);
	}

	public String getPackingResult() {
		return this.getInternalObject().getPackingResult();
	}

	public void setPackingResult(String packingResult) {
		this.getInternalObject().setPackingResult(packingResult);
	}

	public String getCertificationResult() {
		return this.getInternalObject().getCertificationResult();
	}

	public void setCertificationResult(String certificationResult) {
		this.getInternalObject().setCertificationResult(certificationResult);
	}

	public String getOtherResult() {
		return this.getInternalObject().getOtherResult();
	}

	public void setOtherResult(String otherResult) {
		this.getInternalObject().setOtherResult(otherResult);
	}

	public String getComment() {
		return this.getInternalObject().getComment();
	}

	public void setComment(String comment) {
		this.getInternalObject().setComment(comment);
	}

	public String getQcConfirmDate() {
		return this.getInternalObject().getQcConfirmDate();
	}

	public void setQcConfirmDate(String qcConfirmDate) {
		this.getInternalObject().setQcConfirmDate(qcConfirmDate);
	}

	public String getQaConfirmDate() {
		return this.getInternalObject().getQaConfirmDate();
	}

	public void setQaConfirmDate(String qaConfirmDate) {
		this.getInternalObject().setQaConfirmDate(qaConfirmDate);
	}

	public String getApiSpec() {
		return this.getInternalObject().getApiSpec();
	}

	public void setApiSpec(String apiSpec) {
		this.getInternalObject().setApiSpec(apiSpec);
	}

	public String getPsl() {
		return this.getInternalObject().getPsl();
	}

	public void setPsl(String psl) {
		this.getInternalObject().setPsl(psl);
	}
	
	public String getSN() {
		return this.getInternalObject().getSn();
	}
	
	public void setSN(String sn) {
		this.getInternalObject().setSn(sn);
	}
}
