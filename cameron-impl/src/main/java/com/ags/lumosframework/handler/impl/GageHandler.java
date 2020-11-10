package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.GageDao;
import com.ags.lumosframework.entity.GageEntity;
import com.ags.lumosframework.handler.IGageHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class GageHandler extends AbstractBaseEntityHandler<GageEntity> implements IGageHandler {

    private static final long serialVersionUID = 7822097096617444158L;

    @Autowired
    private GageDao gageDao;

    @Override
    protected BaseEntityDao<GageEntity> getDao() {
        return gageDao;
    }
}
