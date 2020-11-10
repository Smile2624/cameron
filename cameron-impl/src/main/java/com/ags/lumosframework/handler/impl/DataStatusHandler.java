package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.DataStatusDao;
import com.ags.lumosframework.entity.DataStatusEntity;
import com.ags.lumosframework.handler.IDataStatusHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class DataStatusHandler extends AbstractBaseEntityHandler<DataStatusEntity> implements IDataStatusHandler{

    @Autowired
    private DataStatusDao dao;
    @Override
    protected BaseEntityDao<DataStatusEntity> getDao() {
        return dao;
    }
}
