package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.UniqueTraceabilityEntity;
import com.ags.lumosframework.handler.IUniqueTraceabilityHandler;
import com.ags.lumosframework.pojo.UniqueTraceability;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IUniqueTraceabilityService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class UniqueTraceabilityService extends AbstractBaseDomainObjectService<UniqueTraceability,UniqueTraceabilityEntity> 
				implements IUniqueTraceabilityService {

	
	@Autowired
	private IUniqueTraceabilityHandler handler;
	@Override
	protected IBaseEntityHandler<UniqueTraceabilityEntity> getEntityHandler() {
		return handler;
	}
	@Override
	public int countByCustOrder(String custOrder) {
		EntityFilter filter = this.createFilter();
		if(!Strings.isNullOrEmpty(custOrder)) {
			filter.fieldEqualTo(UniqueTraceabilityEntity.CUST_ORDER, custOrder);
		}
		return countByFilter(filter);
	}
	@Override
	public List<UniqueTraceability> getByPo(String po) {
		EntityFilter filter = this.createFilter();
		if(!Strings.isNullOrEmpty(po)) {
			filter.fieldEqualTo(UniqueTraceabilityEntity.PURCHASING_ORDER, po);
		}
		return listByFilter(filter);
	}
	@Override
	public List<UniqueTraceability> getByPoSn(String po) {
		EntityFilter filter = this.createFilter();
		if(!Strings.isNullOrEmpty(po)) {
			filter.fieldContains(UniqueTraceabilityEntity.PURCHASING_NO, po);
		}
		return listByFilter(filter);
	}
	@Override
	public UniqueTraceability getByItem(String serialNo) {
		// TODO Auto-generated method stub
		EntityFilter filter = this.createFilter();
		if(!Strings.isNullOrEmpty(serialNo)) {
			filter.fieldContains(UniqueTraceabilityEntity.PURCHASING_NO, serialNo);
		}
		return getByFilter(filter);
	}

}
