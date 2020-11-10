package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.OperationInstructionEntity;
import com.ags.lumosframework.handler.IOperationInstructionHandler;
import com.ags.lumosframework.pojo.OperationInstruction;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IOperationInstructionService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class OperationInstructionService extends AbstractBaseDomainObjectService<OperationInstruction, OperationInstructionEntity> implements IOperationInstructionService {

    @Autowired
    private IOperationInstructionHandler handler;

    @Override
    protected IBaseEntityHandler<OperationInstructionEntity> getEntityHandler() {
        return handler;
    }

    @Override
    public OperationInstruction getByInspectionName(String name) {
        EntityFilter filter = createFilter();
        filter.fieldEqualTo(OperationInstructionEntity.INSTRUCTION_NAME, name);
        return getByFilter(filter);
    }

    @Override
    public List<OperationInstruction> listByInspectionName(String name) {
        EntityFilter filter = createFilter();
        if (!Strings.isNullOrEmpty(name)) {
            filter.fieldContains(OperationInstructionEntity.INSTRUCTION_NAME, name);
        }
        return listByFilter(filter);
    }

    @Override
    public List<OperationInstruction> getSuperInstructions() {
        EntityFilter filter = createFilter();
        filter.fieldEqualTo(OperationInstructionEntity.TOC_TYPE, "Super");
        return listByFilter(filter);
    }

    @Override
    public OperationInstruction getSuperByChildren(String superName) {
        EntityFilter filter = createFilter();
        filter.fieldEqualTo(OperationInstructionEntity.INSTRUCTION_NAME, superName);
        filter.fieldEqualTo(OperationInstructionEntity.TOC_TYPE, "Super");
        return getByFilter(filter);
    }

    @Override
    public List<OperationInstruction> getChildrenByInstructionType(String tocType,String insName,Boolean removeDelete) {
        EntityFilter filter = createFilter();
        if (!Strings.isNullOrEmpty(tocType)) {
            filter.fieldEqualTo(OperationInstructionEntity.TOC_TYPE, tocType);
        }
        if(!Strings.isNullOrEmpty(insName)){
            filter.fieldContains(OperationInstructionEntity.INSTRUCTION_NAME,insName);
        }
        if(removeDelete){//除去删除的
            filter.fieldEqualTo(OperationInstructionEntity.DELETE_FLAG,false);//未删除
        }

        filter.orderBy(OperationInstructionEntity.INSTRUCTION_NAME,false);
        return listByFilter(filter);
    }

    @Override
    public List<OperationInstruction> getByTypeAndDepartment(String tocType,String department) {
        EntityFilter filter = createFilter();
        if (!Strings.isNullOrEmpty(tocType)) {
            filter.fieldEqualTo(OperationInstructionEntity.TOC_TYPE, tocType);
        }
        filter.fieldEqualTo(OperationInstructionEntity.WI_DEPARTMENT, department);
        filter.orderBy(OperationInstructionEntity.INSTRUCTION_NAME,false);
        return listByFilter(filter);
    }

}
