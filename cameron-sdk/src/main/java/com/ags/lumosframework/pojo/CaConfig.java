package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.CaConfigEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

/**
 * @author peyton
 * @date 2019/10/18 10:20
 */
public class CaConfig extends ObjectBaseImpl<CaConfigEntity> {

    private static final long serialVersionUID = -4126132000958206058L;

    public CaConfig() {
        super(null);
    }

    public CaConfig(CaConfigEntity entity) {
        super(entity);
    }


    @Override
    public String getName() {
        return null;
    }

    public String getConfigType() {
        return getInternalObject().getConfigType();
    }

    public void setConfigType(String configType) {
        getInternalObject().setConfigType(configType);
    }

    public String getConfigValue() {
        return getInternalObject().getConfigValue();
    }

    public void setConfigValue(String configValue) {
        getInternalObject().setConfigValue(configValue);
    }

    public String getConfigRemark() {
        return getInternalObject().getConfigRemark();
    }

    public void setConfigRemark(String configRemark) {
        getInternalObject().setConfigRemark(configRemark);
    }
}
