package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.InspectionPlanDao;
import com.ags.lumosframework.entity.InspectionPlanEntity;
import com.ags.lumosframework.handler.IInspectionPlanHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class InspectionPlanHandler extends AbstractBaseEntityHandler<InspectionPlanEntity> implements IInspectionPlanHandler{
    @Autowired
    private InspectionPlanDao dao;
    @Override
    protected BaseEntityDao<InspectionPlanEntity> getDao() {
        return dao;
    }
}
