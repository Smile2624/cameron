package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.BomDao;
import com.ags.lumosframework.entity.BomEntity;
import com.ags.lumosframework.handler.IBomHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class BomHandler extends AbstractBaseEntityHandler<BomEntity> implements IBomHandler {

    private static final long serialVersionUID = 4696437390780780519L;

    @Autowired
    private BomDao bomDao;

    @Override
    protected BaseEntityDao<BomEntity> getDao() {
        return bomDao;
    }
}
