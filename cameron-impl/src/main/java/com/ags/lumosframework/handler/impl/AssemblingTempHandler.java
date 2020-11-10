package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.AssemblingTempDao;
import com.ags.lumosframework.entity.AssemblingTempEntity;
import com.ags.lumosframework.handler.IAssemblingTempHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class AssemblingTempHandler extends AbstractBaseEntityHandler<AssemblingTempEntity> implements IAssemblingTempHandler {

    private static final long serialVersionUID = -6901264602022745947L;

    @Autowired
    private AssemblingTempDao assemblingTempDao;

    @Override
    protected BaseEntityDao<AssemblingTempEntity> getDao() {
        return assemblingTempDao;
    }
}
