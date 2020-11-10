package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.HardnessEntity;
import com.ags.lumosframework.handler.IHardnessHandler;
import com.ags.lumosframework.pojo.Hardness;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IHardnessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class HardnessService extends AbstractBaseDomainObjectService<Hardness, HardnessEntity> implements IHardnessService {

    @Autowired
    private IHardnessHandler hardnessHandler;

    @Override
    protected IBaseEntityHandler<HardnessEntity> getEntityHandler() {
        return hardnessHandler;
    }
    
    @Override
    public Hardness getByHardnessFile(String hardnessFile) {
    	EntityFilter filter = createFilter();
        if (hardnessFile != null && !"".equals(hardnessFile)) {
            filter.fieldEqualTo(HardnessEntity.HARDNESS_NAME, hardnessFile);
        }
        return getByFilter(filter);
    }

	@Override
	public Hardness getByRuleName(String rulerName) {
    	EntityFilter filter = createFilter();
        if (rulerName != null && !"".equals(rulerName)) {
            filter.fieldEqualTo(HardnessEntity.HARDNESS_STAND, rulerName);
        }
        return getByFilter(filter);
	};
}
