package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.CaConfigEntity;
import com.ags.lumosframework.handler.ICaConfigHandler;
import com.ags.lumosframework.pojo.CaConfig;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.ICaConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * @author peyton
 * @date 2019/10/18 10:25
 */
@Service
@Primary
public class CaConfigService extends AbstractBaseDomainObjectService<CaConfig, CaConfigEntity> implements ICaConfigService {

    @Autowired
    private ICaConfigHandler configHandler;

    @Override
    protected IBaseEntityHandler<CaConfigEntity> getEntityHandler() {
        return configHandler;
    }

    @Override
    public CaConfig getConfigByType(String configType) {
        EntityFilter filter = createFilter();
        if (configType != null && !"".equals(configType)) {
            filter.fieldEqualTo(CaConfigEntity.CONFIG_TYPE, configType);
        }
        return getByFilter(filter);
    }
}
