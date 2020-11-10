package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.GageEntity;
import com.ags.lumosframework.handler.IGageHandler;
import com.ags.lumosframework.pojo.Gage;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IGageService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class GageService extends AbstractBaseDomainObjectService<Gage, GageEntity> implements IGageService {

    @Autowired
    private IGageHandler gageHandler;

    @Override
    protected IBaseEntityHandler<GageEntity> getEntityHandler() {
        return gageHandler;
    }

	@Override
	public Gage getByGageNo(String gageNo) {
		EntityFilter filter = createFilter();
		if(!Strings.isNullOrEmpty(gageNo) && !"-".equals(gageNo.trim())) {
			filter.fieldEqualTo(GageEntity.GAGE_NO, gageNo);
		}
		return getByFilter(filter);
	}
}
