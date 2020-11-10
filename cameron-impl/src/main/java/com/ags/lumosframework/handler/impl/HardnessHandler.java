package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.HardnessDao;
import com.ags.lumosframework.entity.HardnessEntity;
import com.ags.lumosframework.handler.IHardnessHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class HardnessHandler extends AbstractBaseEntityHandler<HardnessEntity> implements IHardnessHandler {

    private static final long serialVersionUID = -921963431620650960L;

    @Autowired
    private HardnessDao hardnessDao;

    @Override
    protected BaseEntityDao<HardnessEntity> getDao() {
        return hardnessDao;
    }
}
