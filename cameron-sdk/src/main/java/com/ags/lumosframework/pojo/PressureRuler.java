package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.PressureRulerEntity;
import com.ags.lumosframework.enums.PressureTypeEnum;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class PressureRuler extends ObjectBaseImpl<PressureRulerEntity> {

    private static final long serialVersionUID = -2105894028226861460L;

    public PressureRuler(PressureRulerEntity entity) {
        super(entity);
    }

    public PressureRuler() {
        super(null);
    }

    @Override
    public String getName() {
        return null;
    }

    public String getRulerNo() {
        return this.getInternalObject().getRulerNo();
    }

    public void setRulerNo(String rulerNo) {
        this.getInternalObject().setRulerNo(rulerNo);
    }

    public String getSuffixNo() {
        return this.getInternalObject().getSuffixNo();
    }

    public void setSuffixNo(String suffixNo) {
        this.getInternalObject().setSuffixNo(suffixNo);
    }

    public int getInspectionTimes() {
        return this.getInternalObject().getInspectionTimes();
    }

    public void setInspectionTimes(int inspectionTimes) {
        this.getInternalObject().setInspectionTimes(inspectionTimes);
    }

    public double getTime() {
        return this.getInternalObject().getTime();
    }

    public void setTime(double time) {
        this.getInternalObject().setTime(time);
    }

    public double getTime1() {
        return this.getInternalObject().getTime1();
    }

    public void setTime1(double time) {
        this.getInternalObject().setTime1(time);
    }

    public double getTime2() {
        return this.getInternalObject().getTime2();
    }

    public void setTime2(double time) {
        this.getInternalObject().setTime2(time);
    }

    public int getUpperStreamInspectionTimes() {
        return this.getInternalObject().getUpperStreamInspectionTimes();
    }

    public void setUpperStreamInspectionTimes(int upperStreamInspectionTimes) {
        this.getInternalObject().setUpperStreamInspectionTimes(upperStreamInspectionTimes);
    }

    public double getUpperStreamTime() {
        return this.getInternalObject().getUpperStreamTime();
    }

    public void setUpperStreamTime(double upperStreamTime) {
        this.getInternalObject().setUpperStreamTime(upperStreamTime);
    }

    public double getUpperStreamTime1() {
        return this.getInternalObject().getUpperStreamTime1();
    }

    public void setUpperStreamTime1(double upperStreamTime) {
        this.getInternalObject().setUpperStreamTime1(upperStreamTime);
    }

    public double getUpperStreamTime2() {
        return this.getInternalObject().getUpperStreamTime2();
    }

    public void setUpperStreamTime2(double upperStreamTime) {
        this.getInternalObject().setUpperStreamTime2(upperStreamTime);
    }

    public int getDownStreamInspectionTimes() {
        return this.getInternalObject().getDownStreamInspectionTimes();
    }

    public void setDownStreamInspectionTimes(int downStreamInspectionTimes) {
        this.getInternalObject().setDownStreamInspectionTimes(downStreamInspectionTimes);
    }

    public double getDownStreamTime() {
        return this.getInternalObject().getDownStreamTime();
    }

    public void setDownStreamTime(double downStreamTime) {
        this.getInternalObject().setDownStreamTime(downStreamTime);
    }

    public double getDownStreamTime1() {
        return this.getInternalObject().getDownStreamTime1();
    }

    public void setDownStreamTime1(double downStreamTime) {
        this.getInternalObject().setDownStreamTime1(downStreamTime);
    }

    public double getDownStreamTime2() {
        return this.getInternalObject().getDownStreamTime2();
    }

    public void setDownStreamTime2(double downStreamTime) {
        this.getInternalObject().setDownStreamTime2(downStreamTime);
    }

    public String getProductNo() {
        return this.getInternalObject().getProductNo();
    }

    public void setProductNo(String productNo) {
        this.getInternalObject().setProductNo(productNo);
    }

    public String getPressureType() {
        return this.getInternalObject().getPressureType();
    }
    public PressureTypeEnum getPressureTypeEnum() {
        return PressureTypeEnum.getValue(getPressureType());
    }

    public void setPressureType(PressureTypeEnum pressureType) {
        this.getInternalObject().setPressureType(pressureType.getKey());
    }

    public double getTestPressureValue() {
        return this.getInternalObject().getTestPressureValue();
    }

    public void setTestPressureValue(double testPressureValue) {
        this.getInternalObject().setTestPressureValue(testPressureValue);
    }

    public double getMaxPressureValue() {
        return this.getInternalObject().getMaxPressureValue();
    }

    public void setMaxPressureValue(double maxPressureValue) {
        this.getInternalObject().setMaxPressureValue(maxPressureValue);
    }

    public double getDifferencePressureValue() {
        return this.getInternalObject().getDifferencePressureValue();
    }

    public void setDifferencePressureValue(double differencePressureValue) {
        this.getInternalObject().setDifferencePressureValue(differencePressureValue);
    }

    public double getFirstTime() {
        return this.getInternalObject().getFirstTime();
    }

    public void setFirstTime(double firstTime) {
        this.getInternalObject().setFirstTime(firstTime);
    }

    public double getSecondTime() {
        return this.getInternalObject().getSecondTime();
    }

    public void setSecondTime(double secondTime) {
        this.getInternalObject().setSecondTime(secondTime);
    }

    public double getThirdTime() {
        return this.getInternalObject().getThirdTime();
    }

    public void setThirdTime(double thirdTime) {
        this.getInternalObject().setThirdTime(thirdTime);
    }

    public double getTorqueValue() {
        return this.getInternalObject().getTorqueValue();
    }

    public void setTorqueValue(double torqueValue) {
        this.getInternalObject().setTorqueValue(torqueValue);
    }

}
