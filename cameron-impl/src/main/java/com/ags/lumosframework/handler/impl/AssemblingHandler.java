package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.AssemblingDao;
import com.ags.lumosframework.entity.AssemblingEntity;
import com.ags.lumosframework.handler.IAssemblingHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class AssemblingHandler extends AbstractBaseEntityHandler<AssemblingEntity> implements IAssemblingHandler {

    private static final long serialVersionUID = -6901264602022745947L;

    @Autowired
    private AssemblingDao assemblingDao;

    @Override
    protected BaseEntityDao<AssemblingEntity> getDao() {
        return assemblingDao;
    }
}
