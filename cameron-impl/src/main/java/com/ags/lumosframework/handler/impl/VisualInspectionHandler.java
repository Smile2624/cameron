package com.ags.lumosframework.handler.impl;

import com.ags.lumosframework.dao.VisualInspectionDao;
import com.ags.lumosframework.entity.VisualInspectionEntity;
import com.ags.lumosframework.handler.IVisualInspectionHandler;
import com.ags.lumosframework.impl.base.dao.core.BaseEntityDao;
import com.ags.lumosframework.impl.base.handler.AbstractBaseEntityHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class VisualInspectionHandler extends AbstractBaseEntityHandler<VisualInspectionEntity> implements IVisualInspectionHandler{

    private static final long serialVersionUID = 1L;

    @Autowired
    private VisualInspectionDao dao;
    @Override
    protected BaseEntityDao<VisualInspectionEntity> getDao() {
        return dao;
    }

}
