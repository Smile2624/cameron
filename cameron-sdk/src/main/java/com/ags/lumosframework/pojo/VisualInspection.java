package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.VisualInspectionEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class VisualInspection extends ObjectBaseImpl<VisualInspectionEntity> {

    private static final long serialVersionUID = 1L;

    public VisualInspection(VisualInspectionEntity entity) {
        super(entity);
    }

    public String getQcChecker() {
        return this.getInternalObject().getQcChecker();
    }

    public void setQcChecker(String qcChecker) {
        this.getInternalObject().setQcChecker(qcChecker);
    }

    public VisualInspection() {
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

    public String getPackingResult() {
        return this.getInternalObject().getPackingResult();
    }

    public void setPackingResult(String packingResult) {
        this.getInternalObject().setPackingResult(packingResult);
    }

    public String getRawMaterialResult() {
        return this.getInternalObject().getRawMaterialResult();
    }

    public void setRawMaterialResult(String rawMaterialResult) {
        this.getInternalObject().setRawMaterialResult(rawMaterialResult);
    }

    public String getMachinedSurfaceResult() {
        return this.getInternalObject().getMachinedSurfaceResult();
    }

    public void setMachinedSurfaceResult(String machinedSurfaceResult) {
        this.getInternalObject().setMachinedSurfaceResult(machinedSurfaceResult);
    }

    public String getNonmetalPartsResult() {
        return this.getInternalObject().getNonmetalPartsResult();
    }

    public void setNonmetalPartsResult(String nonmetalPartsResult) {
        this.getInternalObject().setNonmetalPartsResult(nonmetalPartsResult);
    }

    public String getCoatingResult() {
        return this.getInternalObject().getCoatingResult();
    }

    public void setCoatingResult(String coatingResult) {
        this.getInternalObject().setCoatingResult(coatingResult);
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

    public String getQcWitness() {
        return this.getInternalObject().getQcWitness();
    }

    public void setQcWitness(String qcWitness) {
        this.getInternalObject().setQcWitness(qcWitness);
    }
}
