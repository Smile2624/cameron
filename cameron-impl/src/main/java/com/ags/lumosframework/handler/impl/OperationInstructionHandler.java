package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.OperationInstructionDao;
import com.ags.lumosframework.entity.OperationInstructionEntity;
import com.ags.lumosframework.handler.IOperationInstructionHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class OperationInstructionHandler extends AbstractBaseEntityHandler<OperationInstructionEntity> implements IOperationInstructionHandler {

    @Autowired
    private OperationInstructionDao dao;
    @Override
    protected BaseEntityDao<OperationInstructionEntity> getDao() {
        return dao;
    }
}
