package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.SparePartEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class SparePart extends ObjectBaseImpl<SparePartEntity> {

    private static final long serialVersionUID = -5741113529479238796L;

    public SparePart() {
        super(null);
    }

    public SparePart(SparePartEntity entity) {
        super(entity);
    }

    @Override
    public String getName() {
        return null;
    }

    public String getSparePartNo() {
        return getInternalObject().getSPartNo();
    }

    public void setSparePartNo(String sparePartNo) {
        getInternalObject().setSPartNo(sparePartNo);
    }

    public String getSparePartRev() {
        return getInternalObject().getSPartRev();
    }

    public void setSparePartRev(String sparePartRev) {
        getInternalObject().setSPartRev(sparePartRev);
    }

    public String getSparePartDec() {
        return getInternalObject().getSPartDec();
    }

    public void setSparePartDec(String spareDec) {
        getInternalObject().setSPartDec(spareDec);
    }

    public String getQaPlan() {
        return getInternalObject().getQaPaln();
    }

    public void setQaPlan(String qaPlan) {
        getInternalObject().setQaPaln(qaPlan);
    }

    public String getQaPlanRev() {
        return getInternalObject().getQaPalnRev();
    }

    public void setQaPlanRev(String qaPlanRev) {
        getInternalObject().setQaPalnRev(qaPlanRev);
    }

    public String getDrawNo() {
        return getInternalObject().getDrawNo();
    }

    public void setDrawNo(String drawNo) {
        getInternalObject().setDrawNo(drawNo);
    }

    public String getPslLevelStand() {
        return getInternalObject().getPslLevelStand();
    }

    public void setPslLevelStand(String pslLevelStand) {
        getInternalObject().setPslLevelStand(pslLevelStand);
    }

    public String getApiStand() {
        return getInternalObject().getApiStand();
    }

    public void setApiStand(String apiStand) {
        getInternalObject().setApiStand(apiStand);
    }

    public String getHardnessFile() {
        return getInternalObject().getHardnessFile();
    }

    public void setHardnessFile(String hardnessFile) {
        getInternalObject().setHardnessFile(hardnessFile);
    }

    public String getPlmRev() {
        return getInternalObject().getPlmRev();
    }

    public void setPlmRev(String plmRev) {
        getInternalObject().setPlmRev(plmRev);
    }

    public String getPartRev() {
        return getInternalObject().getPartRev();
    }

    public void setPartRev(String partRev) {
        getInternalObject().setPartRev(partRev);
    }
    
    public String getLongText() {
        return getInternalObject().getLongText();
    }

    public void setLongText(String longText) {
        getInternalObject().setLongText(longText);
    }
    
    //bruce 添加追溯类型
    public boolean isRetrospect() {
        return getInternalObject().isRetrospect();
    }

    public void setRetrospect(boolean retrospect) {
        getInternalObject().setRetrospect(retrospect);
    }

    public String getRetrospectType() {
        return getInternalObject().getRetrospectType();
    }

    public void setRetrospectType(String retrospectType) {
        getInternalObject().setRetrospectType(retrospectType);
    }

    public boolean isReviewed(){return getInternalObject().isReviewed();}

    public void setReviewed(boolean reviewed){getInternalObject().setReviewed(reviewed);}

    //JY 添加D-note, coating, welding, DWG rev, MS rev
    public String getDNote() {
        return getInternalObject().getDNote();
    }

    public void setDNote(String dNote) {
        getInternalObject().setDNote(dNote);
    }

    public String getDNoteRev() {
        return getInternalObject().getDNoteRev();
    }

    public void setDNoteRev(String dNoteRev) {
        getInternalObject().setDNoteRev(dNoteRev);
    }

    public String getCoating() {
        return getInternalObject().getCoating();
    }

    public void setCoating(String coating) {
        getInternalObject().setCoating(coating);
    }

    public String getCoatingRev() {
        return getInternalObject().getCoatingRev();
    }

    public void setCoatingRev(String coatingRev) {
        getInternalObject().setCoatingRev(coatingRev);
    }

    public String getWelding() {
        return getInternalObject().getWelding();
    }

    public void setWelding(String welding) {
        getInternalObject().setWelding(welding);
    }

    public String getWeldingRev() {
        return getInternalObject().getWeldingRev();
    }

    public void setWeldingRev(String weldingRev) {
        getInternalObject().setWeldingRev(weldingRev);
    }

    public String getDrawRev() {
        return getInternalObject().getDrawRev();
    }

    public void setDrawRev(String drawRev) {
        getInternalObject().setDrawRev(drawRev);
    }

    public String getHardnessRev() {
        return getInternalObject().getHardnessRev();
    }

    public void setHardnessRev(String hardnessRev) {
        getInternalObject().setHardnessRev(hardnessRev);
    }
}
