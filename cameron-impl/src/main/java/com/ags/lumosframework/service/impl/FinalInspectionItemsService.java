package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.FinalInspectionItemsEntity;
import com.ags.lumosframework.entity.FinalInspectionResultEntity;
import com.ags.lumosframework.handler.IFinalInspectionItemsHandler;
import com.ags.lumosframework.pojo.FinalInspectionItems;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IFinalInspectionItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class FinalInspectionItemsService extends AbstractBaseDomainObjectService<FinalInspectionItems, FinalInspectionItemsEntity>
        implements IFinalInspectionItemsService {

    @Autowired
    private IFinalInspectionItemsHandler finalInspectionItemsHandler;

    @Override
    public List<FinalInspectionItems> getAllData() {
        EntityFilter filter = this.createFilter();
        filter.isNotNull(FinalInspectionItemsEntity.INSPECTION_ITEM_NAME, false);
        int count = countByFilter(filter);
        filter.setStartPosition(0);
        filter.setMaxResult(count);
        return listByFilter(filter);
    }

    @Override
    protected IBaseEntityHandler<FinalInspectionItemsEntity> getEntityHandler() {
        return finalInspectionItemsHandler;
    }

}
