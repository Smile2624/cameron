package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.OperationInstruction;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

import java.util.List;

public interface IOperationInstructionService extends IBaseDomainObjectService<OperationInstruction> {

    OperationInstruction getByInspectionName(String name);

    List<OperationInstruction> listByInspectionName(String name);

    List<OperationInstruction> getSuperInstructions();

    OperationInstruction getSuperByChildren(String superName);

    List<OperationInstruction> getChildrenByInstructionType(String tocType,String insName,Boolean removeDelete);

    List<OperationInstruction> getByTypeAndDepartment(String tocType,String department);
}
