package com.ags.lumosframework.pojo;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.OperationInstructionEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;
import com.ags.lumosframework.service.IOperationInstructionService;
import com.ags.lumosframework.service.IProductionOrderService;
import com.google.common.base.Strings;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OperationInstruction extends ObjectBaseImpl<OperationInstructionEntity> {
    public OperationInstruction(OperationInstructionEntity entity) {
        super(entity);
    }

    public OperationInstruction() {
        super(null);
    }

    @Override
    public String getName() {
        return this.getInternalObject().getInstructionName();
    }

    public String getInstructionName() {
        return this.getInternalObject().getInstructionName();
    }

    public void setInstructionName(String instructionName) {
        this.getInternalObject().setInstructionName(instructionName);
    }

    public String getInstructionDesc() {
        return this.getInternalObject().getInstructionDesc();
    }

    public void setInstructionDesc(String instructionDesc) {
        this.getInternalObject().setInstructionDesc(instructionDesc);
    }

    public String getInstructionRev() {
        return this.getInternalObject().getInstructionRev();
    }

    public void setInstructionRev(String instructionRev) {
        this.getInternalObject().setInstructionRev(instructionRev);
    }

    public String getModifiedDate() {
        return this.getInternalObject().getModifiedDate();
    }

    public void setModifiedDate(String modifiedDate) {
        this.getInternalObject().setModifiedDate(modifiedDate);
    }


    public String getTocType() {
        return this.getInternalObject().getTocType();
    }

    public void setTocType(String tocType) {
        this.getInternalObject().setTocType(tocType);
    }

    public LocalDate getModifiedDate1() {
        if (Strings.isNullOrEmpty(getModifiedDate())) {
            return LocalDate.now();
        }
        String mDate = getModifiedDate();
        String[] split = mDate.split("-");
        return LocalDate.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }

    public void setModifiedDate1(LocalDate modifiedDate) {
        setModifiedDate(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(modifiedDate));
    }

    public String getWiDepartment() {
        return this.getInternalObject().getWiDepartment();
    }

    public void setWiDepartment(String wiDepartment) {
        this.getInternalObject().setWiDepartment(wiDepartment);
    }

    public boolean getDeleteFlag() {
        return this.getInternalObject().isDeleteFlag();
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.getInternalObject().setDeleteFlag(deleteFlag);
    }

    //获取子类
    public List<OperationInstruction> getChild(String insName,Boolean removeDelete) {
        IOperationInstructionService operationInstructionService = BeanManager.getService(IOperationInstructionService.class);
        return operationInstructionService.getChildrenByInstructionType(getInstructionName(),insName,removeDelete);
    }

    //获取父类
    public OperationInstruction getSuper() {
        if ("Super".equals(this.getTocType())) {
            return this;
        }
        IOperationInstructionService operationInstructionService = BeanManager.getService(IOperationInstructionService.class);
        return operationInstructionService.getSuperByChildren(this.getTocType());
    }

}
