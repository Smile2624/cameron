package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.AppearanceInstrumentationResultEntity;
import com.ags.lumosframework.handler.IAppearanceInstrumentationResultHandler;
import com.ags.lumosframework.pojo.AppearanceInstrumentationResult;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IAppearanceInstrumentationResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class AppearanceInstrumentationResultService extends AbstractBaseDomainObjectService<AppearanceInstrumentationResult,AppearanceInstrumentationResultEntity> implements IAppearanceInstrumentationResultService {
	
	@Autowired
	private IAppearanceInstrumentationResultHandler appearanceInstrumentationResultHandler;

	@Override
	protected IBaseEntityHandler<AppearanceInstrumentationResultEntity> getEntityHandler() {
		return appearanceInstrumentationResultHandler;
	}
	
	@Override
	public List<AppearanceInstrumentationResult> getByNo(String orderNo){
		EntityFilter filter = createFilter();
        if (orderNo != null && !"".equals(orderNo)){
            filter.fieldEqualTo(AppearanceInstrumentationResultEntity.ORDER_NO, orderNo);
        }
        return listByFilter(filter);
	}

	@Override//Changed by Cameron: 加入通过SN获得外观检验结果的方法
	public AppearanceInstrumentationResult getBySN(String sn){
		EntityFilter filter = createFilter();
		if (sn != null && !"".equals(sn)){
			filter.fieldEqualTo(AppearanceInstrumentationResultEntity.SN, sn);
		}
		return getByFilter(filter);
	}
}
