package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.CaConfigDao;
import com.ags.lumosframework.entity.CaConfigEntity;
import com.ags.lumosframework.handler.ICaConfigHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @author peyton
 * @date 2019/10/18 10:29
 */
@Service
@Primary
public class CaConfigHandler extends AbstractBaseEntityHandler<CaConfigEntity> implements ICaConfigHandler {

    private static final long serialVersionUID = 8608251126033760044L;

    @Autowired
    private CaConfigDao caConfigDao;

    @Override
    protected BaseEntityDao<CaConfigEntity> getDao() {
        return caConfigDao;
    }
}
