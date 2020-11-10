package com.ags.lumosframework.service;

import com.ags.lumosframework.pojo.CaConfig;
import com.ags.lumosframework.sdk.base.service.api.IBaseDomainObjectService;

/**
 * @author peyton
 * @date 2019/10/18 10:24
 */
public interface ICaConfigService extends IBaseDomainObjectService<CaConfig> {

    CaConfig getConfigByType(String configType);
}
