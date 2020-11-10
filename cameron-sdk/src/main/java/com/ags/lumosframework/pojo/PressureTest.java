//
// Decompiled by Procyon v0.5.36
//

package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.PressureTestEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

import java.util.Date;

public class PressureTest extends ObjectBaseImpl<PressureTestEntity> {
    private static final long serialVersionUID = -7877658748296655324L;

    public PressureTest() {
        super(null);
    }

    public PressureTest(final PressureTestEntity entity) {
        super(entity);
    }

    @Override
    public String getName() {
        return null;
    }

    public String getProductSN() {
        return this.getInternalObject().getProductSN();
    }

    public void setProductSN(final String productSN) {
        this.getInternalObject().setProductSN(productSN);
    }

    public String getPartNo() {
        return this.getInternalObject().getPartNo();
    }

    public void setPartNo(final String partNo) {
        this.getInternalObject().setPartNo(partNo);
    }

    public String getPartRev() {
        return this.getInternalObject().getPartRev();
    }

    public void setPartRev(final String partRev) {
        this.getInternalObject().setPartRev(partRev);
    }

    public String getStationName() {
        return this.getInternalObject().getStationName();
    }

    public void setStationName(final String stationName) {
        this.getInternalObject().setStationName(stationName);
    }

    public String getStationEpuNo() {
        return this.getInternalObject().getStationEpuNo();
    }

    public void setStationEpuNo(final String stationEpuNo) {
        this.getInternalObject().setStationEpuNo(stationEpuNo);
    }

    public String getGageNoSize() {
        return this.getInternalObject().getGageNoSize();
    }

    public void setGageNoSize(final String gageNoSize) {
        this.getInternalObject().setGageNoSize(gageNoSize);
    }

    public boolean getBlowDownTest() {
        return this.getInternalObject().isBlowDownTest();
    }

    public void setBlowDownTest(final boolean blowDownTest) {
        this.getInternalObject().setBlowDownTest(blowDownTest);
    }

    public boolean getDriftTest() {
        return this.getInternalObject().isDriftTest();
    }

    public void setDriftTest(final boolean driftTest) {
        this.getInternalObject().setDriftTest(driftTest);
    }

    public String getTestBy() {
        return this.getInternalObject().getTestBy();
    }

    public void setTestBy(final String testBy) {
        this.getInternalObject().setTestBy(testBy);
    }

    public String getTestDate() {
        return this.getInternalObject().getTestDate();
    }

    public void setTestDate(final String testDate) {
        this.getInternalObject().setTestDate(testDate);
    }

    public String getWorkOrder() {
        return this.getInternalObject().getWorkOrder();
    }

    public void setWorkOrder(final String workOrder) {
        this.getInternalObject().setWorkOrder(workOrder);
    }

    public String getTestTableName() {
        return this.getInternalObject().getTestTableName();
    }

    public void setTestTableName(final String testTableName) {
        this.getInternalObject().setTestTableName(testTableName);
    }

    public String getTestResult() {
        return this.getInternalObject().getTestResult();
    }

    public void setTestResult(final String testResult) {
        this.getInternalObject().setTestResult(testResult);
    }

    public String getTestType() {
        return this.getInternalObject().getTestType();
    }

    public void setTestType(final String testType) {
        this.getInternalObject().setTestType(testType);
    }

    public String getDriftTestBy() {
        return this.getInternalObject().getDriftTestBy();
    }

    public void setDriftTestBy(final String driftTestBy) {
        this.getInternalObject().setDriftTestBy(driftTestBy);
    }

    public String getTorqueValue1() {
        return this.getInternalObject().getTorqueValue1();
    }

    public void setTorqueValue1(final String torqueValue1) {
        this.getInternalObject().setTorqueValue1(torqueValue1);
    }

    public String getTorqueValue2() {
        return this.getInternalObject().getTorqueValue2();
    }

    public void setTorqueValue2(final String torqueValue2) {
        this.getInternalObject().setTorqueValue2(torqueValue2);
    }

    public String getTorqueValue3() {
        return this.getInternalObject().getTorqueValue3();
    }

    public void setTorqueValue3(final String torqueValue3) {
        this.getInternalObject().setTorqueValue3(torqueValue3);
    }

    public String getTorqueValue4() {
        return this.getInternalObject().getTorqueValue4();
    }

    public void setTorqueValue4(final String torqueValue4) {
        this.getInternalObject().setTorqueValue4(torqueValue4);
    }

    public String getProcedureNo() {
        return this.getInternalObject().getProcedureNo();
    }

    public void setProcedureNo(final String procedureNo) {
        this.getInternalObject().setProcedureNo(procedureNo);
    }

    public boolean isTorqueDriftSaved() {
        return this.getInternalObject().isTorqueDriftSaved();
    }

    public void setTorqueDriftSaved(final boolean torqueDriftSaved) {
        this.getInternalObject().setTorqueDriftSaved(torqueDriftSaved);
    }


    public String getTorqueSensorNo() {
        return this.getInternalObject().getTorqueSensorNo();
    }

    public void setTorqueSensorNo(String torqueSensorNo) {
        this.getInternalObject().setTorqueSensorNo(torqueSensorNo);
    }

    //fm
    public boolean isFmOpenBodyFlag() {
        return this.getInternalObject().isFmOpenBodyFlag();
    }

    public void setFmOpenBodyFlag(boolean fmOpenBodyFlag) {
        this.getInternalObject().setFmOpenBodyFlag(fmOpenBodyFlag);
    }

    public String getFmOpenBodyStationName() {
        return this.getInternalObject().getFmOpenBodyStationName();
    }

    public void setFmOpenBodyStationName(String fmOpenBodyStationName) {
        this.getInternalObject().setFmOpenBodyStationName(fmOpenBodyStationName);
    }

    public String getFmOpenBodyStationNo() {
        return this.getInternalObject().getFmOpenBodyStationNo();
    }

    public void setFmOpenBodyStationNo(String fmOpenBodyStationNo) {
        this.getInternalObject().setFmOpenBodyStationNo(fmOpenBodyStationNo);
    }

    public String getFmOpenBodyRecord() {
        return this.getInternalObject().getFmOpenBodyRecord();
    }

    public void setFmOpenBodyRecord(String fmOpenBodyRecord) {
        this.getInternalObject().setFmOpenBodyRecord(fmOpenBodyRecord);
    }

    public double getFmOpenP1() {
        return this.getInternalObject().getFmOpenP1();
    }

    public void setFmOpenP1(double fmOpenP1) {
        this.getInternalObject().setFmOpenP1(fmOpenP1);
    }

    public double getFmOpenP2() {
        return this.getInternalObject().getFmOpenP2();
    }

    public void setFmOpenP2(double fmOpenP2) {
        this.getInternalObject().setFmOpenP2(fmOpenP2);
    }

    public double getFmOpenP3() {
        return this.getInternalObject().getFmOpenP3();
    }

    public void setFmOpenP3(double fmOpenP3) {
        this.getInternalObject().setFmOpenP3(fmOpenP3);
    }

    public double getFmOpenT1() {
        return this.getInternalObject().getFmOpenT1();
    }

    public void setFmOpenT1(double fmOpenT1) {
        this.getInternalObject().setFmOpenT1(fmOpenT1);
    }

    public double getFmOpenT2() {
        return this.getInternalObject().getFmOpenT2();
    }

    public void setFmOpenT2(double fmOpenT2) {
        this.getInternalObject().setFmOpenT2(fmOpenT2);
    }

    public double getFmOpenT3() {
        return this.getInternalObject().getFmOpenT3();
    }

    public void setFmOpenT3(double fmOpenT3) {
        this.getInternalObject().setFmOpenT3(fmOpenT3);
    }

    public String getFmOpenResult() {
        return this.getInternalObject().getFmOpenResult();
    }

    public void setFmOpenResult(String fmOpenResult) {
        this.getInternalObject().setFmOpenResult(fmOpenResult);
    }

    public String getFmOpenUser() {
        return this.getInternalObject().getFmOpenUser();
    }

    public void setFmOpenUser(String fmOpenUser) {
        this.getInternalObject().setFmOpenUser(fmOpenUser);
    }

    public String getFmOpenDate() {
        return this.getInternalObject().getFmOpenDate();
    }

    public void setFmOpenDate(String fmOpenDate) {
        this.getInternalObject().setFmOpenDate(fmOpenDate);
    }

    public boolean isFmDownFlag() {
        return this.getInternalObject().isFmDownFlag();
    }

    public void setFmDownFlag(boolean fmDownFlag) {
        this.getInternalObject().setFmDownFlag(fmDownFlag);
    }

    public String getFmDownStationName() {
        return this.getInternalObject().getFmDownStationName();
    }

    public void setFmDownStationName(String fmDownStationName) {
        this.getInternalObject().setFmDownStationName(fmDownStationName);
    }

    public String getFmDownStationNo() {
        return this.getInternalObject().getFmDownStationNo();
    }

    public void setFmDownStationNo(String fmDownStationNo) {
        this.getInternalObject().setFmDownStationNo(fmDownStationNo);
    }

    public String getFmDownRecord() {
        return this.getInternalObject().getFmDownRecord();
    }

    public void setFmDownRecord(String fmDownRecord) {
        this.getInternalObject().setFmDownRecord(fmDownRecord);
    }

    public double getFmDownP1() {
        return this.getInternalObject().getFmDownP1();
    }

    public void setFmDownP1(double fmDownP1) {
        this.getInternalObject().setFmDownP1(fmDownP1);
    }

    public double getFmDownP2() {
        return this.getInternalObject().getFmDownP2();
    }

    public void setFmDownP2(double fmDownP2) {
        this.getInternalObject().setFmDownP2(fmDownP2);
    }

    public double getFmDownP3() {
        return this.getInternalObject().getFmDownP3();
    }

    public void setFmDownP3(double fmDownP3) {
        this.getInternalObject().setFmDownP3(fmDownP3);
    }

    public double getFmDownT1() {
        return this.getInternalObject().getFmDownT1();
    }

    public void setFmDownT1(double fmDownT1) {
        this.getInternalObject().setFmDownT1(fmDownT1);
    }

    public double getFmDownT2() {
        return this.getInternalObject().getFmDownT2();
    }

    public void setFmDownT2(double fmDownT2) {
        this.getInternalObject().setFmDownT2(fmDownT2);
    }

    public double getFmDownT3() {
        return this.getInternalObject().getFmDownT3();
    }

    public void setFmDownT3(double fmDownT3) {
        this.getInternalObject().setFmDownT3(fmDownT3);
    }

    public String getFmDownResult() {
        return this.getInternalObject().getFmDownResult();
    }

    public void setFmDownResult(String fmDownResult) {
        this.getInternalObject().setFmDownResult(fmDownResult);
    }

    public String getFmDownUser() {
        return this.getInternalObject().getFmDownUser();
    }

    public void setFmDownUser(String fmDownUser) {
        this.getInternalObject().setFmDownUser(fmDownUser);
    }

    public String getFmDownDate() {
        return this.getInternalObject().getFmDownDate();
    }

    public void setFmDownDate(String fmDownDate) {
        this.getInternalObject().setFmDownDate(fmDownDate);
    }

    public boolean isFmUpFlag() {
        return this.getInternalObject().isFmUpFlag();
    }

    public void setFmUpFlag(boolean fmUpFlag) {
        this.getInternalObject().setFmUpFlag(fmUpFlag);
    }

    public String getFmUpStationName() {
        return this.getInternalObject().getFmUpStationName();
    }

    public void setFmUpStationName(String fmUpStationName) {
        this.getInternalObject().setFmUpStationName(fmUpStationName);
    }

    public String getFmUpStationNo() {
        return this.getInternalObject().getFmUpStationNo();
    }

    public void setFmUpStationNo(String fmUpStationNo) {
        this.getInternalObject().setFmUpStationNo(fmUpStationNo);
    }

    public String getFmUpRecord() {
        return this.getInternalObject().getFmUpRecord();
    }

    public void setFmUpRecord(String fmUpRecord) {
        this.getInternalObject().setFmUpRecord(fmUpRecord);
    }

    public double getFmUpP1() {
        return this.getInternalObject().getFmUpP1();
    }

    public void setFmUpP1(double fmUpP1) {
        this.getInternalObject().setFmUpP1(fmUpP1);
    }

    public double getFmUpP2() {
        return this.getInternalObject().getFmUpP2();
    }

    public void setFmUpP2(double fmUpP2) {
        this.getInternalObject().setFmUpP2(fmUpP2);
    }

    public double getFmUpP3() {
        return this.getInternalObject().getFmUpP3();
    }

    public void setFmUpP3(double fmUpP3) {
        this.getInternalObject().setFmUpP3(fmUpP3);
    }

    public double getFmUpT1() {
        return this.getInternalObject().getFmUpT1();
    }

    public void setFmUpT1(double fmUpT1) {
        this.getInternalObject().setFmUpT1(fmUpT1);
    }

    public double getFmUpT2() {
        return this.getInternalObject().getFmUpT2();
    }

    public void setFmUpT2(double fmUpT2) {
        this.getInternalObject().setFmUpT2(fmUpT2);
    }

    public double getFmUpT3() {
        return this.getInternalObject().getFmUpT3();
    }

    public void setFmUpT3(double fmUpT3) {
        this.getInternalObject().setFmUpT3(fmUpT3);
    }

    public String getFmUpResult() {
        return this.getInternalObject().getFmUpResult();
    }

    public void setFmUpResult(String fmUpResult) {
        this.getInternalObject().setFmUpResult(fmUpResult);
    }

    public String getFmUpUser() {
        return this.getInternalObject().getFmUpUser();
    }

    public void setFmUpUser(String fmUpUser) {
        this.getInternalObject().setFmUpUser(fmUpUser);
    }

    public String getFmUpDate() {
        return this.getInternalObject().getFmUpDate();
    }

    public void setFmUpDate(String fmUpDate) {
        this.getInternalObject().setFmUpDate(fmUpDate);
    }
    //jk

    public boolean isJkOpenBodyFlag() {
        return this.getInternalObject().isJkOpenBodyFlag();
    }

    public void setJkOpenBodyFlag(boolean jkOpenBodyFlag) {
        this.getInternalObject().setJkOpenBodyFlag(jkOpenBodyFlag);
    }

    public String getJkOpenBodyStationName() {
        return this.getInternalObject().getJkOpenBodyStationName();
    }

    public void setJkOpenBodyStationName(String jkOpenBodyStationName) {
        this.getInternalObject().setJkOpenBodyStationName(jkOpenBodyStationName);
    }

    public String getJkOpenBodyStationNo() {
        return this.getInternalObject().getJkOpenBodyStationNo();
    }

    public void setJkOpenBodyStationNo(String jkOpenBodyStationNo) {
        this.getInternalObject().setJkOpenBodyStationNo(jkOpenBodyStationNo);
    }

    public String getJkOpenBodyRecord() {
        return this.getInternalObject().getJkOpenBodyRecord();
    }

    public void setJkOpenBodyRecord(String jkOpenBodyRecord) {
        this.getInternalObject().setJkOpenBodyRecord(jkOpenBodyRecord);
    }

    public double getJkOpenP1() {
        return this.getInternalObject().getJkOpenP1();
    }

    public void setJkOpenP1(double jkOpenP1) {
        this.getInternalObject().setJkOpenP1(jkOpenP1);
    }

    public double getJkOpenP2() {
        return this.getInternalObject().getJkOpenP2();
    }

    public void setJkOpenP2(double jkOpenP2) {
        this.getInternalObject().setJkOpenP2(jkOpenP2);
    }

    public double getJkOpenP3() {
        return this.getInternalObject().getJkOpenP3();
    }

    public void setJkOpenP3(double jkOpenP3) {
        this.getInternalObject().setJkOpenP3(jkOpenP3);
    }

    public double getJkOpenT1() {
        return this.getInternalObject().getJkOpenT1();
    }

    public void setJkOpenT1(double jkOpenT1) {
        this.getInternalObject().setJkOpenT1(jkOpenT1);
    }

    public double getJkOpenT2() {
        return this.getInternalObject().getJkOpenT2();
    }

    public void setJkOpenT2(double jkOpenT2) {
        this.getInternalObject().setJkOpenT2(jkOpenT2);
    }

    public double getJkOpenT3() {
        return this.getInternalObject().getJkOpenT3();
    }

    public void setJkOpenT3(double jkOpenT3) {
        this.getInternalObject().setJkOpenT3(jkOpenT3);
    }

    public String getJkOpenR1() {
        return this.getInternalObject().getJkOpenR1();
    }

    public void setJkOpenR1(String jkOpenR1) {
        this.getInternalObject().setJkOpenR1(jkOpenR1);
    }

    public String getJkOpenR2() {
        return this.getInternalObject().getJkOpenR2();
    }

    public void setJkOpenR2(String jkOpenR2) {
        this.getInternalObject().setJkOpenR2(jkOpenR2);
    }

    public String getJkOpenR3() {
        return this.getInternalObject().getJkOpenR3();
    }

    public void setJkOpenR3(String jkOpenR3) {
        this.getInternalObject().setJkOpenR3(jkOpenR3);
    }

    public String getJkOpenUser() {
        return this.getInternalObject().getJkOpenUser();
    }

    public void setJkOpenUser(String jkOpenUser) {
        this.getInternalObject().setJkOpenUser(jkOpenUser);
    }

    public String getJkOpenDate() {
        return this.getInternalObject().getJkOpenDate();
    }

    public void setJkOpenDate(String jkOpenDate) {
        this.getInternalObject().setJkOpenDate(jkOpenDate);
    }

    public boolean isJkCrossoverBodyFlag() {
        return this.getInternalObject().isJkCrossoverBodyFlag();
    }

    public void setJkCrossoverBodyFlag(boolean jkCrossoverBodyFlag) {
        this.getInternalObject().setJkCrossoverBodyFlag(jkCrossoverBodyFlag);
    }

    public String getJkCrossoverBodyTopStationName() {
        return this.getInternalObject().getJkCrossoverBodyTopStationName();
    }

    public void setJkCrossoverBodyTopStationName(String jkCrossoverBodyTopStationName) {
        this.getInternalObject().setJkCrossoverBodyTopStationName(jkCrossoverBodyTopStationName);
    }

    public String getJkCrossoverBodyTopStationNo() {
        return this.getInternalObject().getJkCrossoverBodyTopStationNo();
    }

    public void setJkCrossoverBodyTopStationNo(String jkCrossoverBodyTopStationNo) {
        this.getInternalObject().setJkCrossoverBodyTopStationNo(jkCrossoverBodyTopStationNo);
    }



    public String getJkCrossoverBodyBtmStationName() {
        return this.getInternalObject().getJkCrossoverBodyBtmStationName();
    }

    public void setJkCrossoverBodyBtmStationName(String jkCrossoverBodyBtmStationName) {
        this.getInternalObject().setJkCrossoverBodyBtmStationName(jkCrossoverBodyBtmStationName);
    }

    public String getJkCrossoverBodyBtmStationNo() {
        return this.getInternalObject().getJkCrossoverBodyBtmStationNo();
    }

    public void setJkCrossoverBodyBtmStationNo(String jkCrossoverBodyBtmStationNo) {
        this.getInternalObject().setJkCrossoverBodyBtmStationNo(jkCrossoverBodyBtmStationNo);
    }
    public String getJkCrossoverBodyTopRecord() {
        return this.getInternalObject().getJkCrossoverBodyTopRecord();
    }

    public void setJkCrossoverBodyTopRecord(String jkCrossoverBodyTopRecord) {
        this.getInternalObject().setJkCrossoverBodyTopRecord(jkCrossoverBodyTopRecord);
    }

    public String getJkCrossoverBodyBtmRecord() {
        return this.getInternalObject().getJkCrossoverBodyBtmRecord();
    }

    public void setJkCrossoverBodyBtmRecord(String jkCrossoverBodyBtmRecord) {
        this.getInternalObject().setJkCrossoverBodyBtmRecord(jkCrossoverBodyBtmRecord);
    }

    public double getJkCrossoverBodyTopP1() {
        return this.getInternalObject().getJkCrossoverBodyTopP1();
    }

    public void setJkCrossoverBodyTopP1(double jkCrossoverBodyTopP1) {
        this.getInternalObject().setJkCrossoverBodyTopP1(jkCrossoverBodyTopP1);
    }

    public double getJkCrossoverBodyTopP2() {
        return this.getInternalObject().getJkCrossoverBodyTopP2();
    }

    public void setJkCrossoverBodyTopP2(double jkCrossoverBodyTopP2) {
        this.getInternalObject().setJkCrossoverBodyTopP2(jkCrossoverBodyTopP2);
    }

    public double getJkCrossoverBodyTopP3() {
        return this.getInternalObject().getJkCrossoverBodyTopP3();
    }

    public void setJkCrossoverBodyTopP3(double jkCrossoverBodyTopP3) {
        this.getInternalObject().setJkCrossoverBodyTopP3(jkCrossoverBodyTopP3);
    }

    public double getJkCrossoverBodyTopT1() {
        return this.getInternalObject().getJkCrossoverBodyTopT1();
    }

    public void setJkCrossoverBodyTopT1(double jkCrossoverBodyTopT1) {
        this.getInternalObject().setJkCrossoverBodyTopT1(jkCrossoverBodyTopT1);
    }

    public double getJkCrossoverBodyTopT2() {
        return this.getInternalObject().getJkCrossoverBodyTopT2();
    }

    public void setJkCrossoverBodyTopT2(double jkCrossoverBodyTopT2) {
        this.getInternalObject().setJkCrossoverBodyTopT2(jkCrossoverBodyTopT2);
    }

    public double getJkCrossoverBodyTopT3() {
        return this.getInternalObject().getJkCrossoverBodyTopT3();
    }

    public void setJkCrossoverBodyTopT3(double jkCrossoverBodyTopT3) {
        this.getInternalObject().setJkCrossoverBodyTopT3(jkCrossoverBodyTopT3);
    }

    public String getJkCrossoverBodyTopR1() {
        return this.getInternalObject().getJkCrossoverBodyTopR1();
    }

    public void setJkCrossoverBodyTopR1(String jkCrossoverBodyTopR1) {
        this.getInternalObject().setJkCrossoverBodyTopR1(jkCrossoverBodyTopR1);
    }

    public String getJkCrossoverBodyTopR2() {
        return this.getInternalObject().getJkCrossoverBodyTopR2();
    }

    public void setJkCrossoverBodyTopR2(String jkCrossoverBodyTopR2) {
        this.getInternalObject().setJkCrossoverBodyTopR2(jkCrossoverBodyTopR2);
    }

    public String getJkCrossoverBodyTopR3() {
        return this.getInternalObject().getJkCrossoverBodyTopR3();
    }

    public void setJkCrossoverBodyTopR3(String jkCrossoverBodyTopR3) {
        this.getInternalObject().setJkCrossoverBodyTopR3(jkCrossoverBodyTopR3);
    }

    public double getJkCrossoverBodyBtmP1() {
        return this.getInternalObject().getJkCrossoverBodyBtmP1();
    }

    public void setJkCrossoverBodyBtmP1(double jkCrossoverBodyBtmP1) {
        this.getInternalObject().setJkCrossoverBodyBtmP1(jkCrossoverBodyBtmP1);
    }

    public double getJkCrossoverBodyBtmP2() {
        return this.getInternalObject().getJkCrossoverBodyBtmP2();
    }

    public void setJkCrossoverBodyBtmP2(double jkCrossoverBodyBtmP2) {
        this.getInternalObject().setJkCrossoverBodyBtmP2(jkCrossoverBodyBtmP2);
    }

    public double getJkCrossoverBodyBtmP3() {
        return this.getInternalObject().getJkCrossoverBodyBtmP3();
    }

    public void setJkCrossoverBodyBtmP3(double jkCrossoverBodyBtmP3) {
        this.getInternalObject().setJkCrossoverBodyBtmP3(jkCrossoverBodyBtmP3);
    }

    public double getJkCrossoverBodyBtmT1() {
        return this.getInternalObject().getJkCrossoverBodyBtmT1();
    }

    public void setJkCrossoverBodyBtmT1(double jkCrossoverBodyBtmT1) {
        this.getInternalObject().setJkCrossoverBodyBtmT1(jkCrossoverBodyBtmT1);
    }

    public double getJkCrossoverBodyBtmT2() {
        return this.getInternalObject().getJkCrossoverBodyBtmT2();
    }

    public void setJkCrossoverBodyBtmT2(double jkCrossoverBodyBtmT2) {
        this.getInternalObject().setJkCrossoverBodyBtmT2(jkCrossoverBodyBtmT2);
    }

    public double getJkCrossoverBodyBtmT3() {
        return this.getInternalObject().getJkCrossoverBodyBtmT3();
    }

    public void setJkCrossoverBodyBtmT3(double jkCrossoverBodyBtmT3) {
        this.getInternalObject().setJkCrossoverBodyBtmT3(jkCrossoverBodyBtmT3);
    }

    public String getJkCrossoverBodyBtmR1() {
        return this.getInternalObject().getJkCrossoverBodyBtmR1();
    }

    public void setJkCrossoverBodyBtmR1(String jkCrossoverBodyBtmR1) {
        this.getInternalObject().setJkCrossoverBodyBtmR1(jkCrossoverBodyBtmR1);
    }

    public String getJkCrossoverBodyBtmR2() {
        return this.getInternalObject().getJkCrossoverBodyBtmR2();
    }

    public void setJkCrossoverBodyBtmR2(String jkCrossoverBodyBtmR2) {
        this.getInternalObject().setJkCrossoverBodyBtmR2(jkCrossoverBodyBtmR2);
    }

    public String getJkCrossoverBodyBtmR3() {
        return this.getInternalObject().getJkCrossoverBodyBtmR3();
    }

    public void setJkCrossoverBodyBtmR3(String jkCrossoverBodyBtmR3) {
        this.getInternalObject().setJkCrossoverBodyBtmR3(jkCrossoverBodyBtmR3);
    }

    public String getJkCrossoverBodyUser() {
        return this.getInternalObject().getJkCrossoverBodyUser();
    }

    public void setJkCrossoverBodyUser(String jkCrossoverBodyUser) {
        this.getInternalObject().setJkCrossoverBodyUser(jkCrossoverBodyUser);
    }

    public String getJkCrossoverBodyDate() {
        return this.getInternalObject().getJkCrossoverBodyDate();
    }

    public void setJkCrossoverBodyDate(String jkCrossoverBodyDate) {
        this.getInternalObject().setJkCrossoverBodyDate(jkCrossoverBodyDate);
    }

    public boolean isJkChristmasTreeFlag() {
        return this.getInternalObject().isJkChristmasTreeFlag();
    }

    public void setJkChristmasTreeFlag(boolean jkChristmasTreeFlag) {
        this.getInternalObject().setJkChristmasTreeFlag(jkChristmasTreeFlag);
    }

    public String getJkChristmasTreeStationName() {
        return this.getInternalObject().getJkChristmasTreeStationName();
    }

    public void setJkChristmasTreeStationName(String jkChristmasTreeStationName) {
        this.getInternalObject().setJkChristmasTreeStationName(jkChristmasTreeStationName);
    }

    public String getJkChristmasTreeStationNo() {
        return this.getInternalObject().getJkChristmasTreeStationNo();
    }

    public void setJkChristmasTreeStationNo(String jkChristmasTreeStationNo) {
        this.getInternalObject().setJkChristmasTreeStationNo(jkChristmasTreeStationNo);
    }

    public String getJkChristmasTreeRecord() {
        return this.getInternalObject().getJkChristmasTreeRecord();
    }

    public void setJkChristmasTreeRecord(String jkChristmasTreeRecord) {
        this.getInternalObject().setJkChristmasTreeRecord(jkChristmasTreeRecord);
    }

    public double getJkChristmasTreeP1() {
        return this.getInternalObject().getJkChristmasTreeP1();
    }

    public void setJkChristmasTreeP1(double jkChristmasTreeP1) {
        this.getInternalObject().setJkChristmasTreeP1(jkChristmasTreeP1);
    }

    public double getJkChristmasTreeP2() {
        return this.getInternalObject().getJkChristmasTreeP2();
    }

    public void setJkChristmasTreeP2(double jkChristmasTreeP2) {
        this.getInternalObject().setJkChristmasTreeP2(jkChristmasTreeP2);
    }

    public double getJkChristmasTreeP3() {
        return this.getInternalObject().getJkChristmasTreeP3();
    }

    public void setJkChristmasTreeP3(double jkChristmasTreeP3) {
        this.getInternalObject().setJkChristmasTreeP3(jkChristmasTreeP3);
    }

    public double getJkChristmasTreeT1() {
        return this.getInternalObject().getJkChristmasTreeT1();
    }

    public void setJkChristmasTreeT1(double jkChristmasTreeT1) {
        this.getInternalObject().setJkChristmasTreeT1(jkChristmasTreeT1);
    }

    public double getJkChristmasTreeT2() {
        return this.getInternalObject().getJkChristmasTreeT2();
    }

    public void setJkChristmasTreeT2(double jkChristmasTreeT2) {
        this.getInternalObject().setJkChristmasTreeT2(jkChristmasTreeT2);
    }

    public double getJkChristmasTreeT3() {
        return this.getInternalObject().getJkChristmasTreeT3();
    }

    public void setJkChristmasTreeT3(double jkChristmasTreeT3) {
        this.getInternalObject().setJkChristmasTreeT3(jkChristmasTreeT3);
    }

    public String getJkChristmasTreeR1() {
        return this.getInternalObject().getJkChristmasTreeR1();
    }

    public void setJkChristmasTreeR1(String jkChristmasTreeR1) {
        this.getInternalObject().setJkChristmasTreeR1(jkChristmasTreeR1);
    }

    public String getJkChristmasTreeR2() {
        return this.getInternalObject().getJkChristmasTreeR2();
    }

    public void setJkChristmasTreeR2(String jkChristmasTreeR2) {
        this.getInternalObject().setJkChristmasTreeR2(jkChristmasTreeR2);
    }

    public String getJkChristmasTreeR3() {
        return this.getInternalObject().getJkChristmasTreeR3();
    }

    public void setJkChristmasTreeR3(String jkChristmasTreeR3) {
        this.getInternalObject().setJkChristmasTreeR3(jkChristmasTreeR3);
    }

    public String getJkChristmasTreeUser() {
        return this.getInternalObject().getJkChristmasTreeUser();
    }

    public void setJkChristmasTreeUser(String jkChristmasTreeUser) {
        this.getInternalObject().setJkChristmasTreeUser(jkChristmasTreeUser);
    }

    public String getJkChristmasTreeDate() {
        return this.getInternalObject().getJkChristmasTreeDate();
    }

    public void setJkChristmasTreeDate(String jkChristmasTreeDate) {
        this.getInternalObject().setJkChristmasTreeDate(jkChristmasTreeDate);
    }

    public boolean isJkWellheadAssemblyFlag() {
        return this.getInternalObject().isJkWellheadAssemblyFlag();
    }

    public void setJkWellheadAssemblyFlag(boolean jkWellheadAssemblyFlag) {
        this.getInternalObject().setJkWellheadAssemblyFlag(jkWellheadAssemblyFlag);
    }

    public String getJkWellheadAssemblyStationName() {
        return this.getInternalObject().getJkWellheadAssemblyStationName();
    }

    public void setJkWellheadAssemblyStationName(String jkWellheadAssemblyStationName) {
        this.getInternalObject().setJkWellheadAssemblyStationName(jkWellheadAssemblyStationName);
    }

    public String getJkWellheadAssemblyStationNo() {
        return this.getInternalObject().getJkWellheadAssemblyStationNo();
    }

    public void setJkWellheadAssemblyStationNo(String jkWellheadAssemblyStationNo) {
        this.getInternalObject().setJkWellheadAssemblyStationNo(jkWellheadAssemblyStationNo);
    }

    public String getJkWellheadAssemblyRecord() {
        return this.getInternalObject().getJkWellheadAssemblyRecord();
    }

    public void setJkWellheadAssemblyRecord(String jkWellheadAssemblyRecord) {
        this.getInternalObject().setJkWellheadAssemblyRecord(jkWellheadAssemblyRecord);
    }

    public double getJkWellheadP1() {
        return this.getInternalObject().getJkWellheadP1();
    }

    public void setJkWellheadP1(double jkWellheadP1) {
        this.getInternalObject().setJkWellheadP1(jkWellheadP1);
    }

    public double getJkWellheadP2() {
        return this.getInternalObject().getJkWellheadP2();
    }

    public void setJkWellheadP2(double jkWellheadP2) {
        this.getInternalObject().setJkWellheadP2(jkWellheadP2);
    }

    public double getJkWellheadP3() {
        return this.getInternalObject().getJkWellheadP3();
    }

    public void setJkWellheadP3(double jkWellheadP3) {
        this.getInternalObject().setJkWellheadP3(jkWellheadP3);
    }

    public double getJkWellheadT1() {
        return this.getInternalObject().getJkWellheadT1();
    }

    public void setJkWellheadT1(double jkWellheadT1) {
        this.getInternalObject().setJkWellheadT1(jkWellheadT1);
    }

    public double getJkWellheadT2() {
        return this.getInternalObject().getJkWellheadT2();
    }

    public void setJkWellheadT2(double jkWellheadT2) {
        this.getInternalObject().setJkWellheadT2(jkWellheadT2);
    }

    public double getJkWellheadT3() {
        return this.getInternalObject().getJkWellheadT3();
    }

    public void setJkWellheadT3(double jkWellheadT3) {
        this.getInternalObject().setJkWellheadT3(jkWellheadT3);
    }

    public String getJkWellheadR1() {
        return this.getInternalObject().getJkWellheadR1();
    }

    public void setJkWellheadR1(String jkWellheadR1) {
        this.getInternalObject().setJkWellheadR1(jkWellheadR1);
    }

    public String getJkWellheadR2() {
        return this.getInternalObject().getJkWellheadR2();
    }

    public void setJkWellheadR2(String jkWellheadR2) {
        this.getInternalObject().setJkWellheadR2(jkWellheadR2);
    }

    public String getJkWellheadR3() {
        return this.getInternalObject().getJkWellheadR3();
    }

    public void setJkWellheadR3(String jkWellheadR3) {
        this.getInternalObject().setJkWellheadR3(jkWellheadR3);
    }

    public String getJkWellheadUser() {
        return this.getInternalObject().getJkWellheadUser();
    }

    public void setJkWellheadUser(String jkWellheadUser) {
        this.getInternalObject().setJkWellheadUser(jkWellheadUser);
    }

    public String getJkWellheadDate() {
        return this.getInternalObject().getJkWellheadDate();
    }

    public void setJkWellheadDate(String jkWellheadDate) {
        this.getInternalObject().setJkWellheadDate(jkWellheadDate);
    }

    public boolean isJkCrossoverAssemblyFlag() {
        return this.getInternalObject().isJkCrossoverAssemblyFlag();
    }

    public void setJkCrossoverAssemblyFlag(boolean jkCrossoverAssemblyFlag) {
        this.getInternalObject().setJkCrossoverAssemblyFlag(jkCrossoverAssemblyFlag);
    }

    public String getJkCrossoverAssemblyTopStationName() {
        return this.getInternalObject().getJkCrossoverAssemblyTopStationName();
    }

    public void setJkCrossoverAssemblyTopStationName(String jkCrossoverAssemblyTopStationName) {
        this.getInternalObject().setJkCrossoverAssemblyTopStationName(jkCrossoverAssemblyTopStationName);
    }

    public String getJkCrossoverAssemblyTopStationNo() {
        return this.getInternalObject().getJkCrossoverAssemblyTopStationNo();
    }

    public void setJkCrossoverAssemblyTopStationNo(String jkCrossoverAssemblyTopStationNo) {
        this.getInternalObject().setJkCrossoverAssemblyTopStationNo(jkCrossoverAssemblyTopStationNo);
    }

    public String getJkCrossoverAssemblyBtmStationName() {
        return this.getInternalObject().getJkCrossoverAssemblyBtmStationName();
    }

    public void setJkCrossoverAssemblyBtmStationName(String jkCrossoverAssemblyBtmStationName) {
        this.getInternalObject().setJkCrossoverAssemblyBtmStationName(jkCrossoverAssemblyBtmStationName);
    }

    public String getJkCrossoverAssemblyBtmStationNo() {
        return this.getInternalObject().getJkCrossoverAssemblyBtmStationNo();
    }

    public void setJkCrossoverAssemblyBtmStationNo(String jkCrossoverAssemblyBtmStationNo) {
        this.getInternalObject().setJkCrossoverAssemblyBtmStationNo(jkCrossoverAssemblyBtmStationNo);
    }

    public String getJkCrossoverAssemblyTopRecord() {
        return this.getInternalObject().getJkCrossoverAssemblyTopRecord();
    }

    public void setJkCrossoverAssemblyTopRecord(String jkCrossoverAssemblyTopRecord) {
        this.getInternalObject().setJkCrossoverAssemblyTopRecord(jkCrossoverAssemblyTopRecord);
    }

    public String getJkCrossoverAssemblyBtmRecord() {
        return this.getInternalObject().getJkCrossoverAssemblyBtmRecord();
    }

    public void setJkCrossoverAssemblyBtmRecord(String jkCrossoverAssemblyBtmRecord) {
        this.getInternalObject().setJkCrossoverAssemblyBtmRecord(jkCrossoverAssemblyBtmRecord);
    }

    public double getJkCrossoverAssemblyTopP1() {
        return this.getInternalObject().getJkCrossoverAssemblyTopP1();
    }

    public void setJkCrossoverAssemblyTopP1(double jkCrossoverAssemblyTopP1) {
        this.getInternalObject().setJkCrossoverAssemblyTopP1(jkCrossoverAssemblyTopP1);
    }

    public double getJkCrossoverAssemblyTopP2() {
        return this.getInternalObject().getJkCrossoverAssemblyTopP2();
    }

    public void setJkCrossoverAssemblyTopP2(double jkCrossoverAssemblyTopP2) {
        this.getInternalObject().setJkCrossoverAssemblyTopP2(jkCrossoverAssemblyTopP2);
    }

    public double getJkCrossoverAssemblyTopP3() {
        return this.getInternalObject().getJkCrossoverAssemblyTopP3();
    }

    public void setJkCrossoverAssemblyTopP3(double jkCrossoverAssemblyTopP3) {
        this.getInternalObject().setJkCrossoverAssemblyTopP3(jkCrossoverAssemblyTopP3);
    }

    public double getJkCrossoverAssemblyTopT1() {
        return this.getInternalObject().getJkCrossoverAssemblyTopT1();
    }

    public void setJkCrossoverAssemblyTopT1(double jkCrossoverAssemblyTopT1) {
        this.getInternalObject().setJkCrossoverAssemblyTopT1(jkCrossoverAssemblyTopT1);
    }

    public double getJkCrossoverAssemblyTopT2() {
        return this.getInternalObject().getJkCrossoverAssemblyTopT2();
    }

    public void setJkCrossoverAssemblyTopT2(double jkCrossoverAssemblyTopT2) {
        this.getInternalObject().setJkCrossoverAssemblyTopT2(jkCrossoverAssemblyTopT2);
    }

    public double getJkCrossoverAssemblyTopT3() {
        return this.getInternalObject().getJkCrossoverAssemblyTopT3();
    }

    public void setJkCrossoverAssemblyTopT3(double jkCrossoverAssemblyTopT3) {
        this.getInternalObject().setJkCrossoverAssemblyTopT3(jkCrossoverAssemblyTopT3);
    }

    public String getJkCrossoverAssemblyTopR1() {
        return this.getInternalObject().getJkCrossoverAssemblyTopR1();
    }

    public void setJkCrossoverAssemblyTopR1(String jkCrossoverAssemblyTopR1) {
        this.getInternalObject().setJkCrossoverAssemblyTopR1(jkCrossoverAssemblyTopR1);
    }

    public String getJkCrossoverAssemblyTopR2() {
        return this.getInternalObject().getJkCrossoverAssemblyTopR2();
    }

    public void setJkCrossoverAssemblyTopR2(String jkCrossoverAssemblyTopR2) {
        this.getInternalObject().setJkCrossoverAssemblyTopR2(jkCrossoverAssemblyTopR2);
    }

    public String getJkCrossoverAssemblyTopR3() {
        return this.getInternalObject().getJkCrossoverAssemblyTopR3();
    }

    public void setJkCrossoverAssemblyTopR3(String jkCrossoverAssemblyTopR3) {
        this.getInternalObject().setJkCrossoverAssemblyTopR3(jkCrossoverAssemblyTopR3);
    }

    public double getJkCrossoverAssemblyBtmP1() {
        return this.getInternalObject().getJkCrossoverAssemblyBtmP1();
    }

    public void setJkCrossoverAssemblyBtmP1(double jkCrossoverAssemblyBtmP1) {
        this.getInternalObject().setJkCrossoverAssemblyBtmP1(jkCrossoverAssemblyBtmP1);
    }

    public double getJkCrossoverAssemblyBtmP2() {
        return this.getInternalObject().getJkCrossoverAssemblyBtmP2();
    }

    public void setJkCrossoverAssemblyBtmP2(double jkCrossoverAssemblyBtmP2) {
        this.getInternalObject().setJkCrossoverAssemblyBtmP2(jkCrossoverAssemblyBtmP2);
    }

    public double getJkCrossoverAssemblyBtmP3() {
        return this.getInternalObject().getJkCrossoverAssemblyBtmP3();
    }

    public void setJkCrossoverAssemblyBtmP3(double jkCrossoverAssemblyBtmP3) {
        this.getInternalObject().setJkCrossoverAssemblyBtmP3(jkCrossoverAssemblyBtmP3);
    }

    public double getJkCrossoverAssemblyBtmT1() {
        return this.getInternalObject().getJkCrossoverAssemblyBtmT1();
    }

    public void setJkCrossoverAssemblyBtmT1(double jkCrossoverAssemblyBtmT1) {
        this.getInternalObject().setJkCrossoverAssemblyBtmT1(jkCrossoverAssemblyBtmT1);
    }

    public double getJkCrossoverAssemblyBtmT2() {
        return this.getInternalObject().getJkCrossoverAssemblyBtmT2();
    }

    public void setJkCrossoverAssemblyBtmT2(double jkCrossoverAssemblyBtmT2) {
        this.getInternalObject().setJkCrossoverAssemblyBtmT2(jkCrossoverAssemblyBtmT2);
    }

    public double getJkCrossoverAssemblyBtmT3() {
        return this.getInternalObject().getJkCrossoverAssemblyBtmT3();
    }

    public void setJkCrossoverAssemblyBtmT3(double jkCrossoverAssemblyBtmT3) {
        this.getInternalObject().setJkCrossoverAssemblyBtmT3(jkCrossoverAssemblyBtmT3);
    }

    public String getJkCrossoverAssemblyBtmR1() {
        return this.getInternalObject().getJkCrossoverAssemblyBtmR1();
    }

    public void setJkCrossoverAssemblyBtmR1(String jkCrossoverAssemblyBtmR1) {
        this.getInternalObject().setJkCrossoverAssemblyBtmR1(jkCrossoverAssemblyBtmR1);
    }

    public String getJkCrossoverAssemblyBtmR2() {
        return this.getInternalObject().getJkCrossoverAssemblyBtmR2();
    }

    public void setJkCrossoverAssemblyBtmR2(String jkCrossoverAssemblyBtmR2) {
        this.getInternalObject().setJkCrossoverAssemblyBtmR2(jkCrossoverAssemblyBtmR2);
    }

    public String getJkCrossoverAssemblyBtmR3() {
        return this.getInternalObject().getJkCrossoverAssemblyBtmR3();
    }

    public void setJkCrossoverAssemblyBtmR3(String jkCrossoverAssemblyBtmR3) {
        this.getInternalObject().setJkCrossoverAssemblyBtmR3(jkCrossoverAssemblyBtmR3);
    }

    public String getJkCrossoverAssemblyUser() {
        return this.getInternalObject().getJkCrossoverAssemblyUser();
    }

    public void setJkCrossoverAssemblyUser(String jkCrossoverAssemblyUser) {
        this.getInternalObject().setJkCrossoverAssemblyUser(jkCrossoverAssemblyUser);
    }

    public String getJkCrossoverAssemblyDate() {
        return this.getInternalObject().getJkCrossoverAssemblyDate();
    }

    public void setJkCrossoverAssemblyDate(String jkCrossoverAssemblyDate) {
        this.getInternalObject().setJkCrossoverAssemblyDate(jkCrossoverAssemblyDate);
    }

    public String getDriftTestDate() {
        return this.getInternalObject().getDriftTestDate();
    }

    public void setDriftTestDate(String driftTestDate) {
        this.getInternalObject().setDriftTestDate(driftTestDate);
    }
}