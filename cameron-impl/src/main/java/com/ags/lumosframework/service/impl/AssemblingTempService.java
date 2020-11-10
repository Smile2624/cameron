package com.ags.lumosframework.service.impl;

import com.ags.lumosframework.entity.AssemblingEntity;
import com.ags.lumosframework.entity.AssemblingTempEntity;
import com.ags.lumosframework.handler.IAssemblingTempHandler;
import com.ags.lumosframework.impl.handler.DBHandler;
import com.ags.lumosframework.pojo.AssemblingTemp;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.sdk.base.handler.api.IBaseEntityHandler;
import com.ags.lumosframework.sdk.base.service.AbstractBaseDomainObjectService;
import com.ags.lumosframework.service.IAssemblingTempService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class AssemblingTempService extends AbstractBaseDomainObjectService<AssemblingTemp, AssemblingTempEntity> implements IAssemblingTempService {

    @Autowired
    private IAssemblingTempHandler assemblingTempHandler;
    
	@Autowired
	private DBHandler dbHandler;

    @Override
    protected IBaseEntityHandler<AssemblingTempEntity> getEntityHandler() {
        return assemblingTempHandler;
    }



	@Override
	public AssemblingTemp getByOrderNo(String orderNo) {
		EntityFilter filter = createFilter();
		if(!Strings.isNullOrEmpty(orderNo)) {
			filter.fieldEqualTo(AssemblingEntity.ORDER_NO, orderNo);
		}
		filter.orderBy(AssemblingEntity.SN_BATCH, false);
		return getByFilter(filter);
	}


}
