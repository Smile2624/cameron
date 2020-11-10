package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.SparePartDao;
import com.ags.lumosframework.entity.SparePartEntity;
import com.ags.lumosframework.handler.ISparePartHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class SparePartHandler extends AbstractBaseEntityHandler<SparePartEntity> implements ISparePartHandler {
    private static final long serialVersionUID = 4484455148957749364L;

    @Autowired
    private SparePartDao sparePartDao;

    @Override
    protected BaseEntityDao<SparePartEntity> getDao() {
        return sparePartDao;
    }


}
