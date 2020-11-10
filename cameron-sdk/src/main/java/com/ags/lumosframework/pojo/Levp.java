package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.LevpEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

import java.util.Date;

public class Levp extends ObjectBaseImpl<LevpEntity> {

    public Levp(LevpEntity entity) {
        super(entity);
    }

    public Levp() {
        super(null);
    }

    @Override
    public String getName() {
        return null;
    }

    public String getProductionOrderNo() {
        return this.getInternalObject().getProductionOrderNo();
    }

    public void setProductionOrderNo(String productionOrderNo) {
        this.getInternalObject().setProductionOrderNo(productionOrderNo);
    }

    public String getQcRlt() {
        return this.getInternalObject().getQcRlt();
    }

    public void setQcRlt(String qcRlt) {
        this.getInternalObject().setQcRlt(qcRlt);
    }

    public String getQcChecker() {
        return this.getInternalObject().getQcChecker();
    }

    public void setQcChecker(String qcChecker) {
        this.getInternalObject().setQcChecker(qcChecker);
    }

    public Date getQcCheckedDate() {
        return this.getInternalObject().getQcCheckedDate();
    }

    public void setQcCheckedDate(Date qcCheckedDate) {
        this.getInternalObject().setQcCheckedDate(qcCheckedDate);
    }

    public String getWhRlt() {
        return this.getInternalObject().getWhRlt();
    }

    public void setWhRlt(String whRlt) {
        this.getInternalObject().setWhRlt(whRlt);
    }

    public String getWhChecker() {
        return this.getInternalObject().getWhChecker();
    }

    public void setWhChecker(String whChecker) {
        this.getInternalObject().setWhChecker(whChecker);
    }

    public Date getWhCheckedDate() {
        return this.getInternalObject().getWhCheckedDate();
    }

    public void setWhCheckedDate(Date whCheckedDate) {
        this.getInternalObject().setWhCheckedDate(whCheckedDate);
    }
}
