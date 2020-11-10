package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.LevpEntity;
import com.ags.lumosframework.handler.ILevpHandler;
import com.ags.lumosframework.pojo.Levp;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.ILevpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class LevpService extends AbstractBaseDomainObjectService<Levp, LevpEntity> implements ILevpService {
    @Autowired
    private ILevpHandler levpHandler;

    @Override
    protected IBaseEntityHandler<LevpEntity> getEntityHandler() {
        return levpHandler;
    }

    @Override
    public Levp getByOrderNo(String productionOrder) {
        EntityFilter filter = createFilter();
        if (productionOrder != null && !"".equals(productionOrder)) {
            filter.fieldEqualTo(LevpEntity.PRODUCTION_ORDER_NO, productionOrder);
        }
        return getByFilter(filter);
    }

}
