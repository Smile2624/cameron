package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.AssemblingTempEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class AssemblingTemp extends ObjectBaseImpl<AssemblingTempEntity> {

    private static final long serialVersionUID = -2995006622693568840L;

    public AssemblingTemp() {
        super(null);
    }

    public AssemblingTemp(AssemblingTempEntity entity) {
        super(entity);
    }

    @Override
    public String getName() {
        return null;
    }

    public String getOrderNo() {
        return getInternalObject().getOrderNo();
    }

    public void setOrderNo(String orderNo) {
        getInternalObject().setOrderNo(orderNo);
    }

    public String getSnBatch() {
        return getInternalObject().getSnBatch();
    }

    public void setSnBatch(String snBatch) {
        getInternalObject().setSnBatch(snBatch);
    }

    public int getxValue() {
        return getInternalObject().getXValue();
    }

    public void setxValue(int xValue) {
        getInternalObject().setXValue(xValue);
    }

    public int getyValue() {
        return getInternalObject().getYValue();
    }

    public void setyValue(int yValue) {
        getInternalObject().setYValue(yValue);
    }

    public int getCountNo() {
        return getInternalObject().getCountNo();
    }

    public void setCountNo(int countNo) {
        getInternalObject().setCountNo(countNo);
    }

    public boolean isSingleOrBatch() {
        return getInternalObject().isSingleOrBatch();
    }

    public void setSingleOrBatch(boolean singleOrBatch) {
        getInternalObject().setSingleOrBatch(singleOrBatch);
    }

    public String getCheckType() {
        return getInternalObject().getCheckType();
    }

    public void setCheckType(String checkType) {
        getInternalObject().setCheckType(checkType);
    }
}
