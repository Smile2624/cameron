package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.LevpDao;
import com.ags.lumosframework.entity.LevpEntity;
import com.ags.lumosframework.handler.ILevpHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import com.ags.lumosframework.pojo.Levp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class LevpHandler extends AbstractBaseEntityHandler<LevpEntity> implements ILevpHandler {
    @Autowired
    private LevpDao levpDao;

    @Override
    protected BaseEntityDao<LevpEntity> getDao() {
        return levpDao;
    }
}
