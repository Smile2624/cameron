package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.VisualInspectionEntity;
import com.ags.lumosframework.handler.IVisualInspectionHandler;
import com.ags.lumosframework.pojo.VisualInspection;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IVisualInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class VisualInspectionService extends AbstractBaseDomainObjectService<VisualInspection, VisualInspectionEntity> implements IVisualInspectionService {

    @Autowired
    private IVisualInspectionHandler handler;
    @Override
    protected IBaseEntityHandler<VisualInspectionEntity> getEntityHandler() {
        return handler;
    }
    @Override
    public VisualInspection getBySapLotNo(String sapLotNo) {
        // TODO Auto-generated method stub
        EntityFilter filter = this.createFilter();
        filter.fieldEqualTo(VisualInspectionEntity.SAP_INSPECTION_NO, sapLotNo);
        return getByFilter(filter);
    }

}
